//
// Source code recreated from mHttpMessage .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sinashow.headline.main.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.caishi.venus.R.dimen;
import com.caishi.venus.R.id;
import com.caishi.venus.R.layout;
import com.caishi.venus.R.string;
import com.caishi.venus.VenusWebEmbedActivity;
import com.caishi.venus.api.bean.http.HttpError;
import com.caishi.venus.api.bean.news.NewsInfo;
import com.caishi.venus.api.bean.resp.Responses.NEWS_LIST;
import com.caishi.venus.api.misc.LogCat;
import com.caishi.venus.api.remote.HttpCallback;
import com.caishi.venus.api.remote.HttpCreator;
import com.caishi.venus.api.remote.HttpMessage;
import com.caishi.venus.details.NewsDetailsLoader;
import com.caishi.venus.ui.config.NewsSettings;
import com.caishi.venus.ui.store.NewsStore;
import com.caishi.venus.ui.view.pulltorefresh.PullToRefreshBase;
import com.caishi.venus.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.caishi.venus.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.caishi.venus.ui.view.pulltorefresh.PullToRefreshListView;
import com.sinashow.headline.R;
import com.sinashow.headline.main.adapter.NewsListAdapter;
import com.sinashow.headline.utils.LogUtil;

import java.util.List;

public class NewsFragment extends Fragment implements OnItemClickListener {
    private HttpMessage mHttpMessage;
    private NewsListAdapter mNewsListAdapter;
    private PullToRefreshListView mPullToRefresh;
    private String mChannelId;
    private String mPageTitle;
    private TextView mTvNewsRefreshNote;
    private int mAnimHeight;
    private View mFootView;
    private View mHeadView;
    private ObjectAnimator objectAnimator;
    private float k;
    private float l;
    private float m;
    private float n;

    public NewsFragment() {
    }

    public static NewsFragment create(String var0, String var1) {
        NewsFragment var2 = new NewsFragment();
        Bundle var3 = new Bundle();
        var3.putString("channelId", var0);
        var3.putString("pageTitle", var1);
        var2.setArguments(var3);
        return var2;
    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        Bundle var2 = this.getArguments();
        this.mChannelId = var2.getString("channelId");
        this.mPageTitle = var2.getString("pageTitle");
    }

