package com.sinashow.headline.okhttp.builder;


import com.sinashow.headline.okhttp.OkHttpUtils;
import com.sinashow.headline.okhttp.request.OtherRequest;
import com.sinashow.headline.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
