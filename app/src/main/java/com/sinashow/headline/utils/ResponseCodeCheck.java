package com.sinashow.headline.utils;

import android.content.Intent;
import android.widget.Toast;

import com.sinashow.headline.R;
import com.sinashow.headline.base.HeadlineApplication;


/**
 * Created by lidongliang on 2017/10/3.
 */

public class ResponseCodeCheck {

    //网络请求状态码
    public static final int CODE_10000 = 10000;//sucess
    public static final int CODE_10001 = 10001;//服务器异常
    public static final int CODE_20002 = 20002;//对不起,您无权使用该服务
    private static Toast toast;

    /**
     * 检测状态码
     *
     * @param code
     * @return
     */
    public static boolean checkResponseCode(int code) {
        switch (code) {
            case CODE_10000:
                return true;
            case CODE_10001:
            case CODE_20002:
                return false;
            default:
                return false;
        }
    }

    public static void showErrorMsg(int code) {
        switch (code) {
            case CODE_10000:
                //showToast(HeadlineApplication.getAppContext().getString(R.string.response_code_200));
                break;
            case CODE_10001:
                showToast(HeadlineApplication.getAppContext().getString(R.string.response_code_10001));
                break;
            case CODE_20002:
                showToast(HeadlineApplication.getAppContext().getString(R.string.response_code_20002));
                break;
            default:
                showToast(HeadlineApplication.getAppContext().getString(R.string.response_code_other));
                break;
        }
    }

    private static void showToast(String toastStr) {
        if (toast == null) {
            toast = Toast.makeText(HeadlineApplication.getAppContext(), toastStr, Toast.LENGTH_SHORT);
        } else {
            toast.setText(toastStr);
        }
        toast.show();
    }
}
