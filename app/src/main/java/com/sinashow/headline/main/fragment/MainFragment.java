package com.sinashow.headline.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseFragment;
import com.sinashow.headline.widget.DragGridView;
import com.sinashow.headline.widget.TabPageIndicator;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener {
    public static final String FRAGMENT_TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.vp)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    TabPageIndicator mPageIndicator;
    @BindView(R.id.ll_channel_manage)
    FrameLayout mFrameChannelEdit;
    @BindView(R.id.channel_edit)
    ImageView mIvChannelEdit;
    @BindView(R.id.channel_list)
    DragGridView mGridView;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;


    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    public void setView() {
        tvTitle.setText(getString(R.string.tab_main));
        mIvSearch.setVisibility(View.VISIBLE);
        mIvSearch.setOnClickListener(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
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
