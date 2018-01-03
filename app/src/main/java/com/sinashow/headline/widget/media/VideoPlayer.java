package com.sinashow.headline.widget.media;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.danikula.videocache.HttpProxyCacheServer;
import com.sinashow.headline.R;
import com.sinashow.headline.base.HeadlineApplication;
import com.sinashow.headline.utils.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Administrator on 2017/6/8.
 */

public class VideoPlayer extends FrameLayout implements TextureView.SurfaceTextureListener, MediaPlayerListener {
    public static final String TAG = VideoPlayer.class.getSimpleName();
    public static final int CURRENT_STATE_NORMAL = 0; //正常
    public static final int CURRENT_STATE_PREPAREING = 1; //准备中
    public static final int CURRENT_STATE_PLAYING = 2; //播放中
    public static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3; //开始缓冲
    public static final int CURRENT_STATE_PAUSE = 5; //暂停
    public static final int CURRENT_STATE_AUTO_COMPLETE = 6; //自动播放结束
    public static final int CURRENT_STATE_ERROR = 7; //错误状态

    private boolean mCache = false;//是否播边边缓冲
    private File mCachePath; //缓存目录
    private String mOriginUrl; //原来的url
    private int mCurrentState = -1; //当前的播放状态
    private boolean mCacheFile = false; //是否是缓存的文件
    private String mUrl; //转化后的URL
    private int mBuffterPoint;//缓存进度
    private int mSeekToInAdvance = -1; //// TODO: 2016/11/13 跳过广告
    private long mSeekOnStart = -1; //从哪个开始播放
    private boolean mHadPlay = false;//是否播放过
    private boolean mLooping = false;//循环
    private static int mBackUpPlayingBufferState = -1;
    private Map<String, String> mMapHeadData = new HashMap<>();

    protected float mSpeed = 1;//播放速度，只支持6.0以上

    private long mPauseTime; //保存暂停时的时间

    private long mCurrentPosition; //当前的播放位置

    private String mPlayTag = ""; //播放的tag，防止错误，因为普通的url也可能重复
    private int mPlayPosition = -22; //播放的tag，防止错误，因为普通的url也可能重复

    private ViewGroup mTextureViewContainer; //渲染控件父类
    private ViewGroup mThumbImageViewLayout;//封面父布局
    private View mThumbImageView; //封面
    private Surface mSurface;
    private Context mContext;
    private View mVideoLoading;
    private VideoView mTextureView;

    public VideoPlayer(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        this.mContext = context;
        View.inflate(context, getLayoutId(), this);
        mTextureViewContainer = (ViewGroup) findViewById(R.id.surface_container);
        mThumbImageViewLayout = (ViewGroup) findViewById(R.id.fly_cover);
        mVideoLoading = findViewById(R.id.video_loading);
        if (mThumbImageView != null) {
            mThumbImageViewLayout.removeAllViews();
            resolveThumbImage(mThumbImageView);
        }
//        if (isInEditMode())
//            return;

    }

    public int getLayoutId() {
        return R.layout.layout_video_standard;
    }

    private HttpProxyCacheServer getProxy() {
        return HeadlineApplication.getProxy();
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @return
     */
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath) {
        mCache = cacheWithPlay;
        mCachePath = cachePath;

        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl(url);
        url = proxyUrl;

        mOriginUrl = url;
        if (isCurrentMediaListener())
            return false;
        mCurrentState = CURRENT_STATE_NORMAL;
//        if (cacheWithPlay && url.startsWith("http") && !url.contains("127.0.0.1")) {
//            HttpProxyCacheServer proxy = GSYVideoManager.getProxy(getContext().getApplicationContext(), cachePath);
//            //此处转换了url，然后再赋值给mUrl。
//            url = proxy.getProxyUrl(url);
//            mCacheFile = (!url.startsWith("http"));
//            //注册上缓冲监听
//            if (!mCacheFile && GSYVideoManager.instance() != null) {
//                proxy.registerCacheListener(GSYVideoManager.instance(), mOriginUrl);
//            }
//        } else if (!cacheWithPlay && (!url.startsWith("http") && !url.startsWith("rtmp") && !url.startsWith("rtsp"))) {
//            mCacheFile = true;
//        }
        this.mUrl = url;
        setStateAndUi(CURRENT_STATE_NORMAL);
        prepareVideo();
        return true;
    }