    public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, Bundle var3) {
        this.mHeadView = var1.inflate(layout.scene_item_load, (ViewGroup) null, false);
        ImageView var4 = (ImageView) this.mHeadView.findViewById(id.iv_loading);
        ((AnimationDrawable) var4.getDrawable()).start();
        return var1.inflate(layout.news_fragment, var2, false);
    }

    public void onViewCreated(View var1, @Nullable Bundle var2) {
        this.mPullToRefresh = (PullToRefreshListView) var1;
        this.mNewsListAdapter = new NewsListAdapter(this.getActivity());
        this.mPullToRefresh.setOnItemClickListener(this);
        this.mPullToRefresh.setMode(Mode.PULL_FROM_START);
        this.mPullToRefresh.setScrollingWhileRefreshingEnabled(true);
        this.mPullToRefresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            public void onPullDownToRefresh(PullToRefreshBase<ListView> var1) {
                String var2 = DateUtils.formatDateTime(NewsFragment.this.getActivity(), System.currentTimeMillis(), 524305);
                var1.setLastUpdatedLabel(var2);
                NewsFragment.this.loadNewsList(0);
            }

            public void onPullUpToRefresh(PullToRefreshBase<ListView> var1) {
            }
        });
        this.mTvNewsRefreshNote = (TextView) LayoutInflater.from(this.getActivity()).inflate(layout.news_refresh_note, (ViewGroup) null);
        this.mTvNewsRefreshNote.setBackgroundColor(getResources().getColor(R.color.app_theme));
        ((ListView) this.mPullToRefresh.getRefreshableView()).addHeaderView(this.mTvNewsRefreshNote, (Object) null, false);
        this.mTvNewsRefreshNote.setHeight(0);
        this.mAnimHeight = (int) this.getActivity().getResources().getDimension(dimen.y70);
        ((ListView) this.mPullToRefresh.getRefreshableView()).addFooterView(this.mHeadView);
        this.mHeadView.setVisibility(View.GONE);
        this.mPullToRefresh.setOnScrollListener(new OnScrollListener() {
            boolean a = false;

            public void onScrollStateChanged(AbsListView var1, int var2) {
            }

            public void onScroll(AbsListView var1, int var2, int var3, int var4) {
                this.a = var4 > 0 && var2 + var3 >= var4 - 1;
                if (this.a && NewsFragment.this.mFootView == null && NewsFragment.this.mNewsListAdapter.getCount() > 0 && NewsFragment.this.mHeadView.getVisibility() == View.GONE) {
                    NewsFragment.this.mHeadView.setVisibility(View.VISIBLE);
                    ImageView var5 = (ImageView) NewsFragment.this.mHeadView.findViewById(id.iv_loading);
                    ((AnimationDrawable) var5.getDrawable()).start();
                    NewsFragment.this.loadNewsList(1);
                }

                int var8 = var1.getLastVisiblePosition();
                if (NewsFragment.this.mNewsListAdapter.getCount() > 0 && var3 > 0) {
                    NewsInfo var6 = (NewsInfo) ((ListView) NewsFragment.this.mPullToRefresh.getRefreshableView()).getItemAtPosition(var2);
                    if (var6 != null && var6.isAd()) {
                        var6.onExposure(NewsFragment.this.mChannelId, 1);
                    }

                    NewsInfo var7 = (NewsInfo) ((ListView) NewsFragment.this.mPullToRefresh.getRefreshableView()).getItemAtPosition(var8);
                    if (var7 != null && var7.isAd()) {
                        var7.onExposure(NewsFragment.this.mChannelId, 1);
                    }
                }

            }
        });
        this.mPullToRefresh.setAdapter(this.mNewsListAdapter);
        List var3 = NewsStore.getNewsList(this.mChannelId);
        this.mNewsListAdapter.addNewsList(var3);
        if (var3.size() > 0) {
            LogCat.d(this.getClass(), "load news info from cache");
        }

        var1.postDelayed(new Runnable() {
            public void run() {
                NewsFragment.this.loadNewsList(0);
            }
        }, 500L);
        ((ListView) this.mPullToRefresh.getRefreshableView()).setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View var1, MotionEvent var2) {
                if (var2.getAction() == 0) {
                    NewsFragment.this.k = var2.getX();
                    NewsFragment.this.l = var2.getY();
                } else if (var2.getAction() == 1) {
                    NewsFragment.this.m = var2.getX();
                    NewsFragment.this.n = var2.getY();
                }

                return false;
            }
        });
    }

    private void a() {
        if (this.mFootView == null) {
            this.mFootView = LayoutInflater.from(this.getActivity()).inflate(layout.news_item_no_more_bar, (ViewGroup) null);
            this.mFootView.setOnClickListener(new OnClickListener() {
                public void onClick(View var1) {
                    NewsFragment.this.loadNewsList(0);
                }
            });
            ((ListView) this.mPullToRefresh.getRefreshableView()).removeFooterView(this.mHeadView);
            ((ListView) this.mPullToRefresh.getRefreshableView()).addFooterView(this.mFootView, (Object) null, true);
        } else {
            this.mHeadView.setVisibility(View.GONE);
        }

    }

    public void onDestroyView() {
        this.b();
        if (this.mHeadView != null) {
            ((ListView) this.mPullToRefresh.getRefreshableView()).removeFooterView(this.mHeadView);
            this.mHeadView = null;
        }

        if (this.mFootView != null) {
            ((ListView) this.mPullToRefresh.getRefreshableView()).removeFooterView(this.mFootView);
            this.mFootView = null;
        }

        super.onDestroyView();
    }

    public String getChannelId() {
        return this.mChannelId;
    }

    public String getPageTitle() {
        return this.mPageTitle;
    }

    public void loadNewsList(final int var1) {
        this.b();
        long var2 = -1L;
        if (var1 == 0) {
            if (this.mFootView != null) {
                ((ListView) this.mPullToRefresh.getRefreshableView()).removeFooterView(this.mFootView);
                this.mFootView = null;
            }

            if (this.mHeadView != null) {
                this.mHeadView.setVisibility(View.GONE);
            }
        } else {
            var2 = this.mNewsListAdapter.getItem(this.mNewsListAdapter.getCount() - 1).publishTime;
        }

        HttpCreator.readNewsList2(this.getContext(), this.mChannelId, var1 == 0, 10, var2, new HttpCallback<NEWS_LIST>() {
            @Override
            public void onCompleted(NEWS_LIST var1x, HttpError httpError) {
                if (var1x != null && var1x.data != null) {
                    LogUtil.i("news_list", "var1x.data = " + ((List) var1x.data));
                    List<NewsInfo> data = var1x.data;
                    for (NewsInfo newsInfo : data) {
                        LogUtil.i("newsInfos", "engineType = " + newsInfo.engineType);
                        LogUtil.i("newsInfos", "srcDisplay =" + newsInfo.srcDisplay);
                        LogUtil.i("newsInfos", "layoutType =" + newsInfo.layoutType);
                        LogUtil.i("newsInfos", "engine =" + newsInfo.engine);
                        LogUtil.i("newsInfos", "isRead =" + newsInfo.isRead);
                        LogUtil.i("newsInfos", "coverImage.url =" + newsInfo.coverImage.url);
                        LogUtil.i("newsInfos", "subTitle =" + newsInfo.subTitle);
                        LogUtil.i("newsInfos", "adId =" + newsInfo.adId);
                        LogUtil.i("newsInfos", "adType =" + newsInfo.adType);
                        LogUtil.i("newsInfos", "adName =" + newsInfo.adName);
                        LogUtil.i("newsInfos", "adTraceId =" + newsInfo.adTraceId);
                        LogUtil.i("newsInfos", "sspId =" + newsInfo.sspId);
                        LogUtil.i("newsInfos", "sspAdId =" + newsInfo.sspAdId);
                        LogUtil.i("newsInfos", "imageUrl =" + newsInfo.imageUrl);
                        LogUtil.i("newsInfos", "source =" + newsInfo.source);
                        LogUtil.i("newsInfos", "iconUrl =" + newsInfo.iconUrl);
                        LogUtil.i("newsInfos", "detailUrl =" + newsInfo.detailUrl);
                        LogUtil.i("newsInfos", "sourceImageUrl =" + newsInfo.sourceImageUrl);
                        LogUtil.i("newsInfos", "clickCallbackUrls =" + newsInfo.clickCallbackUrls);
                        LogUtil.i("newsInfos", "exposureCallbackUrls =" + newsInfo.exposureCallbackUrls);
                        LogUtil.i("newsInfos", "isRefreshBar =" + newsInfo.isRefreshBar());
                        LogUtil.i("newsInfos", "isGif =" + newsInfo.isGif());
                        LogUtil.i("newsInfos", "isBigPic =" + newsInfo.isBigPic());
                        LogUtil.i("newsInfos", "isJoke =" + newsInfo.isJoke());
                        LogUtil.i("newsInfos", "isTextPic =" + newsInfo.isTextPic());
                        LogUtil.i("newsInfos", "isThreePic =" + newsInfo.isThreePic());
                        LogUtil.i("newsInfos", "isAd =" + newsInfo.isAd());
                        LogUtil.i("newsInfos", "isNews =" + newsInfo.isNews());
                        LogUtil.i("newsInfos", "isOrigin =" + newsInfo.isOrigin());
                    }
                    int var5 = ((List) var1x.data).size();
                    if (var1 == 0) {
                        if (NewsFragment.this.isAdded()) {
                            String var6;
                            if (var5 > 0) {
                                var6 = NewsFragment.this.getString(string.feed_load_count, new Object[]{Integer.valueOf(var5)});
                            } else {
                                var6 = NewsFragment.this.getString(string.feed_no_more_load);
                            }
                            NewsFragment.this.a(var6);
                        }

                        NewsFragment.this.mNewsListAdapter.addNewsList((List) var1x.data);

                        NewsStore.addNewsList(NewsFragment.this.getContext(), NewsFragment.this.mChannelId, (List) var1x.data);
                        ((ListView) NewsFragment.this.mPullToRefresh.getRefreshableView()).setSelection(0);
                    } else {
                        if (var5 > 0) {
                            NewsFragment.this.mNewsListAdapter.appendDataList((List) var1x.data);
                            NewsFragment.this.mNewsListAdapter.notifyDataSetChanged();
                        } else {
                            NewsFragment.this.a();
                        }

                        if (NewsFragment.this.mHeadView != null) {
                            NewsFragment.this.mHeadView.setVisibility(View.GONE);
                        }
                    }
                } else if (var1 == 1) {
                    final ImageView var3 = (ImageView) NewsFragment.this.mHeadView.findViewById(id.iv_loading);
                    ((AnimationDrawable) var3.getDrawable()).stop();
                    var3.setVisibility(View.GONE);
                    final TextView var4 = (TextView) NewsFragment.this.mHeadView.findViewById(id.tv_loading);
                    var4.setText("点击加载更多内容");
                    NewsFragment.this.mHeadView.setOnClickListener(new OnClickListener() {
                        public void onClick(View var1x) {
                            NewsFragment.this.mHeadView.setVisibility(View.GONE);
                            var3.setVisibility(View.VISIBLE);
                            var4.setText("加载中");
                            NewsFragment.this.loadNewsList(1);
                        }
                    });
                } else {
//                    if (NewsFragment.this.isAdded()) {
//                        switch (null.a[httpError.ordinal()]) {
//                            case 1:
//                                NewsFragment.this.a(NewsFragment.this.getString(string.feed_timeout_tips));
//                                break;
//                            case 2:
//                            case 3:
//                                if (!NetworkMonitor.isAvailable()) {
//                                    NewsFragment.this.a(NewsFragment.this.getString(string.feed_neterror_tips));
//                                }
//                                break;
//                            case 4:
//                                NewsFragment.this.a(NewsFragment.this.getString(string.feed_timeout_tips));
//                        }
//                    }

                    ((ListView) NewsFragment.this.mPullToRefresh.getRefreshableView()).setSelection(0);
                }

                NewsFragment.this.mPullToRefresh.onRefreshComplete();
            }
        });
    }

    private void a(String var1) {
        if (this.getUserVisibleHint() && this.mTvNewsRefreshNote != null) {
            this.mTvNewsRefreshNote.setText(var1);
            this.mTvNewsRefreshNote.setHeight(this.mAnimHeight);
            if (this.objectAnimator != null) {
                this.objectAnimator.cancel();
                this.objectAnimator = null;
            }

            this.objectAnimator = ObjectAnimator.ofInt(this.mTvNewsRefreshNote, "height", new int[]{this.mAnimHeight, 0});
            this.objectAnimator.setDuration(500L);
            this.objectAnimator.setStartDelay(1500L);
            this.objectAnimator.start();
        }

    }

    private void b() {
        if (this.mHttpMessage != null) {
            this.mHttpMessage.cancel();
            this.mHttpMessage = null;
        }

    }

    public void onItemClick(AdapterView<?> var1, View var2, int var3, long var4) {
        if (NewsSettings.sDetailsActivity == null) {
            throw new IllegalArgumentException("Please set NewsSettings.sDetailsActivity as expected details activity");
        } else {
            HeaderViewListAdapter var6 = (HeaderViewListAdapter) var1.getAdapter();
            NewsListAdapter var7 = (NewsListAdapter) var6.getWrappedAdapter();
            LogCat.d(this.getClass(), "click item position: " + var3);
            NewsInfo var8 = var7.getItem(this.mTvNewsRefreshNote == null ? var3 : var3 - 1);
            if (var8.isRefreshBar()) {
                this.loadNewsList(0);
            } else if (var8.isAd()) {
                var8.onClick(this.mChannelId, 1, this.k, this.l - var2.getY(), this.m, this.n - var2.getY(), var2.getWidth(), var2.getHeight());
                this.startActivity((new Intent(this.getActivity(), VenusWebEmbedActivity.class)).putExtra("url", var8.detailUrl));
            } else {
                Intent var9 = NewsDetailsLoader.initNewsExtra(new Intent(this.getContext(), NewsSettings.sDetailsActivity), this.mChannelId, var8);
                this.startActivity(var9);
                var8.isRead = true;
                var7.setNewsIsRead(var2.getTag());
                NewsStore.updateNewsInfo(var8);
            }
        }
    }
}
