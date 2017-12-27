package com.sinashow.headline.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.caishi.venus.api.bean.news.ImageInfo;
import com.caishi.venus.details.DetailsFragment;
import com.caishi.venus.details.NewsDetailsLoader;
import com.sinashow.headline.R;
import com.sinashow.headline.constant.MFSDKSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangp on 2016/4/15.
 */
public class InfoDetailActivity extends Activity implements NewsDetailsLoader.WebViewListener {
    public static final String KEY_URL_LIST = "urlList";
    public static final String KEY_SELECTED_INDEX = "selectedIndex";
    private DetailsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        int flags = (MFSDKSetting.sIsShowOriginContent ? NewsDetailsLoader.FLAG_ORIGIN_WEB : 0)
                | (MFSDKSetting.sIsShowShareBar ? NewsDetailsLoader.FLAG_SHARE_BAR : 0);
        Intent intent = getIntent();

        mFragment = DetailsFragment.newInstance(intent, flags);
        getFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
        mFragment.setWebViewListener(this);
        findViewById(R.id.iv_main_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 详情页加载成功时回调
     */
    @Override
    public void onWebViewPageFinished() {
        Log.d("InfoDetailActivity", "onWebViewPageFinished");
    }

    /**
     * 详情页加载失败时回调
     * 错误码： NewsDetailsLoader.ERROR 表示加载失败，NewsDetailsLoader.NO_EXIST 表示新闻已下架
     */
    @Override
    public void onWebViewPageFailure(int errorCode) {
        if (errorCode == NewsDetailsLoader.ERROR) {
            Toast.makeText(this, "加载失败请重试", Toast.LENGTH_SHORT).show();
        } else if (errorCode == NewsDetailsLoader.NO_EXIST) {
            Toast.makeText(this, "内容被偷走了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * WebView中点击图片时的回调
     *
     * @param position 点击的图片在图片列表中所在位置
     * @param images   图片信息集合
     */
    @Override
    public void onWebImageClick(int position, List<ImageInfo> images) {
        ArrayList<String> urlList = new ArrayList<String>();
        for (int i = 0; i < images.size(); i++) {
            urlList.add(images.get(i).url);
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(KEY_URL_LIST, urlList);
        intent.putExtra(KEY_SELECTED_INDEX, position);
        intent.setClass(InfoDetailActivity.this, ImageShowActivity.class);
        this.startActivity(intent);
    }

    /**
     * WebView中查看原文Button点击时回调
     *
     * @param url 原文url
     */
    @Override
    public void onWebOrigin(String url) {
        Intent intent = new Intent(this, WebEmbedActivity.class);
        intent.putExtra(MFSDKSetting.KEY_URL, url);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * WebView中分享按钮点击时回调
     *
     * @param i 微信朋友圈0，QQ空间1，微博2
     */
    @Override
    public void onWebShareClick(int i) {

        Toast.makeText(this, "点击分享", Toast.LENGTH_SHORT).show();
    }
}
