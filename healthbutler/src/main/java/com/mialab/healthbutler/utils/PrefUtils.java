package com.mialab.healthbutler.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * sharepreferences工具类
 *
 * @author Wesly
 */
public class PrefUtils {

    private static final String PREF_NAME = "config";


    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static void setBoolean(Context ctx, String key, boolean value) {
        Editor editor = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void setString(Context ctx, String key, String value) {
        Editor editor = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value).commit();
    }

}
