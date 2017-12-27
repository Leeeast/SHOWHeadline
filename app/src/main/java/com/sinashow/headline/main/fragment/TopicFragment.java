package com.sinashow.headline.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseFragment;

/**
 * Created by Administrator on 2017/12/27.
 */

public class TopicFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = TopicFragment.class.getSimpleName();

    public TopicFragment() {
    }

    public static TopicFragment newInstance() {
        TopicFragment mainFragment = new TopicFragment();
        return mainFragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_topic;
    }

    @Override
    public void setView() {

    }

}
