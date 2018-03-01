package com.mialab.healthbutler.impl.homedetail;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.HistoryStepShowActivitys;
import com.mialab.healthbutler.activity.LoginActivity;
import com.mialab.healthbutler.activity.MainActivity;
import com.mialab.healthbutler.activity.ShowMapActivity;
import com.mialab.healthbutler.activity.SignActivity;
import com.mialab.healthbutler.activity.WeatherActivity;
import com.mialab.healthbutler.db.DBHelper;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.service.StepCounterService;
import com.mialab.healthbutler.utils.EveryDaySportRecords;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.StepDetector;
import com.mialab.healthbutler.view.CircleProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XiangWei on 2016/3/24.
 */
public class HomeDetail implements View.OnClickListener {

    public View mRootView;
    private MainActivity mainActivity;
    private FrameLayout mainframe;
    private boolean mIsShowBack = true;
    private int progressLastnum = 0;
    // ProgressBar环形进度条
    private CircleProgressBar mAbProgressBar;
    // 最大步数设置为5000
    private int max = 5000;
    private int progress = 0;
    private TextView tv_step_numberText, maxText;
    // 距离时间和热量
    private TextView tv_distance, tv_caloria, tv_time;

    private RelativeLayout rl_weather, rl_left_menu;
    // 分享，设置，去运动
    private ImageView img_share;
    private Button img_setting;
    private Button btn_gosport;
    private DrawerLayout drawerLayout;

    private TextView tv_name;

    public Boolean isBack = true;
    private SharedPreferences sp;

    private boolean isRun = false;

    private int noWalkTimes = 0;
    private int WalkTimes = 0;
    private int step_length = 0; // 步长
    private int weight = 0; // 体重
    public int total_step = 0; // 走的总步数

    public long timer = 0;// 运动时间
    private long startTimer = 0;// 开始时间

    private long tempTime = 0;

    public Double distance = 0.0;// 路程：米
    public Double calories = 0.0;// 热量：卡路里
    private Double velocity = 0.0;// 速度：米每秒

    // 环形进度条每次增长的数值
    private int num = 0;
    private int LastNum = 0;

    private Thread thread; // 定义线程对象

