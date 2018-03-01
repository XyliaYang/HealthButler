package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mialab.healthbutler.R;


public class FoodDisplayActivity extends Activity {

    private TextView tvFoodname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_food_display1);
//        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.tasklistactivity));

        tvFoodname=(TextView) findViewById(R.id.tv_foodname);

        Intent intent=getIntent();
        tvFoodname.setText(intent.getStringExtra("food"));



    }




}
