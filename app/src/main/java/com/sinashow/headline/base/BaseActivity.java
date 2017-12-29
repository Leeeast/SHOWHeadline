package com.sinashow.headline.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sinashow.headline.R;
import com.sinashow.headline.utils.statusBar.ImmerseStatusBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/27.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();

    private Toast mToast;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmerseStatusBar.setImmerseStatusBar(this, R.color.transparent);
        setContentView(getLayoutID());
        mUnbinder = ButterKnife.bind(this);
        onCreate();
    }

    /**
     * 设置跟布局 step_1
     *
     * @return
     */
    public abstract int getLayoutID();

    /**
     * 走activity onCreate流程 step_2
     */
    public abstract void onCreate();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    /**
     * 显示Toast
     *
     * @param toastStr
     */
    public void showToast(String toastStr) {
        if (mToast == null) {
            mToast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(toastStr);
        }
        mToast.show();
    }

    /**
     * 跳转Activity
     *
     * @param context 上下文
     * @param clazz   目标Activity
     */
    public void invokActivity(@NonNull Context context,
                              @NonNull Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    /**
     * 跳转Activity
     *
     * @param context     上下文
     * @param clazz       目标Activity
     * @param params      Activity之间传参
     * @param requestCode Activity跳转code
     */
    public void invokActivity(@NonNull Context context,
                              @NonNull Class clazz,
                              @Nullable Bundle params, int requestCode) {
        Intent intent = new Intent(context, clazz);
        if (params != null) {
            intent.putExtras(params);
        }
        startActivityForResult(intent, requestCode);
    }

}
