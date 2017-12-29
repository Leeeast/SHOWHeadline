package com.sinashow.headline.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sinashow.headline.R;
import com.sinashow.headline.base.BaseActivity;
import com.sinashow.headline.constant.Constant;
import com.sinashow.headline.main.fragment.FeedFragment;
import com.sinashow.headline.main.fragment.MineFragment;
import com.sinashow.headline.main.fragment.TopicFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rdogp_main_tab)
    RadioGroup rdogpMainTab;

    protected FragmentManager fragmentManager;
    protected FragmentTransaction ft;

    private String[] fragmentTags = new String[]{FeedFragment.FRAGMENT_TAG,
            TopicFragment.FRAGMENT_TAG, MineFragment.FRAGMENT_TAG};
    //再次点击退出使用
    private long touchTime = 0;


    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate() {
        fragmentManager = getSupportFragmentManager();
        setView();
    }

    private void setView() {
        showFragment(FeedFragment.FRAGMENT_TAG);
        rdogpMainTab.setOnCheckedChangeListener(this);
    }

    /**
     * 显示Fragment
     *
     * @param tag
     */
    protected void showFragment(String tag) {
        ft = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragment = addFragment(tag);
        }
        if (!fragment.isAdded()) {
            ft.add(R.id.fly_content, fragment, tag);
        } else {
            ft.show(fragment);
            // ft.addToBackStack(tag);
        }
        hideOtherFragment(tag, ft);
        ft.commitAllowingStateLoss();
    }

    /**
     * 添加Fragment
     *
     * @param tag
     * @return
     */
    private Fragment addFragment(String tag) {
        Fragment fragment = null;
        if (tag.equals(FeedFragment.FRAGMENT_TAG)) {
            fragment = FeedFragment.newInstance();
        } else if (tag.equals(TopicFragment.FRAGMENT_TAG)) {
            fragment = TopicFragment.newInstance();
        } else if (tag.equals(MineFragment.FRAGMENT_TAG)) {
            fragment = MineFragment.newInstance();
        }
        return fragment;
    }

    /**
     * 隐藏别的Fragment
     *
     * @param tag
     * @param ft
     */
    protected void hideOtherFragment(String tag, FragmentTransaction ft) {
        for (String t : fragmentTags) {
            if (!t.equals(tag)) {
                Fragment otherFragment = fragmentManager.findFragmentByTag(t);
                if (otherFragment != null) {
                    ft.hide(otherFragment);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) > Constant.EXIT_APP_TIME_OFFSET) {
            //让Toast的显示时间和等待时间相同
            Toast.makeText(this, getString(R.string.exit_app_tips) + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            this.finish();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rdobtn_main:
                showFragment(FeedFragment.FRAGMENT_TAG);
                break;
            case R.id.rdobtn_topic:
                showFragment(TopicFragment.FRAGMENT_TAG);
                break;
            case R.id.rdobtn_mine:
                showFragment(MineFragment.FRAGMENT_TAG);
                break;
        }
    }
}
