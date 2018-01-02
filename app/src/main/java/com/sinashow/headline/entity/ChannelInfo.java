package com.sinashow.headline.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/2.
 */


public class ChannelInfo implements Serializable, Comparable<com.caishi.venus.api.bean.news.ChannelInfo> {
    public String id;
    public String name;
    public int sort;
    public int enableStatus;
    public int deleteStatus;
    public long createTime;
    public long modifyTime;

    public ChannelInfo() {
    }

    public int compareTo(com.caishi.venus.api.bean.news.ChannelInfo var1) {
        return this.sort - var1.sort;
    }
}