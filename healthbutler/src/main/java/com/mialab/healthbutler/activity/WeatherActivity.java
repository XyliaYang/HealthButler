package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.utils.ActuaWeather;
import com.mialab.healthbutler.utils.FutureWeather;
import com.mialab.healthbutler.utils.OneDayWeather;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;
import com.mialab.healthbutler.view.WeatherView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends Activity implements OnClickListener {

    public static boolean loadcomplete = false;

    private ProgressDialog progressDialog;
    WeatherView weatherView;
    // 实时天气
    TextView tv_temp_main;
    TextView tv_update_time;
    TextView tv_wind_strengs;
    TextView tv_humidity;
    TextView tv_weather_main;

    ActuaWeather actuaWeather;

    // 今日天气
    OneDayWeather oneDayWeather;
    TextView tv_date;
    TextView tv_week;
    TextView tv_weather_today;
    TextView tv_temp_today;
    TextView tv_wind_today;
    ImageView img_weather;
    TextView tv_clothes_details;
    TextView tv_sport_details;
    TextView tv_bright_details;
    TextView tv_wet_details;
    TextView tv_car_details;
    TextView tv_travel_details;

    // 天气预报
    FutureWeather futureWeather;
    List<FutureWeather> futureWeatherList = new ArrayList<FutureWeather>();
    List<TextView> tv_weeklist = new ArrayList<TextView>();
    List<TextView> tv_Weatherlist = new ArrayList<TextView>();
    List<TextView> tv_Weatherlist2 = new ArrayList<TextView>();
    List<ImageView> WeatherImgList = new ArrayList<ImageView>();
    List<ImageView> WeatherImgList2 = new ArrayList<ImageView>();
    public static List<Integer> hightempList = new ArrayList<Integer>();
    public static List<Integer> lowtempList = new ArrayList<Integer>();
    List<TextView> windsList = new ArrayList<TextView>();
    List<TextView> windStrongList = new ArrayList<TextView>();

    // 星期
    TextView br_day1_day;
    TextView br_day2_day;
    TextView br_day3_day;
    TextView br_day4_day;
    TextView br_day5_day;
    TextView br_day6_day;
    // 第一个天气
    TextView tv_br_day1_weather;
    TextView tv_br_day2_weather;
    TextView tv_br_day3_weather;
    TextView tv_br_day4_weather;
    TextView tv_br_day5_weather;
    TextView tv_br_day6_weather;
    // 第二个天气
    TextView tv_br_day1_weather2;
    TextView tv_br_day2_weather2;
    TextView tv_br_day3_weather2;
    TextView tv_br_day4_weather2;
    TextView tv_br_day5_weather2;
    TextView tv_br_day6_weather2;
    //天气图片
    ImageView img_day11;
    ImageView img_day21;
    ImageView img_day31;
    ImageView img_day41;
    ImageView img_day51;
    ImageView img_day61;
    ImageView img_day12;
    ImageView img_day22;
    ImageView img_day32;
    ImageView img_day42;
    ImageView img_day52;
    ImageView img_day62;
    //风
    TextView wind_day1_strength;
    TextView wind_day2_strength;
    TextView wind_day3_strength;
    TextView wind_day4_strength;
    TextView wind_day5_strength;
    TextView wind_day6_strength;
    TextView wind_day1;
    TextView wind_day2;
    TextView wind_day3;
    TextView wind_day4;
    TextView wind_day5;
    TextView wind_day6;

    ImageView img_back;
    ImageView img_reload;
    TextView tv_reload;

    String weather_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);
        TranslucentBarsUtils.setTranslucent(this);
        ActivityCollector.addActivity(this);

        //绘制温度的控件
        weatherView = (WeatherView) findViewById(R.id.loadview);

        progressDialog = new ProgressDialog(WeatherActivity.this);

        // 实时天气
        tv_temp_main = (TextView) findViewById(R.id.tv_temp_main);
        tv_update_time = (TextView) findViewById(R.id.tv_update_time);
        tv_wind_strengs = (TextView) findViewById(R.id.wind_strengs);
        tv_humidity = (TextView) findViewById(R.id.humidity);
        tv_weather_main = (TextView) findViewById(R.id.tv_weather_main);

        // 今日天气
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_weather_today = (TextView) findViewById(R.id.tv_weather_today);
        tv_temp_today = (TextView) findViewById(R.id.tv_temp_today);
        tv_wind_today = (TextView) findViewById(R.id.tv_wind_today);
        img_weather = (ImageView) findViewById(R.id.img_weather);

        tv_clothes_details = (TextView) findViewById(R.id.tv_clothes_details);
        tv_sport_details = (TextView) findViewById(R.id.tv_sport_details);
        tv_bright_details = (TextView) findViewById(R.id.tv_bright_details);
        tv_wet_details = (TextView) findViewById(R.id.tv_wet_details);
        tv_car_details = (TextView) findViewById(R.id.tv_car_details);
        tv_travel_details = (TextView) findViewById(R.id.tv_travel_details);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        img_reload = (ImageView) findViewById(R.id.img_reload);
        img_reload.setOnClickListener(this);
        tv_reload = (TextView) findViewById(R.id.tv_update);
        tv_reload.setOnClickListener(this);

        // 天气预报
        initWeatherFuture();

        new LoadWeatherAsyncTask().execute("");
        new LoadNowWeatherAsyncTask().execute("");
    }

    /*
     * 加载天气数据
     */
    class LoadWeatherAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if (!progressDialog.isShowing()) {
                progressDialog.setMessage("正在更新天气...");
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "http://v.juhe.cn/weather/index?format=2&cityname=%E8%8B%8F%E5%B7%9E&key=95108b6a0700fdba8b4531345623f4bc";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求成功
                    String result = EntityUtils.toString(
                            httpResponse.getEntity(), "utf-8");
                    JSONObject jo = new JSONObject(result);
                    JSONObject jsonData = jo.getJSONObject("result");

                    // 实时天气
                    JSONObject jsonActuaWeather = jsonData.getJSONObject("sk");
                    actuaWeather = new ActuaWeather();
                    actuaWeather = JSON.parseObject(
                            jsonActuaWeather.toString(), ActuaWeather.class);

                    // 今日天气
                    JSONObject jsonOnedayWeatherObject = jsonData
                            .getJSONObject("today");
                    oneDayWeather = new OneDayWeather();
                    oneDayWeather = JSON.parseObject(
                            jsonOnedayWeatherObject.toString(),
                            OneDayWeather.class);

                    // 未来几天的天气预报
                    JSONArray jsonfutureWeatherObject = jsonData
                            .getJSONArray("future");
                    for (int i = 0; i < 6; i++) {
                        JSONObject jofutureWeather = jsonfutureWeatherObject
                                .getJSONObject(i);
                        futureWeather = new FutureWeather();
                        futureWeather = JSON
                                .parseObject(jofutureWeather.toString(),
                                        FutureWeather.class);
                        futureWeatherList.add(futureWeather);
                    }
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
                // 更新实时天气
                tv_temp_main.setText(actuaWeather.getTemp() + "°");
                tv_update_time.setText("上次更新：" + actuaWeather.getTime());
                tv_wind_strengs.setText(actuaWeather.getWind_strength());
                tv_humidity.setText(actuaWeather.getHumidity() + "");

                // 更新今日天气
                tv_date.setText(oneDayWeather.getDate_y());
                tv_week.setText(oneDayWeather.getWeek());
                tv_weather_today.setText(oneDayWeather.getWeather());
                tv_temp_today.setText(oneDayWeather.getTemperature());
                tv_wind_today.setText(oneDayWeather.getWind());

                String weather_today = oneDayWeather.getWeather();
                if (weather_today.contains("雨")) {
                    img_weather.setImageResource(R.drawable.raining);
                } else if (weather_today.contains("阴")) {
                    img_weather.setImageResource(R.drawable.cloudyall);
                } else if (weather_today.contains("云")) {
                    img_weather.setImageResource(R.drawable.cloudy);
                } else if (weather_today.contains("晴")) {
                    img_weather.setImageResource(R.drawable.sun);
                }

                tv_clothes_details.setText(oneDayWeather.getDressing_index());
                tv_sport_details.setText(oneDayWeather.getExercise_index());
                tv_bright_details.setText(oneDayWeather.getUv_index());
                tv_wet_details.setText(oneDayWeather.getDrying_index().equals(
                        "") ? "正常" : oneDayWeather.getDrying_index());
                tv_car_details.setText(oneDayWeather.getWash_index());
                tv_travel_details.setText(oneDayWeather.getTravel_index());

                // 过几天天气
                for (int i = 0; i < 6; i++) {
                    // 星期
                    if (i > 0) {
                        tv_weeklist.get(i).setText(
                                futureWeatherList.get(i).getWeek());
                    }

                    //加载温度
                    String temp = futureWeatherList.get(i).getTemperature();
                    String beforetemp = temp.substring(0, temp.indexOf("~"));
                    String aftertemp = temp.substring(temp.indexOf("~") + 1, temp.length());

                    int templow = Integer.parseInt(beforetemp.substring(0, beforetemp.indexOf("℃")));
                    int temphigh = Integer.parseInt(aftertemp.substring(0, aftertemp.indexOf("℃")));
                    hightempList.add(temphigh);
                    lowtempList.add(templow);
                    loadcomplete = true;
                    weatherView.invalidate();

                    //加载风
                    String winds = futureWeatherList.get(i).getWind();
                    windsList.get(i).setText(winds.substring(winds.indexOf("风") + 1, winds.length()));
                    windStrongList.get(i).setText(winds.substring(0, winds.indexOf("风") + 1));


                    // 文字天气
                    String weather = futureWeatherList.get(i).getWeather();
                    if (weather.contains("转")) {
                        //文字
                        tv_Weatherlist.get(i).setText(
                                weather.substring(0, weather.indexOf("转")));
                        tv_Weatherlist2.get(i).setText(
                                weather.substring(weather.indexOf("转")));

                        String weather_before = weather.substring(0, weather.indexOf("转"));
                        String weather_behind = weather.substring(weather.indexOf("转"));
                        //图片
                        if (weather_before.contains("雨")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.raining);
                        } else if (weather_before.contains("阴")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.cloudyall);
                        } else if (weather_before.contains("云")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.cloudy);
                        } else if (weather_before.contains("晴")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.sun);
                        }
                        if (weather_behind.contains("雨")) {
                            WeatherImgList2.get(i).setImageResource(R.drawable.raining);
                        } else if (weather_behind.contains("阴")) {
                            WeatherImgList2.get(i).setImageResource(R.drawable.cloudyall);
                        } else if (weather_behind.contains("云")) {
                            WeatherImgList2.get(i).setImageResource(R.drawable.cloudy);
                        } else if (weather_behind.contains("晴")) {
                            WeatherImgList2.get(i).setImageResource(R.drawable.sun);
                        }

                    } else {
                        //文字
                        tv_Weatherlist.get(i).setText(
                                futureWeatherList.get(i).getWeather());

                        tv_Weatherlist2.get(i).setText(
                                futureWeatherList.get(i).getWeather());
                        //图片
                        if (weather.contains("雨")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.raining);
                            WeatherImgList2.get(i).setImageResource(R.drawable.raining);
                        } else if (weather.contains("阴")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.cloudyall);
                            WeatherImgList2.get(i).setImageResource(R.drawable.cloudyall);
                        } else if (weather.contains("云")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.cloudy);
                            WeatherImgList2.get(i).setImageResource(R.drawable.cloudy);
                        } else if (weather.contains("晴")) {
                            WeatherImgList.get(i).setImageResource(R.drawable.sun);
                            WeatherImgList2.get(i).setImageResource(R.drawable.sun);
                        }
                    }

                }
            } else {
                Toast.makeText(WeatherActivity.this, "更新失败，请检查网络",
                        Toast.LENGTH_SHORT).show();
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }


    class LoadNowWeatherAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = "http://op.juhe.cn/onebox/weather/query?cityname=%E8%8B%8F%E5%B7%9E&d&key=8c19fc302c947489558d1a4acb0bca8f";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                    JSONObject jo = new JSONObject(result);
                    weather_now = jo.getJSONObject("result").getJSONObject("data").getJSONObject("realtime").getJSONObject("weather").getString("info");
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
                tv_weather_main.setText(weather_now);
            } else {
                Toast.makeText(WeatherActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
            }

        }

    }

    /*
     * 初始化天气控件
     */
    private void initWeatherFuture() {
        // 周几
        br_day1_day = (TextView) findViewById(R.id.br_day1_day);
        br_day2_day = (TextView) findViewById(R.id.br_day2_day);
        br_day3_day = (TextView) findViewById(R.id.br_day3_day);
        br_day4_day = (TextView) findViewById(R.id.br_day4_day);
        br_day5_day = (TextView) findViewById(R.id.br_day5_day);
        br_day6_day = (TextView) findViewById(R.id.br_day6_day);
        tv_weeklist.add(br_day1_day);
        tv_weeklist.add(br_day2_day);
        tv_weeklist.add(br_day3_day);
        tv_weeklist.add(br_day4_day);
        tv_weeklist.add(br_day5_day);
        tv_weeklist.add(br_day6_day);
        // 天气1
        tv_br_day1_weather = (TextView) findViewById(R.id.tv_br_day1_weather);
        tv_br_day2_weather = (TextView) findViewById(R.id.tv_br_day2_weather);
        tv_br_day3_weather = (TextView) findViewById(R.id.tv_br_day3_weather);
        tv_br_day4_weather = (TextView) findViewById(R.id.tv_br_day4_weather);
        tv_br_day5_weather = (TextView) findViewById(R.id.tv_br_day5_weather);
        tv_br_day6_weather = (TextView) findViewById(R.id.tv_br_day6_weather);

        tv_Weatherlist.add(tv_br_day1_weather);
        tv_Weatherlist.add(tv_br_day2_weather);
        tv_Weatherlist.add(tv_br_day3_weather);
        tv_Weatherlist.add(tv_br_day4_weather);
        tv_Weatherlist.add(tv_br_day5_weather);
        tv_Weatherlist.add(tv_br_day6_weather);
        // 天气2
        tv_br_day1_weather2 = (TextView) findViewById(R.id.tv_br_day1_weather2);
        tv_br_day2_weather2 = (TextView) findViewById(R.id.tv_br_day2_weather2);
        tv_br_day3_weather2 = (TextView) findViewById(R.id.tv_br_day3_weather2);
        tv_br_day4_weather2 = (TextView) findViewById(R.id.tv_br_day4_weather2);
        tv_br_day5_weather2 = (TextView) findViewById(R.id.tv_br_day5_weather2);
        tv_br_day6_weather2 = (TextView) findViewById(R.id.tv_br_day6_weather2);

        tv_Weatherlist2.add(tv_br_day1_weather2);
        tv_Weatherlist2.add(tv_br_day2_weather2);
        tv_Weatherlist2.add(tv_br_day3_weather2);
        tv_Weatherlist2.add(tv_br_day4_weather2);
        tv_Weatherlist2.add(tv_br_day5_weather2);
        tv_Weatherlist2.add(tv_br_day6_weather2);
        //天气图片
        img_day11 = (ImageView) findViewById(R.id.img_day11);
        img_day21 = (ImageView) findViewById(R.id.img_day21);
        img_day31 = (ImageView) findViewById(R.id.img_day31);
        img_day41 = (ImageView) findViewById(R.id.img_day41);
        img_day51 = (ImageView) findViewById(R.id.img_day51);
        img_day61 = (ImageView) findViewById(R.id.img_day61);
        img_day12 = (ImageView) findViewById(R.id.img_day12);
        img_day22 = (ImageView) findViewById(R.id.img_day22);
        img_day32 = (ImageView) findViewById(R.id.img_day32);
        img_day42 = (ImageView) findViewById(R.id.img_day42);
        img_day52 = (ImageView) findViewById(R.id.img_day52);
        img_day62 = (ImageView) findViewById(R.id.img_day62);

        WeatherImgList.add(img_day11);
        WeatherImgList.add(img_day21);
        WeatherImgList.add(img_day31);
        WeatherImgList.add(img_day41);
        WeatherImgList.add(img_day51);
        WeatherImgList.add(img_day61);
        WeatherImgList2.add(img_day12);
        WeatherImgList2.add(img_day22);
        WeatherImgList2.add(img_day32);
        WeatherImgList2.add(img_day42);
        WeatherImgList2.add(img_day52);
        WeatherImgList2.add(img_day62);

        //风
        wind_day1_strength = (TextView) findViewById(R.id.wind_day1_strength);
        wind_day2_strength = (TextView) findViewById(R.id.wind_day2_strength);
        wind_day3_strength = (TextView) findViewById(R.id.wind_day3_strength);
        wind_day4_strength = (TextView) findViewById(R.id.wind_day4_strength);
        wind_day5_strength = (TextView) findViewById(R.id.wind_day5_strength);
        wind_day6_strength = (TextView) findViewById(R.id.wind_day6_strength);
        wind_day1 = (TextView) findViewById(R.id.wind_day1);
        wind_day2 = (TextView) findViewById(R.id.wind_day2);
        wind_day3 = (TextView) findViewById(R.id.wind_day3);
        wind_day4 = (TextView) findViewById(R.id.wind_day4);
        wind_day5 = (TextView) findViewById(R.id.wind_day5);
        wind_day6 = (TextView) findViewById(R.id.wind_day6);
        windsList.add(wind_day1);
        windsList.add(wind_day2);
        windsList.add(wind_day3);
        windsList.add(wind_day4);
        windsList.add(wind_day5);
        windsList.add(wind_day6);
        windStrongList.add(wind_day1_strength);
        windStrongList.add(wind_day2_strength);
        windStrongList.add(wind_day3_strength);
        windStrongList.add(wind_day4_strength);
        windStrongList.add(wind_day5_strength);
        windStrongList.add(wind_day6_strength);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (img_back.getId() == v.getId()) {
            this.finish();
        } else if (img_reload.getId() == v.getId()) {
            new LoadWeatherAsyncTask().execute("");
            new LoadNowWeatherAsyncTask().execute("");
        } else if (tv_reload.getId() == v.getId()) {
            new LoadWeatherAsyncTask().execute("");
            new LoadNowWeatherAsyncTask().execute("");
        }
    }
}
