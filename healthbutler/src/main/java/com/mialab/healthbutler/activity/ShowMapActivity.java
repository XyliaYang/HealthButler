package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.db.DBHelper;
import com.mialab.healthbutler.domain.SportRecords;
import com.mialab.healthbutler.impl.HomePage;
import com.mialab.healthbutler.impl.homedetail.HomeDetail;
import com.mialab.healthbutler.utils.FormatsUtils;
import com.mialab.healthbutler.utils.StepDetector;
import com.mialab.healthbutler.utils.TopSytle;
import com.mialab.healthbutler.view.SliderRelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class ShowMapActivity extends Activity implements LocationSource,
        AMapLocationListener, OnClickListener {

    private HomeDetail mHomeDetail;
    private UiSettings uiSettings;
    private MapView mapView;
    private AMap aMap;
    private AMapLocation myAmapLocation;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private List<LatLng> latLngs = new ArrayList<LatLng>();
    private List<LatLng> latLngsCollectsToSave = new ArrayList<LatLng>();
    private PolylineOptions ployOptions = new PolylineOptions();

    // 判断是否在走路
    public static boolean isWalking = false;

    // 得到点的个数
    private int countF = 0;
    // 首次绘制
    private int firstPaint = 0;
    // 是否首次看地图
    private int firstShowMap = 1;

    private double totalLat = 0;
    private double totalLng = 0;
    private double avgLat;
    private double avglng;

    private boolean openGpsWarning = false;

    private LinearLayout layout;
    private SliderRelativeLayout sliderRelativeLayout;

    private TextView tv_continue, tv_finish;

    // 参数
    // 时间
    // 卡路里
    // 长度
    // 速度
    private long timer = 0;

    private double calories = 0;
    private double distance = 0;
    private double speed = 0;
    private int stepcount = 0;
    private TextView tvtime, tvcaloie, tvlong, tvspeed;

    public static Boolean iscontinue = false;

    private DBHelper dbHelper;
    /*
     * 开始滑动
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                // 滑动
                virbate();
                layout.setVisibility(View.VISIBLE);
                sliderRelativeLayout.setVisibility(View.GONE);
            }
            // else if(msg.what==0)
            // {
            // layout.setVisibility(View.GONE);
            // sliderRelativeLayout.setVisibility(View.VISIBLE);
            // }
            else if (msg.arg1 == 1) {
                // 更新数据
                tvtime.setText(getFormatTime(mHomeDetail.timer - timer) + "");
                tvcaloie.setText(FormatsUtils.formatDouble(
                        mHomeDetail.calories - calories, ShowMapActivity.this)
                        + "kcal");
                tvlong.setText(FormatsUtils.formatDouble(
                        (mHomeDetail.distance - distance) / 1000,
                        ShowMapActivity.this));
                if (timer > 0) {
                    speed = (mHomeDetail.distance - distance) * 1000
                            / (mHomeDetail.timer - timer);
                    tvspeed.setText(FormatsUtils.formatDouble(speed,
                            ShowMapActivity.this) + "m/s");
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paint_on_map);
        TopSytle.setColor(ShowMapActivity.this, Color.argb(180, 1, 1, 1));

        dbHelper = new DBHelper(ShowMapActivity.this);

        sliderRelativeLayout = (SliderRelativeLayout) findViewById(R.id.slider_ralative_layout);
        layout = (LinearLayout) findViewById(R.id.manual_pause_state);
        sliderRelativeLayout.getBackground().setAlpha(220);
        sliderRelativeLayout.setMainHandler(handler);
        // 继续和结束
        tv_continue = (TextView) findViewById(R.id.manual_pause_continue);
        tv_finish = (TextView) findViewById(R.id.manual_pause_complete);
        tv_continue.setOnClickListener(this);
        tv_finish.setOnClickListener(this);

        // 时间热量距离速度
        tvtime = (TextView) findViewById(R.id.tv_time_count);
        tvcaloie = (TextView) findViewById(R.id.tv_caloias_count);
        tvlong = (TextView) findViewById(R.id.tv_distance);
        tvspeed = (TextView) findViewById(R.id.tv_speedcount);

        HomePage homePage = (HomePage) MainActivity.pagerArr.get(0);
        mHomeDetail = homePage.homeDetail;
        // 初始化参数
        timer = mHomeDetail.timer;
        calories = mHomeDetail.calories;
        distance = mHomeDetail.distance;
        speed = 0.0;
        stepcount = StepDetector.CURRENT_SETP;

        mapView = (MapView) findViewById(R.id.mapview);

        // 必须重写
        mapView.onCreate(savedInstanceState);
        // 初始化aMap
        init();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(500);
                        Message msg = new Message();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    // 滑动震动
    private void virbate() {
        Vibrator vibrator = (Vibrator) this
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * 初 始 化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();

            CameraUpdate cu = CameraUpdateFactory.zoomTo(16);
            aMap.moveCamera(cu);

            setupMap();

            aMap.setTrafficEnabled(true);
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
    }

    /**
     * 设置aMap
     */
    private void setupMap() {
        /**
         * 1----->设置定位监听，按定位按钮触发激活定位
         *
         */
        aMap.setLocationSource(this);// 设置定位监听
        uiSettings = aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);// 是否显示定位button
        aMap.setMyLocationEnabled(true);// 显示定位层并可触发定位
        // 设置定位的类型为定位模式，可以由定位，跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    /**
     * 2---->激活定位
     *
     * @param listener
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        // TODO Auto-generated method stub

        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();

            mlocationClient.setLocationListener(this);// 设置定位监听
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);// 设置高精度定位模式
            mLocationOption.setInterval(2000);
            mlocationClient.setLocationOption(mLocationOption);// 设置定位参数
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        myAmapLocation = amapLocation;
        mListener.onLocationChanged(amapLocation);

        /**
         * 绘制路线
         */
        LatLng latlng = new LatLng(myAmapLocation.getLatitude(),
                myAmapLocation.getLongitude());

        /**
         * 没有gps和网络时不进行绘制
         */
        if (myAmapLocation.getLatitude() <= 0
                || myAmapLocation.getLongitude() <= 0) {
            if (!openGpsWarning) {
                Toast.makeText(this, "请打开gps和网络，并确认其可用性", Toast.LENGTH_LONG)
                        .show();
                openGpsWarning = true;
            }
            //return;
        }
        /**
         * 不在走路停止绘图
         */
        // if(!isWalking)
        // {
        // Toast.makeText(this, "没在走路", Toast.LENGTH_SHORT).show();
        // totalLat=0;
        // totalLng=0;
        // countF=0;
        // return;
        // }

        Toast.makeText(this, "正在定位", Toast.LENGTH_SHORT).show();
        totalLat += myAmapLocation.getLatitude();
        totalLng += myAmapLocation.getLongitude();

		/*
         * 每五个点取平均值，然后进行绘制
		 */
        if (countF >= 4) {
            avgLat = totalLat / 5.0;
            avglng = totalLng / 5.0;
            latLngs.add(new LatLng(avgLat, avglng));
            // 收集所有点
            latLngsCollectsToSave.add(new LatLng(avgLat, avglng));
            ployOptions.addAll(latLngs).color(0xff33c3c9).width(10);
            ployOptions.visible(true);
            aMap.addPolyline(ployOptions);

            latLngs.clear();
            countF = 0;
            totalLat = 0;
            totalLng = 0;

            // Toast.makeText(this, "绘制路线" + avgLat + "--" + avglng,
            // Toast.LENGTH_SHORT).show();
        } else {
            countF++;
        }

        /**
         * 首次打开地图设置放大级别
         */
        if (firstShowMap == 1) {
            CameraUpdate cu = CameraUpdateFactory.zoomTo(16);
            aMap.moveCamera(cu);
            firstShowMap = 0;
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    /*
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == tv_continue.getId()) {
            handler.sendEmptyMessage(0);
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            sliderRelativeLayout.setMainHandler(handler);

            // layout.setVisibility(View.GONE);
            // sliderRelativeLayout.setVisibility(View.VISIBLE);
            // iscontinue=true;
        } else if (v.getId() == tv_finish.getId()) {
            // 结束
            finish();

            //存储数据
            Date date = new Date();// 取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 0);// 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            String dateString = formatter.format(date);

            String onetime = tvtime.getText().toString();
            String onecaloie = tvcaloie.getText().toString();
            String onedistance = tvlong.getText().toString();
            String onespeed = tvspeed.getText().toString();

            SportRecords record = new SportRecords();
            record.setDate(dateString);
            record.setTime(onetime);
            record.setDistance(onedistance);
            record.setCalorie(onecaloie);
            record.setSpeed(onespeed);
            record.setStepcount(String.valueOf(StepDetector.CURRENT_SETP
                    - stepcount));
            String savetime = String.valueOf(System.currentTimeMillis());
            record.setSavetime(savetime);
            //存储运动记录和运动轨迹
            dbHelper.insertSportRecord(record);
            dbHelper.insertLatlngRecord(savetime, latLngsCollectsToSave);
        }
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

        return strMinute + ":" + strSecond;
        // + strMillisecond;
    }

    /**
     * 下面四个方法必须重写
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapView.onPause();
        // deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        iscontinue = false;
    }

    /*
     * 屏蔽返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;// 和所有的监听一样返回true是在当前处理不交给上一级。
        }
        return super.onKeyDown(keyCode, event);
    }
}
