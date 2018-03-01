package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.mialab.healthbutler.R;

/**
 * Created by hp on 2016/7/23.
 */
public class RegisterDateActivity extends Activity{
    ImageButton ib_back;
    CardView cv_register_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_date_list);

        initview();
        initData();
    }

    private void initview() {

        ib_back= (ImageButton) findViewById(R.id.ib_back);
        cv_register_confirm= (CardView) findViewById(R.id.cv_register_confirm);
    }

    private  void initData(){

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cv_register_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
