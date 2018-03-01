package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.db.MySqliteHelper;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.math.BigDecimal;

public class HealthShowActivity extends Activity implements View.OnClickListener {
    private TextView tv_show_result;
    private TextView tv_jibu;
    private TextView tv_water;
    private TextView tv_sleep;
    private ImageView iv_jibu;
    private ImageView iv_water;
    private ImageView iv_sleep;
    MySqliteHelper mySqliteHelper = new MySqliteHelper(this, "mydata.db", null, 1);  //mydata.db的数据库
    private String tv_show = "";  //显示测评结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_health_show);
        ActivityCollector.addActivity(this);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.healthyshow));

        initView();

        initData();
    }

    private void initView() {
        tv_show_result = (TextView) findViewById(R.id.textView9);
        tv_jibu = (TextView) findViewById(R.id.textView12);
        tv_water = (TextView) findViewById(R.id.textView10);
        tv_sleep = (TextView) findViewById(R.id.textView20);
        iv_jibu = (ImageView) findViewById(R.id.imageView5);
        iv_water = (ImageView) findViewById(R.id.imageView4);
        iv_sleep = (ImageView) findViewById(R.id.btn_modify);

    }

    private void initData() {


        Intent intent = getIntent();
        int high = Integer.parseInt(intent.getStringExtra("high"));
        int weight = Integer.parseInt(intent.getStringExtra("weight"));

        double bmi = weight / ((high * 0.01) * (high * 0.01));
        String text_jibu = calculator(bmi);
        tv_jibu.setText(text_jibu);


        int water_drink_data = calculator_database("water_drink"); //返回1或0表示选择结果
        int sleep_time = calculator_database("sleep_time");
        int yundong = calculator_database("week_times");
        int stay_up = calculator_database("stay_up");


        BigDecimal b = new BigDecimal(bmi);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();


        tv_show += "1.BMI为" + f1 + ",";
        if (bmi < 18.5) {
            tv_show += "较瘦，注意营养均衡。\r\n";
        } else if (bmi > 25) {
            tv_show += "偏胖，注意加强锻炼。\r\n";
        } else {
            tv_show += "正常体型，建议保持。\r\n";
        }


        if (yundong == 1) {
            tv_show += "2.运动次数合理，建议保持。\r\n";
        } else {
            tv_show += "2.运动量过少，需加强体质锻炼。\r\n";
        }


        if (sleep_time == 1) {
            tv_show += "3.睡眠时间充足。\r\n";
        } else {
            tv_show += "3.睡眠时间不足，建议晚上较早入睡。\r\n";
        }

        if (water_drink_data == 1) {
            tv_show += "4.饮水量充足。\r\n";
        } else {
            tv_show += "4.饮水量不足，建议每天多喝水。\r\n";
        }

        String sleep_time_on = "";
        if (stay_up == 1) {
            sleep_time_on = "23:30";
        } else {
            sleep_time_on = "23:00";
        }


        tv_show_result.setText(tv_show);
        tv_water.setText("每日目标饮水量:8杯");
        tv_sleep.setText("最早上床时间:" + sleep_time_on);


    }


    /*
    * 根据传入的test_name的名字，来返回该行data_string结果
    * */
    private int calculator_database(String water_drink) {
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();
        Cursor c = db.query("my_health", new String[]{"_id", "test_name", "data_string", "question"}, null, null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                if (c.getString(c.getColumnIndex("test_name")).equals(water_drink)) {
                    String result = c.getString(c.getColumnIndex("data_string"));
                    if (result.equals("1"))
                        return 1;
                    else
                        return 0;


                }

            }

        }
        return 2;
    }

    private String calculator(double bmi) {
        String text = "";
        int num_bushu;

        if (bmi < 18.5) {
            num_bushu = 6000; //偏瘦
        } else if (bmi > 25) {
            num_bushu = 10000; //偏重
        } else {
            num_bushu = 7000; //正常
        }

        text = "每日目标步数:" + num_bushu;
        return text;
    }


    @Override
    public void onClick(View v) {

    }
}
