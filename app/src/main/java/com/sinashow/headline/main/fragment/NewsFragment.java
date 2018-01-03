//
// Source code recreated from mHttpMessage .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sinashow.headline.main.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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

import com.caishi.venus.VenusWebEmbedActivity;
import com.caishi.venus.api.bean.http.HttpError;
import com.caishi.venus.api.bean.news.NewsInfo;
import com.caishi.venus.api.bean.resp.Responses;
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
import com.sinashow.headline.constant.Constant;
import com.sinashow.headline.main.WebEmbedActivity;
import com.sinashow.headline.main.adapter.NewsListAdapter;
import com.sinashow.headline.okhttp.RequestUtil;
import com.sinashow.headline.utils.GsonTools;
import com.sinashow.headline.utils.LogUtil;

import java.util.List;

import okhttp3.Call;

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
    private float mActionDownX;
    private float mActionDownY;
    private float mActionUpX;
    private float mActionUpY;

    public NewsFragment() {
    }

    public static NewsFragment create(String channelId, String pageTitle) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channelId", channelId);
        bundle.putString("pageTitle", pageTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        Bundle bundle = this.getArguments();
        if (bundle == null) return;
        this.mChannelId = bundle.getString("channelId");
        this.mPageTitle = bundle.getString("pageTitle");
    }

    public View onCreateView(LayoutInflater var1, @Nullable ViewGroup viewGroup, Bundle bundle) {
        this.mHeadView = var1.inflate(R.layout.scene_item_load, (ViewGroup) null, false);
        ImageView ivLoading = (ImageView) this.mHeadView.findViewById(R.id.iv_loading);
        ((AnimationDrawable) ivLoading.getDrawable()).start();
        return var1.inflate(R.layout.news_fragment, viewGroup, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onViewCreated(View view, @Nullable Bundle bundle) {
        this.mPullToRefresh = (PullToRefreshListView) view;
        this.mNewsListAdapter = new NewsListAdapter(this.getActivity());
        this.mPullToRefresh.setOnItemClickListener(this);
        this.mPullToRefresh.setMode(Mode.PULL_FROM_START);
        this.mPullToRefresh.setScrollingWhileRefreshingEnabled(true);
        this.mPullToRefresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefresh) {
                String dataTime = DateUtils.formatDateTime(NewsFragment.this.getActivity(), System.currentTimeMillis(), 524305);
                pullToRefresh.setLastUpdatedLabel(dataTime);
                NewsFragment.this.loadNewsList(0);
            }

            public void onPullUpToRefresh(PullToRefreshBase<ListView> var1) {
            }
        });
        this.mTvNewsRefreshNote = (TextView) LayoutInflater.from(this.getActivity()).inflate(R.layout.news_refresh_note, (ViewGroup) null);
        ((ListView) this.mPullToRefresh.getRefreshableView()).addHeaderView(this.mTvNewsRefreshNote, (Object) null, false);
        this.mTvNewsRefreshNote.setHeight(0);
        this.mAnimHeight = (int) this.getActivity().getResources().getDimension(R.dimen.DIMEN_17PX);
        ((ListView) this.mPullToRefresh.getRefreshableView()).addFooterView(this.mHeadView);
        this.mHeadView.setVisibility(View.GONE);
        this.mPullToRefresh.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        this.mPullToRefresh.setOnScrollListener(new OnScrollListener() {
            boolean a = false;

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int var4) {
                this.a = var4 > 0 && firstVisibleItem + visibleItemCount >= var4 - 1;
                if (this.a && NewsFragment.this.mFootView == null && NewsFragment.this.mNewsListAdapter.getCount() > 0 && NewsFragment.this.mHeadView.getVisibility() == View.GONE) {
                    NewsFragment.this.mHeadView.setVisibility(View.VISIBLE);
                    ImageView var5 = (ImageView) NewsFragment.this.mHeadView.findViewById(R.id.iv_loading);
                    ((AnimationDrawable) var5.getDrawable()).start();
                    NewsFragment.this.loadNewsList(1);
                }

                int var8 = view.getLastVisiblePosition();
                if (NewsFragment.this.mNewsListAdapter.getCount() > 0 && visibleItemCount > 0) {
                    NewsInfo var6 = (NewsInfo) ((ListView) NewsFragment.this.mPullToRefresh.getRefreshableView()).getItemAtPosition(firstVisibleItem);
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
        List newsList = NewsStore.getNewsList(this.mChannelId);
        this.mNewsListAdapter.addNewsList(newsList);
        if (newsList.size() > 0) {
            LogCat.d(this.getClass(), "load news info from cache");
        }

        view.postDelayed(new Runnable() {
            public void run() {
                NewsFragment.this.loadNewsList(0);
            }
        }, 500L);
        ((ListView) this.mPullToRefresh.getRefreshableView()).setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View var1, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    NewsFragment.this.mActionDownX = event.getX();
                    NewsFragment.this.mActionDownY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    NewsFragment.this.mActionUpX = event.getX();
                    NewsFragment.this.mActionUpY = event.getY();
                }
                return false;
            }
        });
    }

    private void addFootView() {
        if (this.mFootView == null) {
            this.mFootView = LayoutInflater.from(this.getActivity()).inflate(R.layout.news_item_no_more_bar, (ViewGroup) null);
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
        this.cancelRequest();
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

    public void loadNewsList(final int refresh) {
        this.cancelRequest();
        long since = -1L;
        String url;
        if (refresh == 0) {
            if (this.mFootView != null) {
                ((ListView) this.mPullToRefresh.getRefreshableView()).removeFooterView(this.mFootView);
                this.mFootView = null;
            }
            if (this.mHeadView != null) {
                this.mHeadView.setVisibility(View.GONE);
            }
            url = String.format(Constant.URL_MESSAGE_LIST_DWON, 10, this.mChannelId, "");
        } else {
            since = this.mNewsListAdapter.getItem(this.mNewsListAdapter.getCount() - 1).publishTime;
            url = String.format(Constant.URL_MESSAGE_LIST_UP, 10, this.mChannelId, String.valueOf(since));
        }
//        RequestUtil.request(false, url, null, 10, new RequestUtil.RequestListener() {
//            @Override
//            public void onSuccess(boolean isSuccess, String obj, int code, int id) {
//                NewsFragment.this.mPullToRefresh.onRefreshComplete();
//                if (isSuccess) {
//                    try {
//                        LogUtil.i("news_list", obj);
//                        List<NewsInfo> newsInfos = GsonTools.parseDatas(obj, NewsInfo.class);
//                        if (newsInfos != null) {
//                            int size = newsInfos.size();
//                            if (refresh == 0) {
//                                if (NewsFragment.this.isAdded()) {
//                                    String loadResult;
//                                    if (size > 0) {
//                                        loadResult = NewsFragment.this.getString(R.string.feed_load_count, new Object[]{Integer.valueOf(size)});
//                                    } else {
//                                        loadResult = NewsFragment.this.getString(R.string.feed_no_more_load);
//                                    }
//                                    NewsFragment.this.showLoadResult(loadResult);
//                                }
//
//                                NewsFragment.this.mNewsListAdapter.addNewsList(newsInfos);
//
//                                NewsStore.addNewsList(NewsFragment.this.getContext(), NewsFragment.this.mChannelId, newsInfos);
//                                ((ListView) NewsFragment.this.mPullToRefresh.getRefreshableView()).setSelection(0);
//                            } else {
//                                if (size > 0) {
//                                    NewsFragment.this.mNewsListAdapter.appendDataList(newsInfos);
//                                    NewsFragment.this.mNewsListAdapter.notifyDataSetChanged();
//                                } else {
//                                    NewsFragment.this.addFootView();
//                                }
//
//                                if (NewsFragment.this.mHeadView != null) {
//                                    NewsFragment.this.mHeadView.setVisibility(View.GONE);
//                                }
//                            }
//                        } else if (refresh == 1) {
//                            final ImageView ivLoading = (ImageView) NewsFragment.this.mHeadView.findViewById(R.id.iv_loading);
//                            ((AnimationDrawable) ivLoading.getDrawable()).stop();
//                            ivLoading.setVisibility(View.GONE);
//                            final TextView tvLoading = (TextView) NewsFragment.this.mHeadView.findViewById(R.id.tv_loading);
//                            tvLoading.setText("点击加载更多内容");
//                            NewsFragment.this.mHeadView.setOnClickListener(new OnClickListener() {
//                                public void onClick(View var1x) {
//                                    NewsFragment.this.mHeadView.setVisibility(View.GONE);
//                                    ivLoading.setVisibility(View.VISIBLE);
//                                    tvLoading.setText("加载中");
//                                    NewsFragment.this.loadNewsList(1);
//                                }
//                            });
//                        } else {
//
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    if (NewsFragment.this.isAdded()) {
//                        switch (code) {
//                            case 10001:
//                            case 20002:
//                                NewsFragment.this.showLoadResult(NewsFragment.this.getString(R.string.feed_timeout_tips));
//                                break;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(Call call, Exception e, int id) {
//                call.cancel();
//                NewsFragment.this.mPullToRefresh.onRefreshComplete();
//            }
//        });

        HttpCreator.readNewsList2(this.getContext(), this.mChannelId, refresh == 0, 10, since, new HttpCallback<Responses.NEWS_LIST>() {
            @Override
            public void onCompleted(Responses.NEWS_LIST var1x, HttpError httpError) {
                if (var1x != null && var1x.data != null) {
                    LogUtil.i("news_list", "var1x.data = " + ((List) var1x.data));
                    List<NewsInfo> data = var1x.data;
                    int size = ((List) var1x.data).size();
                    if (refresh == 0) {
                        if (NewsFragment.this.isAdded()) {
                            String loadResult;
                            if (size > 0) {
                                loadResult = NewsFragment.this.getString(R.string.feed_load_count, new Object[]{Integer.valueOf(size)});
                            } else {
                                loadResult = NewsFragment.this.getString(R.string.feed_no_more_load);
                            }
                            NewsFragment.this.showLoadResult(loadResult);
                        }

                        NewsFragment.this.mNewsListAdapter.addNewsList((List) var1x.data);

                        NewsStore.addNewsList(NewsFragment.this.getContext(), NewsFragment.this.mChannelId, (List) var1x.data);
                        ((ListView) NewsFragment.this.mPullToRefresh.getRefreshableView()).setSelection(0);
                    } else {
                        if (size > 0) {
                            NewsFragment.this.mNewsListAdapter.appendDataList((List) var1x.data);
                            NewsFragment.this.mNewsListAdapter.notifyDataSetChanged();
                        } else {
                            NewsFragment.this.addFootView();
                        }

                        if (NewsFragment.this.mHeadView != null) {
                            NewsFragment.this.mHeadView.setVisibility(View.GONE);
                        }
                    }
                } else if (refresh == 1) {
                    final ImageView var3 = (ImageView) NewsFragment.this.mHeadView.findViewById(R.id.iv_loading);
                    ((AnimationDrawable) var3.getDrawable()).stop();
                    var3.setVisibility(View.GONE);
                    final TextView var4 = (TextView) NewsFragment.this.mHeadView.findViewById(R.id.tv_loading);
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

    private void showLoadResult(String lable) {
        if (this.getUserVisibleHint() && this.mTvNewsRefreshNote != null) {
            this.mTvNewsRefreshNote.setText(lable);
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

    private void cancelRequest() {
        if (this.mHttpMessage != null) {
            this.mHttpMessage.cancel();
            this.mHttpMessage = null;
        }
    }

    @Override
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
                var8.onClick(this.mChannelId, 1, this.mActionDownX, this.mActionDownY - var2.getY(), this.mActionUpX, this.mActionUpY - var2.getY(), var2.getWidth(), var2.getHeight());
                this.startActivity((new Intent(this.getActivity(), VenusWebEmbedActivity.class)).putExtra("url", var8.detailUrl));
            } else {
//                Intent var9 = NewsDetailsLoader.initNewsExtra(new Intent(this.getContext(), NewsSettings.sDetailsActivity), this.mChannelId, var8);
//                this.startActivity(var9);
                var8.isRead = true;
                var7.setNewsIsRead(var2.getTag());
//                NewsStore.updateNewsInfo(var8);

                var8.onClick(this.mChannelId, 1, this.mActionDownX, this.mActionDownY - var2.getY(), this.mActionUpX, this.mActionUpY - var2.getY(), var2.getWidth(), var2.getHeight());
                this.startActivity((new Intent(this.getActivity(), WebEmbedActivity.class)).putExtra("url", var8.shareLink));
            }
        }
    }
}
