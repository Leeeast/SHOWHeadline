package com.sinashow.headline.widget.media;

import java.util.Map;

/**
 * Created by lidongliang on 2017/6/9.
 */

public class VideoModel {
    String url;
    Map<String, String> mapHeadData;

    float speed = 1;

    boolean looping;

    public VideoModel(String url, Map<String, String> mapHeadData, boolean loop, float speed) {
        this.url = url;
        this.mapHeadData = mapHeadData;
        this.looping = loop;
        this.speed = speed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getMapHeadData() {
        return mapHeadData;
    }

    public void setMapHeadData(Map<String, String> mapHeadData) {
        this.mapHeadData = mapHeadData;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
