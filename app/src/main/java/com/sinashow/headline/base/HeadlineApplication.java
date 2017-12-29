package com.sinashow.headline.base;

import android.app.Application;
import android.content.Context;

import com.caishi.venus.ui.VenusApi;
import com.caishi.venus.ui.config.NewsSettings;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.sinashow.headline.constant.MFSDKSetting;
import com.sinashow.headline.main.InfoDetailActivity;

/**
 * Created by Administrator on 2017/12/27.
 */

public class HeadlineApplication extends Application {
    public static HeadlineApplication showApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        showApplication = this;
        init();
    }
    /**
     * 获取应用ApplicationContext
     *
     * @return
     */
    public static Context getAppContext() {
        return showApplication;
    }

    /**
     * 初始化相关配置
     */
    private void init() {
        Fresco.initialize(this);
        // 设置新闻详情页Activity
        NewsSettings.sDetailsActivity = InfoDetailActivity.class;
        // 修改新闻详情页缓存的过期时间，默认5天
        NewsSettings.DETAILS_OVERDUE_TIME = 1000 * 60 * 60 * 24 * 3;
        // 修改每个频道缓存数目，默认50
        NewsSettings.MAX_NEWS_PER_CHANNEL = 100;

        VenusApi.initialize(this, MFSDKSetting.DEFAULT_APP_ID, MFSDKSetting.DEFAULT_APP_SECRET);
    }
}
