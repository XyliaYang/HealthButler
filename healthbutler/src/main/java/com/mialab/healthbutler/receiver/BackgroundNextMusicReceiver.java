package com.mialab.healthbutler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mialab.healthbutler.domain.MusicData;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by Wesly186 on 2016/3/13.
 */
public class BackgroundNextMusicReceiver extends BroadcastReceiver {

    private ArrayList<MusicData.Music> mMusicList;
    private MusicData mMusicData;

    @Override
    public void onReceive(Context context, Intent intent) {

        //获取缓存
        String cache = CacheUtils.getCache(context, GlobalContants.MUSIC_URL, "");
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        int size = mMusicList.size();
        int position = intent.getIntExtra("position", -1);
        if (position < size - 1) {
            position++;
        } else {
            position = 0;
        }
        Log.i("backreceiverposition+1:", position + "");
        //更改按钮状态
        PrefUtils.setString(context, "togleOn", position + "");
        //播放音乐
        Intent i = new Intent("com.mialab.sleepangel.musicreceiver");
        //i.putExtra("control", HomeDetail.MUSIC_PLAY_URL);
        i.putExtra("musicURL", mMusicList.get(position).getmMusicURL());
        i.putExtra("position", position);
        context.sendBroadcast(i);
        //更新currentplaying
        PrefUtils.setString(context, "currentplaying", position + "");

        abortBroadcast();
    }

    private void parseData(String cache) {
        Gson gson = new Gson();
        mMusicData = gson.fromJson(cache, MusicData.class);
        mMusicList = mMusicData.getmMusicList();
    }

}
