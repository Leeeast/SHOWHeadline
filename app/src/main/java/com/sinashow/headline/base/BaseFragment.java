package com.sinashow.headline.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    private Toast mToast;
    private Unbinder mUnBinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        initData(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        setView();
    }

    /**
     * 获取数据 实例化Fragment需要传传参数，放在Bundle中，使用Bundle时候，需要做为空校验
     * step_1
     */
    public abstract void initData(@Nullable Bundle bundle);

    /**
     * 设置跟布局
     * step_2
     *
     * @return 资源布局ID
     */
    public abstract int getLayoutID();

    /**
     * 设置页面
     * step_3
     */
    public abstract void setView();

    /**
     * 显示Toast
     *
     * @param toastStr
     */
    public void showToast(String toastStr) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), toastStr, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(toastStr);
        }
        mToast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) mUnBinder.unbind();
    }
}
