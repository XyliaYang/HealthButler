package com.mialab.healthbutler.utils;

import android.content.Context;

/**
 * 缓存工具类
 *
 * @author Wesly
 */
public class CacheUtils {

    //设置缓存
    public static void setCache(Context ctx, String url, String json) {
        PrefUtils.setString(ctx, url, json);
    }

    //获取缓存
    public static String getCache(Context ctx, String url, String defValue) {
        return PrefUtils.getString(ctx, url, defValue);
    }
}
