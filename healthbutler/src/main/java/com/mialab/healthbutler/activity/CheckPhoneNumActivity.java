package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

/**
 * 检测手机号是否已经注册
 *
 * @author Wesly
 */
public class CheckPhoneNumActivity extends Activity {

    ImageButton mBack;
    TextView mAgreement;
    Button mNext;
    CheckBox mChoice;
    ProgressBar mCheckNum;
    EditText mPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_checkphonenum);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.mainactivity));

        initView();
        initData();
    }

    private void initView() {
        mAgreement = (TextView) findViewById(R.id.tv_agreement);
        mNext = (Button) findViewById(R.id.btn_next);
        mChoice = (CheckBox) findViewById(R.id.cb_choice);
        mBack = (ImageButton) findViewById(R.id.ib_back);
        mCheckNum = (ProgressBar) findViewById(R.id.pb_checknum);
        mPhoneNum = (EditText) findViewById(R.id.et_phone_num);

    }

    private void initData() {

        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        mAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mAgreement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);

            }
        });
        mChoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mNext.setEnabled(true);
                } else {
                    mNext.setEnabled(false);
                }

            }
        });

        mNext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPhoneNum.getText().toString().length() == 11) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPhoneNum.getWindowToken(), 0);

                    mCheckNum.setVisibility(View.VISIBLE);

                    HttpUtils utils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("mPhoneNum", mPhoneNum.getText().toString());

                    utils.send(HttpMethod.POST, GlobalContants.CHECK_PHONENUM_URL, params,
                            new RequestCallBack<String>() {

                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    mCheckNum.setVisibility(View.INVISIBLE);
                                    String result = responseInfo.result;

                                    if (result.equals("exist")) {
                                        Toast.makeText(CheckPhoneNumActivity.this, "该号码已经注册", Toast.LENGTH_SHORT).show();
                                    } else if (result.equals("not_exist")) {

                                        Intent intent = new Intent(CheckPhoneNumActivity.this, PostCheckNumActivity.class);
                                        intent.putExtra("PhoneNum", mPhoneNum.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(CheckPhoneNumActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    mCheckNum.setVisibility(View.INVISIBLE);
                                    System.out.println(msg);
                                    Toast.makeText(CheckPhoneNumActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    Toast.makeText(CheckPhoneNumActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