    /**
     * 开始状态视频播放
     */
    protected void prepareVideo() {
        if (VideoManager.instance().listener() != null) {
            VideoManager.instance().listener().onCompletion();
        }
        VideoManager.instance().setListener(this);
        VideoManager.instance().setPlayTag(mPlayTag);
        VideoManager.instance().setPlayPosition(mPlayPosition);
        addTextureView();
        //mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBackUpPlayingBufferState = -1;
        VideoManager.instance().prepare(mUrl, mMapHeadData, mLooping, mSpeed);
        setStateAndUi(CURRENT_STATE_PREPAREING);
    }

    /**
     * 设置播放显示状态
     *
     * @param state
     */
    protected void setStateAndUi(int state) {
        mCurrentState = state;
        switch (mCurrentState) {
            case CURRENT_STATE_NORMAL:
                if (isCurrentMediaListener()) {
                    VideoManager.instance().releaseMediaPlayer();
                    mBuffterPoint = 0;
                }
                changeUiToNormal();
                break;
            case CURRENT_STATE_PREPAREING:
                //resetProgressAndTime();
                changeUiToPrepareingShow();
                break;
            case CURRENT_STATE_PLAYING:
                //startProgressTimer();
                changeUiToPlayingShow();
                break;
            case CURRENT_STATE_PAUSE:
                //startProgressTimer();
                //changeUiToPauseShow();
                break;
            case CURRENT_STATE_ERROR:
                if (isCurrentMediaListener()) {
                    VideoManager.instance().releaseMediaPlayer();
                }
//                changeUiToError();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                changeUiToCompleteShow();
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                changeUiToPlayingBufferingShow();
                break;
        }
    }

    //Unified management Ui
    private void changeUiToNormal() {
        LogUtil.d(TAG, "changeUiToNormal");
        mVideoLoading.setVisibility(View.INVISIBLE);
        mThumbImageViewLayout.setVisibility(View.VISIBLE);
    }

    private void changeUiToPrepareingShow() {
        LogUtil.d(TAG, "changeUiToPrepareingShow");
        mVideoLoading.setVisibility(View.VISIBLE);
        mThumbImageViewLayout.setVisibility(View.VISIBLE);
    }

    private void changeUiToPlayingShow() {
        LogUtil.d(TAG, "changeUiToPlayingShow");
        mVideoLoading.setVisibility(View.INVISIBLE);
        mThumbImageViewLayout.setVisibility(View.INVISIBLE);
    }

    private void changeUiToPlayingBufferingShow() {
        LogUtil.d(TAG, "changeUiToPlayingBufferingShow");
        mVideoLoading.setVisibility(View.VISIBLE);
        mThumbImageViewLayout.setVisibility(View.INVISIBLE);
    }

    private void changeUiToCompleteShow() {
        LogUtil.d(TAG, "changeUiToCompleteShow");
        mVideoLoading.setVisibility(View.INVISIBLE);
        mThumbImageViewLayout.setVisibility(View.VISIBLE);
    }

    private boolean isCurrentMediaListener() {
        return VideoManager.instance().listener() != null
                && VideoManager.instance().listener() == this;
    }

    /**
     * 添加播放的view
     */
    private void addTextureView() {
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }
        mTextureView = null;
        mTextureView = new VideoView(getContext());
        mTextureView.setSurfaceTextureListener(this);

