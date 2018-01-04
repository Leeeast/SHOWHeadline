package com.sinashow.headline.constant;

/**
 * Created by Administrator on 2017/12/27.
 */

public class Constant {
    //主页再次按返回按钮，退出程序，时间间隔
    public static final int EXIT_APP_TIME_OFFSET = 2000;

    private static final String URL_MF_BASE = "http://api.9wuli.com";
    /**
     * 获取频道列表
     * version新闻频道版本号。初始化时传0。储存该接口返回数据的attached字段作为下次调用该接口的version
     */
    public static final String URL_CHANNEL = URL_MF_BASE + "/v1/channel/list?version=%s" +
            "&appId=" + MFSDKSetting.DEFAULT_APP_ID +
            "&appSecret=" + MFSDKSetting.DEFAULT_APP_SECRET;
    /**
     * 请求新闻列表数据信息
     */
    public static final String URL_MESSAGE_LIST = URL_MF_BASE + "/v3/message/list?pageSize=%d" +
            "&appId=" + MFSDKSetting.DEFAULT_APP_ID +
            "&appSecret=" + MFSDKSetting.DEFAULT_APP_SECRET +
            "&channelId=%s";
    /**
     * 上滑请求新闻列表数据信息
     */
    public static final String URL_MESSAGE_LIST_UP = URL_MF_BASE + "/v3/message/list?pageSize=%d" +
            "&slipType=UP" +
            "&appId=" + MFSDKSetting.DEFAULT_APP_ID +
            "&appSecret=" + MFSDKSetting.DEFAULT_APP_SECRET +
            "&channelId=%s" +
            "&since=%s";
    /**
     * 下拉请求新闻列表数据信息
     */
    public static final String URL_MESSAGE_LIST_DWON = URL_MF_BASE + "/v3/message/list?pageSize=%d" +
            "&slipType=DOWN" +
            "&appId=" + MFSDKSetting.DEFAULT_APP_ID +
            "&appSecret=" + MFSDKSetting.DEFAULT_APP_SECRET +
            "&channelId=%s" +
            "&userId=%s";

    /**
     * 帖子链接
     */
    public static final String URL_TOPIC = URL_MF_BASE + "/sdk/hsdt1/app/share/topic.html?id=2abefae6757ab4fbc312637d93d59360";
}
