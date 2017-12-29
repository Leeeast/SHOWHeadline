package com.sinashow.headline.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseFragment;
import com.sinashow.headline.utils.DeviceUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class MineFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = MineFragment.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.fly_title_root)
    FrameLayout mFlyTitleRoot;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment mainFragment = new MineFragment();
        return mainFragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    public void setView() {
        mTvTitle.setText(getString(R.string.tab_mine));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mFlyTitleRoot.getLayoutParams();
        layoutParams.topMargin = DeviceUtils.getStatusBarHeight(getContext());
    }
}
