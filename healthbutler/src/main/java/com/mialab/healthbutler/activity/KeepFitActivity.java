package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import butterknife.ButterKnife;

public class KeepFitActivity extends Activity {

    RelativeLayout rl_keep_sport;
    RelativeLayout rl_keep_diet;
    RelativeLayout rl_keep_girth;
    ImageButton ib_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_keepfit);

        initView();
        initData();
    }

    private void initView() {
        rl_keep_sport= (RelativeLayout) findViewById(R.id.rl_keep_sport);
        rl_keep_diet= (RelativeLayout) findViewById(R.id.rl_keep_diet);
        rl_keep_girth= (RelativeLayout) findViewById(R.id.rl_keep_girth);
        ib_back= (ImageButton) findViewById(R.id.ib_back);
    }

    private void initData() {



        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
