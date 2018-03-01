package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

/**
 * 验证手机号是否存在
 * <p/>
 * Created by Wesly186 on 2016/3/17.
 */
public class GetCheckCodeActivity extends Activity {

    private Button btnNext;
    private EditText etPhoneNum;
    private ProgressBar pbCheckNum;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_get_check_code);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.mainactivity));

        btnNext = (Button) findViewById(R.id.btn_next);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        pbCheckNum = (ProgressBar) findViewById(R.id.pb_checknum);
        ibBack = (ImageButton) findViewById(R.id.ib_back);


        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etPhoneNum.getText().toString().length() == 11) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etPhoneNum.getWindowToken(), 0);

                    btnNext.setEnabled(false);
                    pbCheckNum.setVisibility(View.VISIBLE);

                    HttpUtils utils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("mPhoneNum", etPhoneNum.getText().toString());

                    utils.send(HttpRequest.HttpMethod.POST, GlobalContants.CHECK_PHONENUM_URL, params,
                            new RequestCallBack<String>() {

                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    pbCheckNum.setVisibility(View.INVISIBLE);
                                    String result = responseInfo.result;

                                    if (result.equals("exist")) {
                                        Intent intent = new Intent(GetCheckCodeActivity.this, PostCheckNumActivity.class);
                                        intent.putExtra("PhoneNum", etPhoneNum.getText().toString());
                                        intent.putExtra("findBack", true);
                                        startActivity(intent);
                                        finish();
                                    } else if (result.equals("not_exist")) {
                                        Toast.makeText(GetCheckCodeActivity.this, "你还没有使用该手机号注册过", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(GetCheckCodeActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(HttpException error, String msg) {
                                    btnNext.setEnabled(true);
                                    pbCheckNum.setVisibility(View.INVISIBLE);
                                    System.out.println(msg);
                                    Toast.makeText(GetCheckCodeActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();

                                }
                            });
                } else {
                    Toast.makeText(GetCheckCodeActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
