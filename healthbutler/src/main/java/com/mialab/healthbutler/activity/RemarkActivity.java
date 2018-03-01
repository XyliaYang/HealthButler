package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;


public class RemarkActivity extends Activity {

    EditText etRemark; //备注文本框
    ImageView ivComplete; //确认
    ImageButton ibBack; //返回按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_remark);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.primary_2));

        etRemark = (EditText) findViewById(R.id.et_remark);
        ivComplete = (ImageView) findViewById(R.id.btn_modify);
        ibBack = (ImageButton) findViewById(R.id.iv_back);

        //返回
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //确认返回值
        ivComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = etRemark.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("remark", remark);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}