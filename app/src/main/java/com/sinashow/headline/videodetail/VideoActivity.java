package com.sinashow.headline.videodetail;

import android.widget.FrameLayout;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseActivity;
import com.sinashow.headline.utils.DeviceUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/3.
 */

public class VideoActivity extends BaseActivity {
    @BindView(R.id.fly_title_root)
    FrameLayout mFlyTitleRoot;

    @Override
    public int getLayoutID() {
        return R.layout.activity_video;
    }

    @Override
    public void onCreate() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mFlyTitleRoot.getLayoutParams();
        layoutParams.topMargin = DeviceUtils.getStatusBarHeight(this);
    }
}
