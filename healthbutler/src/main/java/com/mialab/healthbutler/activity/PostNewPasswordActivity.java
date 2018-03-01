package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

/**
 * 提交新密码
 * <p/>
 * Created by Wesly186 on 2016/3/17.
 */
public class PostNewPasswordActivity extends Activity {

    private String mPhoneNum;
    private ImageButton mBack;
    private EditText mPassword;
    private Button mComplete;
    private ProgressBar mPbCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_password);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.mainactivity));
        ActivityCollector.addActivity(this);

        initView();
        initData();

    }

    private void initView() {
        mBack = (ImageButton) findViewById(R.id.ib_back);
        mPassword = (EditText) findViewById(R.id.et_password);
        mComplete = (Button) findViewById(R.id.btn_complete);
        mPbCommit = (ProgressBar) findViewById(R.id.pb_commit);

    }

    private void initData() {

        Intent intent = getIntent();
        mPhoneNum = intent.getStringExtra("PhoneNum");
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        mComplete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                commitUserInf(mPassword.getText().toString());

            }

        });

    }

    private void commitUserInf(String password) {
        if (isLegal(password)) {
            mComplete.setEnabled(false);
            mPbCommit.setVisibility(View.VISIBLE);

            HttpUtils utils = new HttpUtils();

            RequestParams params = new RequestParams();
            params.addBodyParameter("mPhoneNum", mPhoneNum);
            params.addBodyParameter("mPassword", password);

            utils.send(HttpRequest.HttpMethod.POST, GlobalContants.POST_NEW_PASSWORD, params, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    mPbCommit.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(PostNewPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(PostNewPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishAll();
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    mComplete.setEnabled(true);
                    mPbCommit.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            Toast.makeText(PostNewPasswordActivity.this, "密码格式不合法", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isLegal(String password) {

        if (!TextUtils.isEmpty(password)) {
            if (password.matches("[a-zA-Z0-9_]{6,20}$")) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

}
