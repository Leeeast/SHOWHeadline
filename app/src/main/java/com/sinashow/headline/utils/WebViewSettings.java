package com.sinashow.headline.utils;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by bruceli on 2015/8/11.
 */
public class WebViewSettings {

    public static void initSettings(WebView webView, boolean needCache) {

        if (webView == null) {
            return;
        }

        WebSettings settings = webView.getSettings();

        if (needCache) {
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setAppCacheEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            settings.setAppCacheEnabled(false);
            settings.setDomStorageEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setDatabaseEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(false); //打开页面时， 自适应屏幕
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");
    }
}
