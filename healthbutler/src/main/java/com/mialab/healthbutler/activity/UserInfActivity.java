package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.domain.UserDetailInfo;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.GetImageUtils;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户详情页
 *
 * @author Wesly
 */
public class UserInfActivity extends Activity implements View.OnClickListener {

    private ImageButton ibBack;
    private Button btnSave;
    private RelativeLayout rlHead;
    private CircleImageView ivHead;
    private RelativeLayout rlNickName;
    private TextView tvName;
    private RelativeLayout rlSex;
    private TextView tvSex;
    private RelativeLayout rlSignature;
    private TextView tvSignature;
    private RelativeLayout rlPassword;

    private UserDetailInfo mUserInf;
    private String mToken;

    private boolean isHeadSculptureChanged = false;

    private GetImageUtils getImageUtils;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_inf);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.mainactivity));
        ActivityCollector.addActivity(this);

        initView();

        initData();
    }

    public void initView() {

        ibBack = (ImageButton) findViewById(R.id.ib_back);
        btnSave = (Button) findViewById(R.id.btn_save);
        rlHead = (RelativeLayout) findViewById(R.id.rl_head);
        ivHead = (CircleImageView) findViewById(R.id.iv_head);
        rlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
        tvName = (TextView) findViewById(R.id.tv_nickname);
        rlSex = (RelativeLayout) findViewById(R.id.rl_sex);
        tvSex = (TextView) findViewById(R.id.tv_sexContent);
        rlSignature = (RelativeLayout) findViewById(R.id.rl_signature);
        tvSignature = (TextView) findViewById(R.id.tv_signature);
        rlPassword = (RelativeLayout) findViewById(R.id.rl_password);
    }

    public void initData() {
        mToken = PrefUtils.getString(UserInfActivity.this, GlobalContants.TOKEN, "");

        String cache = CacheUtils.getCache(UserInfActivity.this, GlobalContants.GET_USER_INFO, "");
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }

        getDataFromServer();

        ibBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        rlHead.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlSignature.setOnClickListener(this);
        rlPassword.setOnClickListener(this);

        tempFile = new File(Environment.getExternalStorageDirectory(), GetImageUtils.getPhotoFileName());
        getImageUtils = new GetImageUtils(this, tempFile, ivHead);


    }

    private void checkOldPassword(String oldPassword, final String newPassword, final AlertDialog dialog) {
        final HttpUtils httpUtils = new HttpUtils();
        final RequestParams params = new RequestParams();

        params.addBodyParameter("UUID", mToken);
        params.addBodyParameter("password", oldPassword);
        httpUtils.send(HttpMethod.POST, GlobalContants.CHECK_PASSWORD_VALUED, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result.equals("passwordIsvalued")) {
                    setNewPassword(newPassword);
                    dialog.cancel();
                } else {
                    Toast.makeText(UserInfActivity.this, "原密码输入不正确", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private void setNewPassword(String new_Password) {

        HttpUtils utils = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", mToken);
        params.addBodyParameter("newPassword", new_Password);
        utils.send(HttpMethod.POST, GlobalContants.UPDATE_PASSWORD, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(UserInfActivity.this, "修改密码成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    public void postData2Server() {
        String userName = tvName.getText().toString();
        String sex = tvSex.getText().toString();
        String signature = tvSignature.getText().toString();
        //封装用户信息
        UserDetailInfo userDetailInfo = new UserDetailInfo();
        userDetailInfo.setmSex(sex);
        userDetailInfo.setmSignature(signature);
        userDetailInfo.setmUserName(userName);
        //转化为json数据
        Gson gson = new Gson();
        String userDetailInfoGson = gson.toJson(userDetailInfo);

        HttpUtils utils = new HttpUtils();

        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", mToken);
        params.addBodyParameter("userDetailInfo", userDetailInfoGson);

        utils.send(HttpMethod.POST, GlobalContants.SAVE_USER_INFO_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(UserInfActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                isHeadSculptureChanged = false;
                finish();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(UserInfActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

        if (isHeadSculptureChanged) {
            RequestParams params2 = new RequestParams();
            params2.addBodyParameter("UUID", mToken);
            params2.addBodyParameter(tempFile.getPath().replace("/", ""), tempFile);
            utils.send(HttpMethod.POST, GlobalContants.UPLOAD_HAED_PICTURE, params2, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });
        }
    }

    public void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();

        params.addBodyParameter("UUID", mToken);
        utils.send(HttpMethod.POST, GlobalContants.GET_USER_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtils.setCache(UserInfActivity.this, GlobalContants.GET_USER_INFO, result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private void parseData(String result) {
        BitmapUtils utils = new BitmapUtils(UserInfActivity.this);
        Gson gson = new Gson();
        mUserInf = gson.fromJson(result, UserDetailInfo.class);

        if (mUserInf != null) {
            tvName.setText(mUserInf.getmUserName());
            if (!TextUtils.isEmpty(mUserInf.getmSex())) {
                tvSex.setText(mUserInf.getmSex());
            } else {
                tvSex.setText("男");
            }
            if (!TextUtils.isEmpty(mUserInf.getmSignature())) {
                tvSignature.setText(mUserInf.getmSignature());
            }

            if (!TextUtils.isEmpty(mUserInf.getmHeadURL())) {
                utils.display(ivHead, mUserInf.getmHeadURL());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ib_back:
                finish();
                break;

            case R.id.btn_save:
                postData2Server();
                //通过广播通知Activity更新UserCenterPage
                Intent intent = new Intent("com.mialab.sleepangel.updateusercenterpage");
                sendOrderedBroadcast(intent, null);
                break;

            case R.id.rl_head:
                getImageUtils.showDialog();
                break;

            case R.id.rl_nickname:
                AlertDialog.Builder nameBuilder = new AlertDialog.Builder(UserInfActivity.this);
                final AlertDialog nameDialog;
                View nameView = View.inflate(UserInfActivity.this, R.layout.dialog_nick_name, null);
                nameBuilder.setView(nameView);
                nameDialog = nameBuilder.create();

                final EditText et_nickName = (EditText) nameView.findViewById(R.id.et_nickName);
                Button btnSure = (Button) nameView.findViewById(R.id.btn_sure);
                Button btnCancel = (Button) nameView.findViewById(R.id.btn_cancel);

                btnSure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String nickname = et_nickName.getText().toString();
                        if (!nickname.equals(""))
                            tvName.setText(nickname);
                        nameDialog.cancel();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        nameDialog.cancel();
                    }
                });
                nameDialog.show();
                break;

            case R.id.rl_sex:
                AlertDialog.Builder sexBuilder = new AlertDialog.Builder(UserInfActivity.this);
                final AlertDialog sexDialog;
                View sexview = View.inflate(UserInfActivity.this, R.layout.dialog_sex, null);
                sexBuilder.setView(sexview);
                sexDialog = sexBuilder.create();

                final RadioButton rb_male = (RadioButton) sexview.findViewById(R.id.rb_male);
                final RadioButton rb_female = (RadioButton) sexview.findViewById(R.id.rb_female);

                Button sexSure = (Button) sexview.findViewById(R.id.btn_sure);
                Button sexCancel = (Button) sexview.findViewById(R.id.btn_cancel);
                sexSure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (rb_male.isChecked()) {
                            tvSex.setText("男");
                        }
                        if (rb_female.isChecked()) {
                            tvSex.setText("女");
                        }
                        sexDialog.cancel();
                    }
                });
                sexCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        sexDialog.cancel();
                    }
                });

                sexDialog.show();
                break;

            case R.id.rl_signature:
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfActivity.this);
                final AlertDialog dialog;
                View signatureView = View.inflate(UserInfActivity.this, R.layout.dialog_nick_name, null);
                builder.setView(signatureView);
                dialog = builder.create();

                TextView tvTitle = (TextView) signatureView.findViewById(R.id.tv_title);
                tvTitle.setText("编辑你的签名");
                final EditText etSignature = (EditText) signatureView.findViewById(R.id.et_nickName);
                Button Sure = (Button) signatureView.findViewById(R.id.btn_sure);
                Button Cancel = (Button) signatureView.findViewById(R.id.btn_cancel);

                Sure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String nickname = etSignature.getText().toString();
                        if (!nickname.equals(""))
                            tvSignature.setText(nickname);
                        dialog.cancel();
                    }
                });
                Cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;

            case R.id.rl_password:
                AlertDialog.Builder passwordBuilder = new AlertDialog.Builder(UserInfActivity.this);
                final AlertDialog passDialog;
                View passwordView = View.inflate(UserInfActivity.this, R.layout.dialog_password, null);
                passwordBuilder.setView(passwordView);
                passDialog = passwordBuilder.create();

                final EditText etOldPassword = (EditText) passwordView.findViewById(R.id.et_oldPassword);
                final EditText etNewPassword = (EditText) passwordView.findViewById(R.id.et_newPassword);
                final EditText etSurePassword = (EditText) passwordView.findViewById(R.id.et_surePassword);
                Button passwordSure = (Button) passwordView.findViewById(R.id.btn_sure);
                Button passwordCancel = (Button) passwordView.findViewById(R.id.btn_cancel);

                passwordSure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final String oldPassword = etOldPassword.getText().toString();
                        final String newPassword = etNewPassword.getText().toString();
                        final String surePassword = etSurePassword.getText().toString();

                        if (!newPassword.equals(surePassword)) {
                            Toast.makeText(UserInfActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            checkOldPassword(oldPassword, newPassword, passDialog);
                        }
                    }
                });
                passwordCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        passDialog.cancel();
                    }
                });

                passDialog.show();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GetImageUtils.PHOTO_CARMERA:
                getImageUtils.startPhotoZoom(Uri.fromFile(tempFile), 300);
                break;
            case GetImageUtils.PHOTO_PICK:
                if (null != data) {
                    getImageUtils.startPhotoZoom(data.getData(), 300);
                }
                break;
            case GetImageUtils.PHOTO_CUT:
                if (null != data) {
                    getImageUtils.setPicToView(data);
                    isHeadSculptureChanged = true;
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
