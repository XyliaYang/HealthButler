package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;
import com.mialab.healthbutler.view.ColorArcProgressBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Wesly186 on 2016/3/12.
 */
public class SignActivity extends Activity implements SensorEventListener {

    ImageButton mibBack;
    SensorManager mSensorManager;
    Vibrator mVibrator;
    int mShakeTimes = 0;
    TextView mtvShakeTimes;
    private Button btn_sure;
    private Button btn_cancel;
    private ImageButton ib_share;
    private ImageView iv_qq;
    private ImageView iv_wechat;
    // private ImageView iv_zone;
    private ColorArcProgressBar capb_signtime;
    private TextView tv_signNum;
    private int flag = 0;
    private TextView tv_step;
    private TextView tv_rank;
    private long TIME_INTERVAL = 500;
    private int signTime = 0;
    private int stepNum = 0;
    private int timeStamp = 0;
    private int place;  //当前排名
    private boolean showrank = false;
    private boolean isSignedToday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.mainactivity));

        initView();

        initdata();
        mibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        mtvShakeTimes = (TextView) findViewById(R.id.tv_shake_times);
        mibBack = (ImageButton) findViewById(R.id.ib_back);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        tv_step = (TextView) findViewById(R.id.tv_step);
        //tv_signNum= (TextView) findViewById(R.id.tv_signNum);
        ib_share = (ImageButton) findViewById(R.id.ib_share);
        iv_qq = (ImageView) findViewById(R.id.iv_qq);
        iv_wechat = (ImageView) findViewById(R.id.iv_wechat);
        //iv_zone = (ImageView) findViewById(R.id.iv_zone);
        Calendar calendarNow = Calendar.getInstance();
        capb_signtime = (ColorArcProgressBar) findViewById(R.id.capb_signtime);
        checkIsSignedToday();
        if ((calendarNow.get(Calendar.HOUR_OF_DAY) >= 6 && calendarNow.get(Calendar.HOUR_OF_DAY) < 7) || (calendarNow.get(Calendar.MINUTE) <= 20 && calendarNow.get(Calendar.HOUR_OF_DAY) == 7)) {
            if (!isSignedToday && timeStamp == 0) {
                Toast.makeText(this, "现在仍是签到时间，快起来追逐未来吧!", Toast.LENGTH_SHORT).show();
                showShakeDialog();
            }
        } else {
            if (!isSignedToday) {
                Toast.makeText(this, "今天已经错过签到了，明天早点起床吧！加油..", Toast.LENGTH_SHORT).show();
                timeStamp = 1;
                mtvShakeTimes.setText("明天继续努力吧!");
            }
            //showShakeDialog();
        }
    }

    private void checkIsSignedToday() {
        Date date = new Date();
        Date lastDate;
        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarLast = Calendar.getInstance();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String signTime = PrefUtils.getString(this, "SIGNTIME", "");
        if (TextUtils.isEmpty(signTime)) {
            isSignedToday = false;
        } else {
            try {
                lastDate = format.parse(signTime);
                calendarLast.setTime(lastDate);
                if (calendarNow.get(Calendar.YEAR) > calendarLast.get(Calendar.YEAR) || calendarNow.get(Calendar.MONTH) > calendarLast.get(Calendar.MONTH) || calendarNow.get(Calendar.DAY_OF_MONTH) > calendarLast.get(Calendar.DAY_OF_MONTH)) {
                    isSignedToday = false;
                } else {
                    Toast.makeText(this, "今天已经完成签到了!", Toast.LENGTH_SHORT).show();
                    mtvShakeTimes.setText("已完成今日起签到！");
                    isSignedToday = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSignRank() {
        View view = View.inflate(this, R.layout.signrank_dialog, null);
        tv_rank = (TextView) view.findViewById(R.id.tv_rank);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final ColorArcProgressBar rank = (ColorArcProgressBar) view.findViewById(R.id.capb_signrank);
        // rank.setMargin(0);
        builder.create().show();
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        String UUID = PrefUtils.getString(this, GlobalContants.TOKEN, "");
        params.addBodyParameter("UUID", UUID);
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalContants.GET_RANK, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                place = Integer.parseInt(responseInfo.result);
                //Log.e("zs", place + "");
                rank.setCurrentValues(signTime);
                tv_rank.setText("第" + place + "名");
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });

    }

    private void showShakeDialog() {
        Toast.makeText(this, "摇25下即可点亮我要签到按钮!", Toast.LENGTH_SHORT).show();
        View view = View.inflate(this, R.layout.shank_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_sure.setClickable(false);
        btn_sure.setText("剩余" + (25 - mShakeTimes) + "次");
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtils utils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("UUID", PrefUtils.getString(SignActivity.this, GlobalContants.TOKEN, ""));
                utils.send(HttpRequest.HttpMethod.POST, GlobalContants.SIGNUP, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        PrefUtils.setString(SignActivity.this, "SIGNTIME", format.format(new Date()));
                        dialog.cancel();
                        // mtvShakeTimes.setText("签到成功!");
                        //flag = 1;
                        Toast.makeText(SignActivity.this, "点击右上角分享到QQ空间可查看您的签到排名！", Toast.LENGTH_SHORT).show();
                        initdata();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mtvShakeTimes.setText("签到失败！");
                        Toast.makeText(SignActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = 0;
        ShareSDK.stopSDK();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        if (showrank) {
            showSignRank();
            showrank = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mShakeTimes > 25 && flag == 0) {
            mVibrator.vibrate(500);
            mSensorManager.unregisterListener(this);
            btn_sure.setTextColor(Color.GRAY);
            btn_sure.setClickable(true);
            btn_sure.setText("我要签到");
            btn_sure.setBackgroundColor(Color.BLACK);
        } else {
            // Log.e("zs",stepNum+"");
            int sensorType = event.sensor.getType();
            float[] values = event.values;
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15 || Math
                        .abs(values[2]) > 15)) {
                    mShakeTimes++;
                    btn_sure.setBackgroundColor(getResources().getColor(R.color.grey));
                    btn_sure.setTextColor(Color.BLACK);
                    btn_sure.setClickable(false);
                    btn_sure.setText("剩余" + (25 - mShakeTimes) + "次");
                    // mtvShakeTimes.setText("离签到成功还剩" + (25 - mShakeTimes) + "次");
                }

            }
        }


    }

    public void initdata() {
        ShareSDK.initSDK(this);       //初始化shareSDK
        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        iv_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToQQ();
            }
        });
        /*iv_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharToQZone();
            }
        });
        iv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWechat();
            }
        });*/
        initSignTime();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("zs", "返回成功");
                showrank = true;
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("zs", "取消分享");
            }
        });
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("星空之下");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://139.129.35.71:8080/SleepAngel/UI/index.jsp");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我已经在WakeUp累计早起" + signTime + "天。" + "快来加入wakeUp俱乐部，健康生活每一天!");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        //oks.setUrl("http://139.129.35.71:8080/SleepAngel/Version/SleepAngelV1.0.4.apk");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setImageUrl("http://139.129.35.71:8080/SleepAngel/icon.png");
        oks.setComment("终于搞定了");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        // oks.setSiteUrl("http://139.129.35.71:8080/SleepAngel/Version/SleepAngelV1.0.4.apk");

        // 启动分享GUI
        oks.show(this);

    }

    /*private void sharToQZone() {
        Platform qzone = ShareSDK.getPlatform("QZone");
        QZone.ShareParams sp2 = new QZone.ShareParams();
        sp2.text = "我已经在WakeUp累计早起" + signTime + "天。" + "快来加入wakeUp俱乐部，健康生活每一天!";
        sp2.setText("我已经在WakeUp累计早起" + signTime + "天。" + "快来加入wakeUp俱乐部，健康生活每一天!");
        sp2.comment="我已经在WakeUp累计早起" + signTime + "天。" + "快来加入wakeUp俱乐部，健康生活每一天!";
        sp2.imageUrl = "http://139.129.35.71:8080/SleepAngel/icon.png";
        qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                showrank=true;
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        qzone.share(sp2);
    }*/

    private void shareToWechat() {
        Platform wechat = ShareSDK.getPlatform("Wechat");
        Wechat.ShareParams sp2 = new Wechat.ShareParams();
        sp2.text = "我已经在WakeUp累计早起" + signTime + "天。" + "快来加入wakeUp俱乐部，健康生活每一天!";
        sp2.imageUrl = "http://139.129.35.71:8080/SleepAngel/UI/index.jsp";
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("zs", "返回成功");
                showrank = true;
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("zs", "取消分享");
            }
        });
        wechat.share(sp2);

    }

    private void shareToQQ() {
        Platform QQ = ShareSDK.getPlatform("QQ");
        QQ.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                showrank = true;
                Log.e("zs", "返回成功");
                // Toast.makeText(getApplicationContext(), "haha", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("zs", "返回失败");
                Toast.makeText(getApplicationContext(), "hee", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                // Toast.makeText(getApplicationContext(), "haha", Toast.LENGTH_SHORT).show();
                Log.e("zs", "取消分享");
            }
        });
        cn.sharesdk.tencent.qq.QQ.ShareParams sp = new cn.sharesdk.tencent.qq.QQ.ShareParams();
        // sp.text = "hahah";
        sp.imageUrl = "http://139.129.35.71:8080/SleepAngel/Version/SleepAngelV1.0.4.apk";
        QQ.share(sp);
    }


    private void initSignTime() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        String UUID = PrefUtils.getString(this, GlobalContants.TOKEN, "");
        params.addBodyParameter("UUID", UUID);
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalContants.GET_SIGN_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.equals("pleaseLoginFirst")) {
                    Toast.makeText(SignActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
//                    tv_signNum.setText("");
                    capb_signtime.setCurrentValues(0);
                } else {
                    //Log.e("zs",responseInfo.result);
                    int Num = Integer.parseInt(responseInfo.result);
                    signTime = Num;
                    capb_signtime.setCurrentValues(Num);
                    //                   tv_signNum.setText( Num + "");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(SignActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
