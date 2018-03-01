package com.mialab.healthbutler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mialab.healthbutler.activity.MusicActivity;
import com.mialab.healthbutler.app.ActivityCollector;

/**
 * 响应notification点击事件的BroadcastReceiver
 * <p/>
 * Created by Wesly186 on 2016/3/17.
 */
public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ActivityCollector.finishAll();
        Intent newIntent = new Intent(context, MusicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}
