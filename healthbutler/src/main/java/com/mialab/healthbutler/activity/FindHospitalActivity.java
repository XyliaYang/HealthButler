package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindHospitalActivity extends Activity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_hospital);
        TranslucentBarsUtils.setColor(this, Color.parseColor("#35b4c2"));
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