        if (mTextureViewContainer instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextureViewContainer.addView(mTextureView, layoutParams);
        } else if (mTextureViewContainer instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            mTextureViewContainer.addView(mTextureView, layoutParams);
        }
    }

    /**
     * 添加封面
     *
     * @param thumb
     */
    private void resolveThumbImage(View thumb) {
        mThumbImageViewLayout.addView(thumb);
        ViewGroup.LayoutParams layoutParams = thumb.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        thumb.setLayoutParams(layoutParams);
    }

    /***
     * 设置封面
     */
    public void setThumbImageView(View view) {
        if (mThumbImageViewLayout != null) {

            if (mThumbImageView != null) {
                mThumbImageViewLayout.removeAllViews();

            }
            mThumbImageView = view;
            resolveThumbImage(mThumbImageView);
        }
    }

    /***
     * 清除封面
     */
    public void clearThumbImageView() {
        if (mThumbImageViewLayout != null) {
            mThumbImageViewLayout.removeAllViews();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        VideoManager.instance().setDisplay(mSurface);
        //显示暂停切换显示的图片
        //showPauseCover();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        VideoManager.instance().setDisplay(null);
        surface.release();
        //cancelProgressTimer();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onPrepared() {
        if (mCurrentState != CURRENT_STATE_PREPAREING) return;

        if (VideoManager.instance().getMediaPlayer() != null) {
            VideoManager.instance().getMediaPlayer().start();
        }

        if (VideoManager.instance().getMediaPlayer() != null && mSeekToInAdvance != -1) {
            VideoManager.instance().getMediaPlayer().seekTo(mSeekToInAdvance);
            mSeekToInAdvance = -1;
        }
        //setStateAndUi(CURRENT_STATE_PLAYING);//此处隐藏封面，页面感觉黑了一下，等Video真正开始播放的时候再显示播放的状态
        if (VideoManager.instance().getMediaPlayer() != null && mSeekOnStart > 0) {
            VideoManager.instance().getMediaPlayer().seekTo(mSeekOnStart);
            mSeekOnStart = 0;
        }
        mHadPlay = true;
    }

    @Override
    public void onAutoCompletion(IMediaPlayer iMediaPlayer) {
        if (isLooping()) {
            iMediaPlayer.seekTo(0);
            iMediaPlayer.start();
        } else {
            setStateAndUi(CURRENT_STATE_AUTO_COMPLETE);
            if (mTextureViewContainer.getChildCount() > 0) {
                mTextureViewContainer.removeAllViews();
            }
            ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onCompletion() {
        //make me normal first
        setStateAndUi(CURRENT_STATE_NORMAL);
        if (mTextureViewContainer.getChildCount() > 0) {
            mTextureViewContainer.removeAllViews();
        }
        VideoManager.instance().setListener(null);
        VideoManager.instance().setCurrentVideoHeight(0);
        VideoManager.instance().setCurrentVideoWidth(0);
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mCurrentState != CURRENT_STATE_NORMAL && mCurrentState != CURRENT_STATE_PREPAREING) {
            if (percent != 0) {
                mBuffterPoint = percent;
            }
        }
    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onError(int what, int extra) {
        if (what != 38 && what != -38) {
            setStateAndUi(CURRENT_STATE_ERROR);
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mBackUpPlayingBufferState = mCurrentState;
            //避免在onPrepared之前就进入了buffering，导致一只loading
            if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
                setStateAndUi(CURRENT_STATE_PLAYING_BUFFERING_START);

        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            if (mBackUpPlayingBufferState != -1) {
                if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
                    setStateAndUi(mBackUpPlayingBufferState);
                mBackUpPlayingBufferState = -1;
            }
        } else if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            setStateAndUi(CURRENT_STATE_PLAYING);
        }
    }

    @Override
    public void onVideoSizeChanged() {
        int mVideoWidth = VideoManager.instance().getCurrentVideoWidth();
        int mVideoHeight = VideoManager.instance().getCurrentVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            mTextureView.requestLayout();
            mTextureView.scaleVideoSize(mVideoWidth, mVideoHeight);
        }
    }

    @Override
    public void onBackFullscreen() {

    }

    @Override
    public void onVideoPause() {
        if (VideoManager.instance().getMediaPlayer().isPlaying()) {
            setStateAndUi(CURRENT_STATE_PAUSE);
            mPauseTime = System.currentTimeMillis();
            mCurrentPosition = VideoManager.instance().getMediaPlayer().getCurrentPosition();
            if (VideoManager.instance().getMediaPlayer() != null)
                VideoManager.instance().getMediaPlayer().pause();
        }
    }

    @Override
    public void onVideoResume() {
        mPauseTime = 0;
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            if (mCurrentPosition > 0 && VideoManager.instance().getMediaPlayer() != null) {
                setStateAndUi(CURRENT_STATE_PLAYING);
                VideoManager.instance().getMediaPlayer().seekTo(mCurrentPosition);
                VideoManager.instance().getMediaPlayer().start();
            }
        }
    }

    public boolean isLooping() {
        return mLooping;
    }

    /**
     * 设置循环
     */
    public void setLooping(boolean looping) {
        this.mLooping = looping;
    }

    /**
     * 播放tag防止错误，因为普通的url也可能重复
     */
    public String getPlayTag() {
        return mPlayTag;
    }

    /**
     * 播放tag防止错误，因为普通的url也可能重复
     *
     * @param playTag 保证不重复就好
     */
    public void setPlayTag(String playTag) {
        this.mPlayTag = playTag;
    }

    public int getPlayPosition() {
        return mPlayPosition;
    }

    /**
     * 设置播放位置防止错位
     */
    public void setPlayPosition(int playPosition) {
        this.mPlayPosition = playPosition;
    }

    /**
     * 页面销毁了记得调用是否所有的video
     */
    public static void releaseAllVideos() {
        if (VideoManager.instance().listener() != null) {
            VideoManager.instance().listener().onCompletion();
        }
        VideoManager.instance().releaseMediaPlayer();
    }

    /**
     * if I am playing release me
     */
    public void release() {
        if (isCurrentMediaListener()) {
            releaseAllVideos();
        }
        mHadPlay = false;
    }


}
