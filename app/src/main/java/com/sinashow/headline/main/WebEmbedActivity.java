package com.sinashow.headline.main;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sinashow.headline.R;
import com.sinashow.headline.constant.MFSDKSetting;
import com.sinashow.headline.utils.WebViewSettings;
import com.sinashow.headline.utils.statusBar.ImmerseStatusBar;


/**
 * Created by bruceli on 2015/8/7.
 */
public class WebEmbedActivity extends Activity {


    private WebView mWebView = null;
    private String mUrl = null;
    private ProgressBar mProgressBar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.status_bar);
        setContentView(R.layout.activity_web_embed);
        mWebView = (WebView) findViewById(R.id.wb_embed_details);
        WebViewSettings.initSettings(mWebView, false);
//        mWebView.getSettings().setBlockNetworkImage(false);
//        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.setBackgroundResource(android.R.color.transparent);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(WebEmbedActivity.this, R.anim.disappear_alpha_anim);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                mProgressBar.startAnimation(animation);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mProgressBar.setVisibility(View.GONE);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress <= 100) {
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mUrl = getIntent().getStringExtra(MFSDKSetting.KEY_URL);
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
        mProgressBar = (ProgressBar) findViewById(R.id.pbar_detail_progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        findViewById(R.id.iv_main_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        ((ViewGroup) mWebView.getParent()).removeView(mWebView);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
