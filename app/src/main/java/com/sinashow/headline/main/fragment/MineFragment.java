package com.sinashow.headline.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/27.
 */

public class MineFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = MineFragment.class.getSimpleName();

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

    }
}
