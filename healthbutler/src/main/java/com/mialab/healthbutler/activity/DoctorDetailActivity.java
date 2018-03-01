package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.Doctor;

/**
 * Created by hp on 2016/6/11.
 */
public class DoctorDetailActivity  extends Activity {

    TextView tv_detail;
    TextView tv_name;
    TextView tv_hospital;
    TextView tv_illness;
    ImageView  iv_head;
    ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_doctor_list);


        initview();
        initData();
    }

    private void initData() {

        Intent intent=getIntent();
        Doctor doctor= (Doctor) intent.getSerializableExtra("doctor");
        tv_name.setText(doctor.getDoctor_name());
        tv_hospital.setText(doctor.getHospital_name());
        tv_illness.setText(doctor.getIllness_name());
        tv_detail.setText(doctor.getDoctor_intro());


        Glide.with(DoctorDetailActivity.this)
                .load(doctor.getHead_image())
                .placeholder(R.drawable.default_head)
                .error(R.drawable.doctor)
                .into(iv_head);


        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initview() {
        setContentView(R.layout.activity_doctor_detail);
        tv_detail= (TextView) findViewById(R.id.tv_detail);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_hospital= (TextView) findViewById(R.id.tv_hospital);
        tv_illness= (TextView) findViewById(R.id.tv_illness);
        iv_head= (ImageView) findViewById(R.id.iv_head);
        ib_back= (ImageButton) findViewById(R.id.ib_back);


    }


}
