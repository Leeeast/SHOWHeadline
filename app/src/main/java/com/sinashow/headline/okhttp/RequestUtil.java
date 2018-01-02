package com.sinashow.headline.okhttp;

import android.text.TextUtils;
import android.widget.Toast;

import com.sinashow.headline.R;
import com.sinashow.headline.base.HeadlineApplication;
import com.sinashow.headline.okhttp.callback.StringCallback;
import com.sinashow.headline.okhttp.request.RequestCall;
import com.sinashow.headline.utils.LogUtil;
import com.sinashow.headline.utils.ResponseCodeCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

/**
 * 请求封装
 * Created by lidongliang on 2017/10/3.
 */

public class RequestUtil {
    public static final String TAG = "RequestUtil";

    /**
     * 普通回调
     */
    public interface RequestListener {
        void onSuccess(boolean isSuccess, String obj, int code, int id);

        void onFailed(Call call, Exception e, int id);
    }

    /**
     * 列表集合请求回调
     */
    public interface RequestListListener {
        void onSuccess(String response, int id);

        void onFailed(Call call, Exception e, int id);
    }

    /**
     * 请求统一封装
     *
     * @param isPost    是否是post请求
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static RequestCall request(boolean isPost, String url, Map<String, String> params, int requestId, RequestListener listener) {
        if (isPost) {
            return postRequest(url, params, requestId, listener);
        } else {
            return getRequest(url, params, requestId, listener);
        }
    }

    /**
     * 请求统一封装
     *
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static RequestCall request(String url, Map<String, String> params, int requestId, final RequestListener listener) {
        RequestCall build = OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .id(requestId)
                .build();
        build.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-" + id);
                listener.onFailed(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i(TAG, response + ":id = " + id);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        String data = jsonObject.optString("data");
                        int totalCount = jsonObject.optInt("totalCount");
                        if (ResponseCodeCheck.checkResponseCode(code)) {
                            listener.onSuccess(true, data, code, totalCount);
                        } else {
                            listener.onSuccess(false, msg, code, totalCount);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onSuccess(false, "Json data parsing exception", 0, id);
                    }
                }
            }
        });
        return build;
    }


    /**
     * post请求封装
     *
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static RequestCall postRequest(String url, Map<String, String> params, int requestId, final RequestListener listener) {
        try {
            RequestCall build = OkHttpUtils
                    .post()
                    .url(url)
                    .params(params)
                    .id(requestId)
                    .build();
            build.execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e, int id) {
                    listener.onFailed(call, e, id);
                }

                @Override
                public void onResponse(String response, int id) {
                    if (!TextUtils.isEmpty(response)) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            String msg = jsonObject.optString("msg");
                            String data = jsonObject.optString("data");
                            if (ResponseCodeCheck.checkResponseCode(code)) {
                                listener.onSuccess(true, data, code, id);
                            } else {
                                listener.onSuccess(false, msg, code, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onSuccess(false, "Json data parsing exception", 0, id);
                        }
                    } else {

                    }
                }
            });
            return build;
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailed(null, e, 10000);
        }
        return null;
    }

    /**
     * get请求封装
     *
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static RequestCall getRequest(String url, Map<String, String> params, final int requestId, final RequestListener listener) {
        try {
            RequestCall build = OkHttpUtils
                    .get()
                    .url(url)
                    .params(params)
                    .id(requestId)
                    .build();
            build.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtil.e(TAG, e.getMessage() + "-" + id);
                    listener.onFailed(call, e, id);
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtil.i(TAG, response + ":id = " + id);
                    if (!TextUtils.isEmpty(response)) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            String msg = jsonObject.optString("msg");
                            String data = jsonObject.optString("data");
                            if (ResponseCodeCheck.checkResponseCode(code)) {
                                listener.onSuccess(true, data, code, id);
                            } else {
                                listener.onSuccess(false, msg, code, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onSuccess(false, "Json data parsing exception", 0, id);
                        }
                    }
                }
            });
            return build;
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailed(null, e, 1000);
        }
        return null;
    }


    /**
     * 请求统一封装
     *
     * @param isPost    是否是post请求
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static RequestCall requestList(boolean isPost, String url, Map<String, String> params, int requestId, RequestListListener listener) {
        if (isPost) {
            return postRequestList(url, params, requestId, listener);
        } else {
            return getRequestList(url, params, requestId, listener);
        }
    }

    /**
     * post请求封装
     *
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static RequestCall postRequestList(String url, Map<String, String> params, int requestId, final RequestListListener listener) {
        RequestCall build = OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .id(requestId)
                .build();
        build.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                listener.onFailed(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                listener.onSuccess(response, id);
            }
        });
        return build;
    }

    /**
     * get请求封装
     *
     * @param url       请求连接
     * @param params    参数
     * @param requestId 请求ID
     * @param listener  请求回调
     */
    public static RequestCall getRequestList(String url, Map<String, String> params, final int requestId, final RequestListListener listener) {
        RequestCall build = OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .id(requestId)
                .build();
        build.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.getMessage() + "-" + id);
                listener.onFailed(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i(TAG, response + ":id = " + id);
                listener.onSuccess(response, id);
            }
        });
        return build;
    }


    public static RequestCall uploadLoadFile(String url, String filePath, final RequestListener listener) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(HeadlineApplication.getAppContext(), HeadlineApplication.getAppContext().getString(R.string.tip_file_path_unexists), Toast.LENGTH_SHORT).show();
                return null;
            }
            RequestCall build = OkHttpUtils
                    .post()
                    .addHeader("Content_Type", "multipart/form-data")
                    .url(url)
                    .addFile("file", "Screenshot_2017-10-08-17-51-24.png", file)
                    .build();
            build.execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    listener.onFailed(call, e, id);
                }

                @Override
                public void onResponse(String response, int id) {
                    if (!TextUtils.isEmpty(response)) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            String msg = jsonObject.optString("msg");
                            String data = jsonObject.optString("data");
                            if (ResponseCodeCheck.checkResponseCode(code)) {
                                listener.onSuccess(true, data, code, id);
                            } else {
                                listener.onSuccess(false, msg, code, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onSuccess(false, "Json data parsing exception", 0, id);
                        }
                    } else {

                    }
                }
            });
            return build;
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailed(null, e, 10000);
        }
        return null;
    }
}
