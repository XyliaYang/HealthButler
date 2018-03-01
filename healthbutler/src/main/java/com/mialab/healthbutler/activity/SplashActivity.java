package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.service.MusicService;
import com.mialab.healthbutler.utils.PrefUtils;

/**
 * 闪屏页
 *
 * @author Wesly
 */
public class SplashActivity extends Activity {

    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);

        startAnim();

        //设置应用主动检查更新
        PrefUtils.setBoolean(SplashActivity.this, GlobalContants.CHECK_UPDATE_IN_MAINACTIVITY, true);
        //设置community选择新闻还是广场
        PrefUtils.setString(SplashActivity.this, GlobalContants.COMMUNITY_CURRENT_SELECTED, 0 + "");

        if (!isServiceRunning()) {
            //初始化播放状态
            PrefUtils.setString(SplashActivity.this, "togleOn", "-1");
            PrefUtils.setString(SplashActivity.this, "currentplaying", "-1");
            // 启动后台MusicService
            Intent intentMusic = new Intent(SplashActivity.this, MusicService.class);
            startService(intentMusic);
        }

    }

    //判断service是否在运行
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.mialab.sleepangel.service.MusicService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startAnim() {
        AnimationSet set = new AnimationSet(false);

        ScaleAnimation scale = new ScaleAnimation((float) 0.95, 1, (float) 0.95, 1, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(500);
        scale.setFillAfter(true);

        AlphaAnimation alpha = new AlphaAnimation((float) 0.5, 1);
        alpha.setDuration(1000);
        alpha.setFillAfter(true);

        set.addAnimation(scale);
        set.addAnimation(alpha);

        set.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpToNextPage();
                finish();
            }

        });

        rl_root.startAnimation(set);

    }

    private void jumpToNextPage() {
        boolean userGuide = PrefUtils.getBoolean(this, GlobalContants.USER_GUIDE_PREF, false);
        Intent intent = new Intent();
        if (!userGuide) {
            intent.setClass(SplashActivity.this, UserGuideActivity.class);
        } else {
            intent.setClass(SplashActivity.this, MainActivity.class);
        }

        startActivity(intent);
    }
}
