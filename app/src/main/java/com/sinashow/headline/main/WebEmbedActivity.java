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
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sinashow.headline.R;
import com.sinashow.headline.constant.MFSDKSetting;
import com.sinashow.headline.utils.DeviceUtils;
import com.sinashow.headline.utils.WebViewSettings;
import com.sinashow.headline.utils.statusBar.ImmerseStatusBar;


/**
 * Created by bruceli on 2015/8/7.
 */
public class WebEmbedActivity extends Activity implements View.OnClickListener {


    private WebView mWebView = null;
    private String mUrl = null;
    private ProgressBar mProgressBar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.transparent);
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
                onBackPressed();
            }
        });
        View ivClose = findViewById(R.id.iv_close);
        ivClose.setVisibility(View.VISIBLE);
        ivClose.setOnClickListener(this);
        findViewById(R.id.iv_more).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);

        FrameLayout flyTitleToot = findViewById(R.id.fly_title_root);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) flyTitleToot.getLayoutParams();
        layoutParams.topMargin = DeviceUtils.getStatusBarHeight(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_more:
            case R.id.iv_share:
                Toast.makeText(this, "攻城狮努力开发中...", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