    // 上次按退出的时间
    private long mexit = 0;
    // 天气
    String weather;
    String temperature;
    String pm25;
    String airquality;
    ImageView img_weather;
    TextView tv_temperature;
    TextView tv_airquality;
    RelativeLayout rl_caloria, rl_distance, rl_time;
    // 食物
    private int foodimgList[] = {R.drawable.hamburger, R.drawable.pizza,
            R.drawable.chickenleg, R.drawable.crips, R.drawable.beautiful_rice,
            R.drawable.apple, R.drawable.coconut};
    private int foodcaloria[] = {430, 200, 400, 300, 125, 83, 231};
    private int food_index = 0;
    private TextView tv_food;
    private ImageView img_fs;
    //建筑
    private int structureList[] = {R.drawable.gaiden, R.drawable.pekingta, R.drawable.sh, R.drawable.jm};
    private int structureDistance[] = {8000, 600, 468, 2737};
    private int structure_index = 0;
    //存储数据
    private DBHelper dbHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg); // 此处可以更新UI

            countDistance(); // 调用距离方法，看一下走了多远

            if (timer != 0 && distance != 0.0) {

                // 体重、距离
                // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
                calories = weight * distance * 0.001 * 1.036;
                // 速度velocity
                velocity = distance * 1000 / timer;

//				//距离
//				Double distances=distance/structureDistance[structure_index];
//				String Distance=distances.toString().substring(0, 4);
//				tv_food.setText("大约走过了"+ Distance + "个 ~ ");
//
//				//食物
//				Double food=calories/foodcaloria[food_index];
//				String foods=food.toString().substring(0, 4);
//				tv_food.setText("大约消耗了"+ foods + "个 ~ ");

            } else {
                calories = 0.0;
                velocity = 0.0;
            }

            countStep(); // 调用步数方法

            tv_step_numberText.setText(total_step + "");// 显示当前步数

            tv_distance.setText(formatDouble(distance / 1000));// 显示路程
            tv_caloria.setText(formatDouble(calories));// 显示卡路里
            // tv_velocity.setText(formatDouble(velocity));// 显示速度
            //
            tv_time.setText(getFormatTime(timer));// 显示当前运行时间
            startAnimator(StepDetector.CURRENT_SETP);
            // progressBar1.setProgress(StepDetector.firstStep);
            setDate();
        }
    };


    public HomeDetail(Activity aty) {
        mainActivity = (MainActivity) aty;
        initView();
        initData();
    }

    private void initView() {
        mRootView = View.inflate(mainActivity, R.layout.detail_home, null);
        // 天气
        new LoadWeatherAsyncTask().execute("");
        img_weather = (ImageView) mRootView.findViewById(R.id.weather_icon);
        tv_temperature = (TextView) mRootView.findViewById(R.id.temperature);
        tv_airquality = (TextView) mRootView.findViewById(R.id.air_pollution);
        //热量区域
        rl_caloria = (RelativeLayout) mRootView.findViewById(R.id.rl_caloria);
        rl_caloria.setOnClickListener(this);
        //距离区域
        rl_distance = (RelativeLayout) mRootView.findViewById(R.id.rl_distance);
        rl_distance.setOnClickListener(this);
        //计时区域
        rl_time = (RelativeLayout) mRootView.findViewById(R.id.rl_time);
        rl_time.setOnClickListener(this);
    }

    public void initData() {
/**
 * 获取步数的数据
 */
        sp = mainActivity.getSharedPreferences("step", mainActivity.MODE_PRIVATE);
        StepDetector.CURRENT_SETP = total_step = sp.getInt("stepCount", 0);
        //+ StepDetector.CURRENT_SETPINBACK;
        StepDetector.CURRENT_SETPINBACK = 0;

        initProgressBar();

        tv_distance = (TextView) mRootView.findViewById(R.id.tv_distance);
        tv_caloria = (TextView) mRootView.findViewById(R.id.tv_caloria);
        tv_time = (TextView) mRootView.findViewById(R.id.tv_time);
        rl_weather = (RelativeLayout) mRootView.findViewById(R.id.rl_weather);
        img_share = (ImageView) mRootView.findViewById(R.id.btn_share);
        btn_gosport = (Button) mRootView.findViewById(R.id.btn_sport);
        img_setting = (Button) mRootView.findViewById(R.id.btn_left_menu);
        rl_weather.setOnClickListener(this);
        img_share.setOnClickListener(this);
        btn_gosport.setOnClickListener(this);
        img_setting.setOnClickListener(this);

        tv_name = (TextView) mRootView.findViewById(R.id.tv_name);
        tv_name.setOnClickListener(this);

        // 中间食物
        tv_food = (TextView) mRootView.findViewById(R.id.tv_food_structrue);
        img_fs = (ImageView) mRootView.findViewById(R.id.img_food_structrue_time);


        if (thread == null) {
            thread = new Thread() {// 子线程用于监听当前步数的变化
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    super.run();
                    int temp = 0;
                    while (true) {
                        try {
                            // 200毫秒一通知
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (StepCounterService.FLAG) {
                            Message msg = new Message();
                            if (temp != StepDetector.CURRENT_SETP) {
                                temp = StepDetector.CURRENT_SETP;
                            }
                            if (startTimer != System.currentTimeMillis()) {
                                timer = tempTime + System.currentTimeMillis()
                                        - startTimer;
                            }
                            handler.sendMessage(msg);// 通知主线程
                        }
                    }
                }
            };
            thread.start();
        }

        handler.removeCallbacks(thread);
        isBack = true;

        // 初始化控件
        init();

        /**
         * 计步服务
         */
        Intent service = new Intent(mainActivity, StepCounterService.class);
        mainActivity.startService(service);
        startTimer = System.currentTimeMillis();
        tempTime = timer;
    }

    /*
   * progressBar的初始化
   */
    public void initProgressBar() {
        // ProgressBar进度控制
        mAbProgressBar = (CircleProgressBar) mRootView.findViewById(R.id.circleProgressBar);
        tv_step_numberText = (TextView) mRootView.findViewById(R.id.numberText);
        tv_step_numberText.setOnClickListener(this);
        maxText = (TextView) mRootView.findViewById(R.id.maxText);
        tv_step_numberText.setText(total_step + "");
        maxText.setText("今日目标:" + String.valueOf(max));
        mAbProgressBar.setMax(max);
        mAbProgressBar.setProgress(total_step);
        mAbProgressBar.setOnClickListener(this);
        startAnimator(total_step);
    }

    /*
   * 计步圆圈增长的动画
   */
    public void startAnimator(int progress) {
        num = 360 / 50 * progress / 100;
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mAbProgressBar,
                "progress", LastNum, num);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setDuration(200);
        objectAnimator.start();
        LastNum = num;
    }

    /**
     * 实际的步数
     */
    private void countStep() {
        if (!isBack) {
            if (total_step == StepDetector.CURRENT_SETP) {
                noWalkTimes++;
                if (noWalkTimes >= 20) {
                    ShowMapActivity.isWalking = false;
                    noWalkTimes = 0;
                    WalkTimes = 0;
                }
            } else {
                WalkTimes++;
                if (WalkTimes >= 30) {
                    ShowMapActivity.isWalking = true;
                    WalkTimes = 0;
                    noWalkTimes = 0;
                }

            }
            total_step = StepDetector.CURRENT_SETP;
        } else {
            total_step = StepDetector.CURRENT_SETP;
            StepDetector.CURRENT_SETP = total_step;
            isBack = false;
        }
    }


    /**
     * 初始化界面
     */
    private void init() {

		/*
         * 步长和体重
		 */
        SharedPreferences sp1;

        sp1 = mainActivity.getSharedPreferences("body_setting", mainActivity.MODE_PRIVATE);

        String gender = sp1.getString("gender", "男");
        int birth_num = sp1.getInt("birthYear", 1990);
        int height_num = sp1.getInt("height", 170);
        int weight_num = sp1.getInt("weight", 60);

        step_length = (int) (height_num * 0.43);
        weight = weight_num;


        countDistance();
        countStep();

        if ((timer += tempTime) != 0 && distance != 0.0) { // tempTime记录运动的总时间，timer记录每次运动时间
            // 体重、距离
            // 跑步热量（kcal）＝体重（kg）×距离（公里）×1.036，换算一下
            calories = weight * distance * 0.001;
            velocity = distance * 1000 / timer;
        } else {
            calories = 0.0;
            velocity = 0.0;
        }
        // 时间 距离 卡路里 步数 日期
        tv_time.setText(getFormatTime(timer + tempTime));
        tv_distance.setText(formatDouble(distance / 1000));
        tv_caloria.setText(formatDouble(calories));
        tv_step_numberText.setText(total_step + "");
        //食物
        String food = formatDouble(calories * 100 / foodimgList[food_index] / 100);
        tv_food.setText("大约消耗了" + food + "个 ~ ");
        img_fs.setImageResource(foodimgList[food_index]);
        setDate();
    }

    /**
     * 设置显示的日期
     */
    private void setDate() {
        /**
         * 获取存储的日期
         */
        sp = mainActivity.getSharedPreferences("step", mainActivity.MODE_PRIVATE);
        int lastDay = sp.getInt("currentDay", -1);
        int lastmonth = sp.getInt("currentmonth", -1);

        Calendar mCalendar = Calendar.getInstance();// 获取当天Calendar对象
        int weekDay = mCalendar.get(Calendar.DAY_OF_WEEK);// 当天的星期
        int month = mCalendar.get(Calendar.MONTH) + 1;// 当前月份
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);// 当前日期
        //不是同一天了
        if (lastDay > 0 && lastDay != day) {

            //将之前的步数存储起来
            //比当前早了几天
            int minusday = 0;
            if (day > lastDay && month == lastmonth) {
                //当月
                minusday = lastDay - day;//负数
            } else if (month - lastmonth == 1) {
                //第二月
                minusday = -(day + 30 - lastDay);
            }

            Date date = new Date();// 取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, minusday);// 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);

            dbHelper = new DBHelper(mainActivity);
            EveryDaySportRecords records = new EveryDaySportRecords();
            records.setDate(dateString);
            records.setCalorie(formatDouble(weight * distance * 0.001 * 1.036));
            records.setDistance(formatDouble(distance));
            records.setStepcount(String.valueOf(StepDetector.CURRENT_SETP));
            dbHelper.insertOneDaySportRecord(records);

            //第二天重置步数和time
            lastDay = day;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("stepCount", 0);
            editor.commit();
            total_step = StepDetector.CURRENT_SETP = 0;
            getFormatTime(0);
            timer = 0;
            tempTime = 0;
            startTimer = System.currentTimeMillis();

        }


        /**
         * 存储日期
         */
        SharedPreferences.Editor editor1 = sp.edit();
        editor1.putInt("currentDay", day);
        editor1.putInt("currentmonth", month);
        editor1.commit();
    }

    /**
     * 计算行走的距离
     */
    private void countDistance() {

        distance = StepDetector.CURRENT_SETP * step_length / 100.0;
    }


    /**
     * 得到一个格式化的时间
     *
     * @param time 时间 毫秒
     * @return 时：分：秒：毫秒
     */
    private String getFormatTime(long time) {
        time = time / 1000;
        long second = time % 60;
        long minute = (time % 3600) / 60;
        long hour = time / 3600;

        // 毫秒秒显示两位
        // String strMillisecond = "" + (millisecond / 10);
        // 秒显示两位
        String strSecond = ("00" + second)
                .substring(("00" + second).length() - 2);
        // 分显示两位
        String strMinute = ("00" + minute)
                .substring(("00" + minute).length() - 2);
        // 时显示两位
        String strHour = ("00" + hour).substring(("00" + hour).length() - 2);

        return strMinute; // + ":" + strSecond;
        // + strMillisecond;
    }

    /**
     * 计算并格式化doubles数值，保留两位有效数字
     *
     * @param doubles
     * @return 返回当前路程
     */
    private String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals(mainActivity.getString(R.string.zero)) ? mainActivity.getString(R.string.double_zero) : distanceStr;
    }

    /*
     * shareSdk
     */
    private void showShare() {
        ShareSDK.initSDK(mainActivity);
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("来自stroll in school分享");// getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("快来下载stroll in school");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(mainActivity.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(mainActivity);
    }

    class LoadWeatherAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = "http://op.juhe.cn/onebox/weather/query?cityname=苏州&key=8c19fc302c947489558d1a4acb0bca8f";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 访问成功
                    String result = EntityUtils.toString(
                            httpResponse.getEntity(), "utf-8");
                    JSONObject jo = new JSONObject(result);
                    weather = jo.getJSONObject("result").getJSONObject("data")
                            .getJSONObject("realtime").getJSONObject("weather")
                            .getString("info");

                    temperature = jo.getJSONObject("result")
                            .getJSONObject("data").getJSONObject("realtime")
                            .getJSONObject("weather").getString("temperature");

                    pm25 = jo.getJSONObject("result").getJSONObject("data")
                            .getJSONObject("pm25").getJSONObject("pm25")
                            .getString("pm25");

                    airquality = jo.getJSONObject("result")
                            .getJSONObject("data").getJSONObject("pm25")
                            .getJSONObject("pm25").getString("quality");
                    System.out.println("天气" + weather + pm25 + airquality);
                    return result;
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                if (weather.contains("雨")) {

                    if (weather.contains("大雨")) {
                        img_weather.setImageResource(R.drawable.heavyrain);
                    } else if (weather.contains("中雨")) {
                        img_weather.setImageResource(R.drawable.heavyrian);
                    } else if (weather.contains("小雨")) {
                        img_weather.setImageResource(R.drawable.raining);
                    } else {
                        img_weather.setImageResource(R.drawable.raining);
                    }

                } else if (weather.contains("阴")) {
                    img_weather.setImageResource(R.drawable.cloudyall);
                } else if (weather.contains("云")) {
                    img_weather.setImageResource(R.drawable.cloudy);
                } else if (weather.contains("晴")) {
                    img_weather.setImageResource(R.drawable.sun);
                }

                tv_temperature.setText(temperature + "°C");
                tv_airquality.setText("PM2.5:" + pm25 + " " + airquality);
            } else {
                Toast.makeText(mainActivity, "天气无法更新请检查网络",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == btn_gosport.getId()) {
            Intent intent = new Intent(mainActivity, ShowMapActivity.class);
            mainActivity.startActivity(intent);
        } else if (v.getId() == img_share.getId()) {
            // shareSDK初始化
            ShareSDK.initSDK(mainActivity);
            // 分享
            showShare();
        } else if (rl_weather.getId() == v.getId()) {
            Intent intent = new Intent(mainActivity, WeatherActivity.class);
            mainActivity.startActivity(intent);
        } else if (img_setting.getId() == v.getId()) {
            String token = PrefUtils.getString(mainActivity, GlobalContants.TOKEN, "");
            if (!TextUtils.isEmpty(token)) {
                Intent intent = new Intent(mainActivity,
                        SignActivity.class);
                mainActivity.startActivity(intent);
            } else {
                Intent intent = new Intent(mainActivity,
                        LoginActivity.class);
                mainActivity.startActivity(intent);
            }

        } else if (tv_step_numberText.getId() == v.getId()) {
            Intent intent = new Intent(mainActivity,
                    HistoryStepShowActivitys.class);
            mainActivity.startActivity(intent);
        } else if (rl_caloria.getId() == v.getId()) {
            food_index = (food_index + 1) % 7;
            //食物
            String foods = "0.0";
            Double food = calories / foodcaloria[food_index];
            if (food > 0)
                foods = food.toString().substring(0, 4);
            tv_food.setText("大约消耗了" + foods + "个 ~ ");
            img_fs.setImageResource(foodimgList[food_index]);
        } else if (rl_distance.getId() == v.getId()) {
            structure_index = (structure_index + 1) % 4;
            //食物
            String Distance = "0.0";
            Double distances = distance / structureDistance[structure_index];
            if (distances > 0)
                Distance = distances.toString().substring(0, 4);
            tv_food.setText("大约走过了" + Distance + "个 ~ ");
            img_fs.setImageResource(structureList[structure_index]);
        } else if (rl_time.getId() == v.getId()) {
            if (Integer.parseInt(tv_time.getText().toString()) < 60) {
                tv_food.setText("今天运动的时间太短了哦 ~ ");
            } else {
                tv_food.setText("要坚持每天运动一小时哦 ~ ");
            }

            img_fs.setImageResource(R.drawable.clock);
        }

    }
}
