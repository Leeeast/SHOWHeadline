package com.sinashow.headline.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据解析工具类
 * Created by lidongliang on 2017/10/18.
 */

public class GsonTools {
    public GsonTools() {
    }

    /**
     * @param
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> T parseData(String jsonString, Class<T> cls) throws Exception {
        Gson gson = new Gson();
        T t = gson.fromJson(jsonString, cls);
        return t;
    }

    /**
     * 使用Gson进行解析 List
     *
     * @param
     * @param jsonData
     * @return
     */
    public static <T> List<T> parseDatas(String jsonData, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonArray arry = new JsonParser().parse(jsonData).getAsJsonArray();
        for (JsonElement jsonElement : arry) {
            list.add(gson.fromJson(jsonElement, clazz));
        }
        return list;
    }
}
