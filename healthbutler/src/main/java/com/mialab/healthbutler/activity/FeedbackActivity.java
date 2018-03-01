package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

/**
 * Created by Wesly186 on 2016/3/27.
 */
public class FeedbackActivity extends Activity implements View.OnClickListener {

    private EditText metText;
    private ImageButton mibBack;
    private ImageButton mibPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.feedbackactivity));
        ActivityCollector.addActivity(this);

        initView();
        initData();
    }

    private void initView() {
        metText = (EditText) findViewById(R.id.et_text);
        mibBack = (ImageButton) findViewById(R.id.ib_back);
        mibPublish = (ImageButton) findViewById(R.id.ib_publish);
    }

    private void initData() {
        mibBack.setOnClickListener(this);
        mibPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_publish:
                if(TextUtils.isEmpty(metText.getText().toString().trim())){
                    Toast.makeText(FeedbackActivity.this, "说点什么吧！", Toast.LENGTH_SHORT).show();
                }else{
                    postData2Server();
                    Toast.makeText(FeedbackActivity.this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void postData2Server() {
        String feed = metText.getText().toString();
    }
}
