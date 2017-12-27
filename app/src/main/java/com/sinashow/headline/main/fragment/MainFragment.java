package com.sinashow.headline.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class MainFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView tvTitle;

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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
    }
}
