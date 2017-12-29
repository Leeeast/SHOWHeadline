package com.sinashow.headline.main.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caishi.venus.api.bean.news.ChannelInfo;
import com.caishi.venus.api.remote.ChannelsLoader;
import com.caishi.venus.ui.VenusApi;
import com.caishi.venus.ui.config.NewsSettings;
import com.sinashow.headline.R;
import com.sinashow.headline.constant.MFSDKSetting;
import com.sinashow.headline.main.adapter.NewsPagerAdapter;
import com.sinashow.headline.utils.DeviceUtils;
import com.sinashow.headline.widget.DragAdapter;
import com.sinashow.headline.widget.DragGridView;
import com.sinashow.headline.widget.TabPageIndicator;

import java.util.List;

/**
 * Created by mo on 2016/10/17.
 * Powered by yueke
 */

public class FeedFragment extends Fragment implements View.OnClickListener {

    public static final String FRAGMENT_TAG = FeedFragment.class.getSimpleName();

    private ChannelsLoader mChannelsLoader;
    private ViewPager mViewPager;
    private NewsPagerAdapter mPagerAdapter;
    private TabPageIndicator mPageIndicator;
    private ImageView mIvChannelEdit;
    private FrameLayout mFrameChannelEdit;
    private DragGridView mGridView;
    private DragAdapter mDragAdapter;
    private int mLastTabIndex;
    private List<ChannelInfo> mUserChannelList;

    public static FeedFragment newInstance() {
        FeedFragment feedFragment = new FeedFragment();
        return feedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VenusApi.initialize(getContext(), MFSDKSetting.DEFAULT_APP_ID, MFSDKSetting.DEFAULT_APP_SECRET);
        mChannelsLoader = new ChannelsLoader(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        // 设置布局方式
        if (MFSDKSetting.sIsTextImage) {
            NewsSettings.sLayoutType = NewsSettings.LAYOUT_TYPE.LAYOUT_TYPE_TEXT_IMAGE;
        } else {
            NewsSettings.sLayoutType = NewsSettings.LAYOUT_TYPE.LAYOUT_TYPE_IMAGE_TEXT;
        }
        mViewPager = (ViewPager) view.findViewById(R.id.vp);
        mPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        mFrameChannelEdit = (FrameLayout) view.findViewById(R.id.ll_channel_manage);
        mIvChannelEdit = (ImageView) view.findViewById(R.id.channel_edit);
        mGridView = (DragGridView) view.findViewById(R.id.channel_list);
        ImageView ivSearch = view.findViewById(R.id.iv_search);
        ivSearch.setVisibility(View.VISIBLE);
        ivSearch.setOnClickListener(this);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.app_name));

        FrameLayout flyTitleToot = view.findViewById(R.id.fly_title_root);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) flyTitleToot.getLayoutParams();
        layoutParams.topMargin = DeviceUtils.getStatusBarHeight(getContext());

        if (MFSDKSetting.sIsShowChannelManager) {
            mIvChannelEdit.setVisibility(View.VISIBLE);
            mGridView.setDragMode(true);

            //点击进入频道管理
            mIvChannelEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //如果频道管理是显示状态
                    if (mFrameChannelEdit.getVisibility() == View.VISIBLE) {
                        if (mChannelsLoader.updateChannelOrder(getContext(), mUserChannelList)) {
                            updatePageOrder(-1);
                        }
                    }
                    //最初频道管理的布局mFrameChannelEdit是GONE的
                    showChannelManager(v, mFrameChannelEdit.getVisibility() == View.GONE);
                }
            });

        } else {

            mPageIndicator.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            mIvChannelEdit.setVisibility(View.GONE);
        }
        mPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLastTabIndex != position) {
                    mLastTabIndex = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mGridView.getVisibility() == View.VISIBLE) {

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mChannelsLoader.updateChannelOrder(getContext(), mUserChannelList)) {
                        updatePageOrder(position);
                    } else if (mLastTabIndex != position) {
                        mLastTabIndex = position;
                        mPageIndicator.setCurrentItem(position);
                    }
                    showChannelManager(mIvChannelEdit, false);
                }
            });
        }

        mChannelsLoader.loadChannelList(new ChannelsLoader.DataCallback<List<ChannelInfo>>() {
            @Override
            public void onResult(List<ChannelInfo> channelInfos) {
                mUserChannelList = channelInfos;
                mPagerAdapter = new NewsPagerAdapter(getChildFragmentManager(), channelInfos);
                mViewPager.setAdapter(mPagerAdapter);
                mPageIndicator.setViewPager(mViewPager);
            }
        });

    }

    /**
     * 刷新频道列表
     *
     * @param tabIndex 刷新频道列表并ViewPager跳转到指定tabIndex，传入-1则刷新后不跳转
     */
    private void updatePageOrder(final int tabIndex) {

        mChannelsLoader.loadChannelList(new ChannelsLoader.DataCallback<List<ChannelInfo>>() {
            @Override
            public void onResult(List<ChannelInfo> channels) {
                int index = tabIndex;
                if (index == -1) {
                    // 拿当前显示的频道index去缓存中找
                    int count = channels.size();
                    String activeId = mUserChannelList.get(mViewPager.getCurrentItem()).id;
                    for (int i = 0; i < count; i++) {
                        if (activeId.equals(channels.get(i).id)) {
                            index = i;
                            break;
                        }
                    }
                    if (index == -1) {
                        index = 0;
                    }
                }
                // 更新频道列表
                mUserChannelList.clear();
                mUserChannelList.addAll(channels);
                mViewPager.getAdapter().notifyDataSetChanged();
                mPageIndicator.notifyDataSetChanged();
                // 刷新当前页面
                mViewPager.setCurrentItem(index);
            }
        });

    }

    private void showChannelManager(final View v, final boolean show) {
// mIvChannelEdit不可用
        v.setEnabled(false);

        if (show) {
            mPageIndicator.setVisibility(View.GONE);
            getActivity().findViewById(R.id.channel_man_title).setVisibility(View.VISIBLE);

            if (mGridView.getAdapter() == null) {
                mDragAdapter = new DragAdapter(getContext(), mUserChannelList);
                mGridView.setAdapter(mDragAdapter);
            } else {
                mDragAdapter.setListData(mUserChannelList);
                mDragAdapter.notifyDataSetChanged();
            }
            //点击后，将频道管理的布局mFrameChannelEdit显示
            mFrameChannelEdit.setVisibility(View.VISIBLE);

            mViewPager.setVisibility(View.INVISIBLE);
        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(mFrameChannelEdit, "translationY",
                (show ? -getResources().getDimension(R.dimen.y1730) : 0),
                (show ? 0 : -getResources().getDimension(R.dimen.y1730)));
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(300L);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                v.setEnabled(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!show) {
                    mPageIndicator.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.channel_man_title).setVisibility(View.GONE);
                    mFrameChannelEdit.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                }
                v.setEnabled(true);
            }
        });

        animator.start();

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(getActivity().findViewById(R.id.channel_edit), "rotation",
                (show ? 0 : -180),
                (show ? -180 : 0));
        animator2.setDuration(300L);
        animator2.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChannelsLoader != null) {
            mChannelsLoader.release();
        }
        VenusApi.release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                Toast.makeText(getContext(), "功能开发中", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
