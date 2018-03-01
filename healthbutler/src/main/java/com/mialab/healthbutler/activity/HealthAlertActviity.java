package com.mialab.healthbutler.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.fragment.DoAlertFragment;
import com.mialab.healthbutler.fragment.DoAlertFragment1;
import com.mialab.healthbutler.fragment.DoAlertFragment2;
import com.mialab.healthbutler.fragment.DoAlertFragment3;
import com.mialab.healthbutler.fragment.HealthAlertListViewFragment;

public class HealthAlertActviity extends Activity implements HealthAlertListViewFragment.ItemClickListener{


    private ImageButton img;
    private FrameLayout frame;
    private FragmentManager fm;
    private FragmentTransaction t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_health_alert_actviity);
        img=(ImageButton)findViewById(R.id.ib_back);
        frame=(FrameLayout)findViewById(R.id.fm_tip_info);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fm=getFragmentManager();
        t=fm.beginTransaction();
        DoAlertFragment fragment=new DoAlertFragment();
        t.replace(R.id.fm_tip_info,fragment);
        t.commit();
    }


    @Override
    public void onItemSelect(int Position) {
        if(Position==0){

            DoAlertFragment fragment=new DoAlertFragment();
            t=fm.beginTransaction();
            t.replace(R.id.fm_tip_info, fragment);

        }
        else if(Position==1){
            DoAlertFragment1 fragment=new DoAlertFragment1();
            t=fm.beginTransaction();
            t.replace(R.id.fm_tip_info, fragment);
        }
        else if(Position==2){
            DoAlertFragment2 fragment=new DoAlertFragment2();
            t=fm.beginTransaction();
            t.replace(R.id.fm_tip_info, fragment);
        }
        else if(Position==3){
            DoAlertFragment3 fragment=new DoAlertFragment3();
            t=fm.beginTransaction();
            t.replace(R.id.fm_tip_info, fragment);
        }
        t.commit();
    }
}
