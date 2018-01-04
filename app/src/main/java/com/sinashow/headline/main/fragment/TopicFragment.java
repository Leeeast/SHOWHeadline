package com.sinashow.headline.main.fragment;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseFragment;
import com.sinashow.headline.constant.Constant;
import com.sinashow.headline.constant.MFSDKSetting;
import com.sinashow.headline.main.WebEmbedActivity;
import com.sinashow.headline.utils.DeviceUtils;
import com.sinashow.headline.utils.UtilNet;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class TopicFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = TopicFragment.class.getSimpleName();
        @BindView(R.id.tv_title)
        TextView mTvTitle;
    @BindView(R.id.fly_title_root)
    FrameLayout mFlyTitleRoot;
    @BindView(R.id.wb_embed_details)
    WebView mWbTopic;
    @BindView(R.id.pbar_detail_progressbar)
    ProgressBar mProgressBar;

    public TopicFragment() {
    }

    public static TopicFragment newInstance() {
        TopicFragment mainFragment = new TopicFragment();
        return mainFragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_topic;
    }

    @Override
    public void setView() {
        mTvTitle.setText(getString(R.string.tab_topic));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mFlyTitleRoot.getLayoutParams();
        layoutParams.topMargin = DeviceUtils.getStatusBarHeight(getContext());

        initWebView();
    }

    private void initWebView() {
        // TODO Auto-generated method stub
        WebSettings mWebSettings = mWbTopic.getSettings();
        // 设置 缓存模式
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setAppCacheEnabled(false);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDisplayZoomControls(false);// 去掉缩放按钮
        mWebSettings.setBuiltInZoomControls(true);// 允许缩放
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        // 开启 DOM storage API 功能
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setJavaScriptEnabled(true);
        // 支持通过js打开新的窗口
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 去掉滚动条
        mWbTopic.setVerticalScrollBarEnabled(false);
        mWbTopic.setHorizontalScrollBarEnabled(false);
        mWbTopic.setKeepScreenOn(true);

        mWbTopic.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });

        mWbTopic.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Intent intent = new Intent(getActivity(), WebEmbedActivity.class);
                intent.putExtra(MFSDKSetting.KEY_URL, view.getUrl());
                startActivity(intent);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        // 当前有网络
        if (UtilNet.isNetAvailable(getActivity())) {
            mWbTopic.loadUrl(Constant.URL_TOPIC);
        } else {// 加载错误页面
            mWbTopic.loadUrl("file:///android_asset/weberror.html");
        }
    }

}
