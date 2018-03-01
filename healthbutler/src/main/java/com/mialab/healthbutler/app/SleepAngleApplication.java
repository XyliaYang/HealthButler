package com.mialab.healthbutler.app;

import android.app.Application;

import cn.smssdk.SMSSDK;

/**
 * 重写Application，初始化环境
 *
 * @author Wesly
 */
public class SleepAngleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "11cbfda5402b2", "e3e09556f1e8e31637e6c6f532be217a",false);
    }
}
