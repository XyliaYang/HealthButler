package com.mialab.healthbutler.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.MusicActivity;
import com.mialab.healthbutler.receiver.NotificationClickReceiver;
import com.mialab.healthbutler.utils.Player;

public class MusicService extends Service {

    private static final int NOTIFICATION_REQUEST_CODE = 10;
    private MusicReceiver mMusicReceiver;
    private int currentPosition;
    private Player mMusicPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicPlayer = new Player();
        //注册MusicReceiver
        mMusicReceiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mialab.sleepangel.musicreceiver");
        registerReceiver(mMusicReceiver, filter);

        mMusicPlayer.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent("com.mialab.sleepangel.nextmusicreceiver");
                intent.putExtra("position", currentPosition);
                sendOrderedBroadcast(intent, null);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMusicReceiver);
    }

    public class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            int control = intent.getIntExtra("control", -1);
            currentPosition = intent.getIntExtra("position", -2);

            Notification.Builder localBuilder = new Notification.Builder(MusicService.this);

            Intent clickIntent = new Intent(MusicService.this, NotificationClickReceiver.class); //点击通知之后要发送的广播
            PendingIntent contentIntent = PendingIntent.getBroadcast(MusicService.this, NOTIFICATION_REQUEST_CODE, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            localBuilder.setContentIntent(contentIntent);
            localBuilder.setAutoCancel(false);
            localBuilder.setSmallIcon(R.drawable.icon_app);
            localBuilder.setTicker("播放音乐");
            localBuilder.setContentTitle("睡前音乐");

            final String musicURL = intent.getStringExtra("musicURL");
            if (control == MusicActivity.MUSIC_PLAY_URL) {
                Log.i("musicStatus:", "MUSIC_PLAY_URL");
                //前台service
                startForeground(1, localBuilder.getNotification());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mMusicPlayer.playUrl(musicURL);
                    }
                }).start();
            } else if (control == MusicActivity.MUSIC_PAUSE) {
                Log.i("musicStatus:", "MUSIC_PAUSE");
                mMusicPlayer.pause();
                //停止前台service
                stopForeground(true);
            } else if (control == MusicActivity.MUSIC_STOP) {
                Log.i("musicStatus:", "MUSIC_STOP");
                //停止前台service
                stopForeground(true);
                mMusicPlayer.stop();
            } else if (control == MusicActivity.MUSIC_PLAY) {
                Log.i("musicStatus:", "MUSIC_PLAY");
                //前台service
                startForeground(1, localBuilder.getNotification());
                mMusicPlayer.play();
            }
        }
    }
}
