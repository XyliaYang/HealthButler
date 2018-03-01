package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 设置界面
 *
 * @author Wesly
 */
public class SettingActivity extends Activity implements OnClickListener {

    private ImageView mBack;
    private RelativeLayout rlFeedback;
    private RelativeLayout rlCheckUpdate;
    private ImageView ivArrow;
    private ImageView mivNew;
    private RelativeLayout rlAutoUpdate;
    private ToggleButton mtbWifi;
    private RelativeLayout rlExit;

    private ProgressDialog progressDialog;
    private HttpHandler handler;
    private int mVersionCode;
    private String mVersionName;
    private String mDescription;
    private String mDownloadUrl;
    private int mLocalVersionCode;
    private boolean mStartDownload;

    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.mainactivity));
        ActivityCollector.addActivity(this);

        initView();
        initData();

    }

    private void initView() {

        mBack = (ImageView) findViewById(R.id.ib_back);
        rlFeedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rlCheckUpdate = (RelativeLayout) findViewById(R.id.rl_check_update);
        mivNew = (ImageView) findViewById(R.id.ib_has_new);
        ivArrow = (ImageView) findViewById(R.id.iv_check_new);
        rlAutoUpdate = (RelativeLayout) findViewById(R.id.rl_auto_update);
        mtbWifi = (ToggleButton) findViewById(R.id.tb_wifi);
        rlExit = (RelativeLayout) findViewById(R.id.rl_exit);

    }

    private void initData() {
        mToken = PrefUtils.getString(SettingActivity.this, GlobalContants.TOKEN, "");
        if (TextUtils.isEmpty(mToken)) {
            rlExit.setVisibility(View.GONE);
        }
        mStartDownload = getIntent().getBooleanExtra("mStartDownload", true);
        // 初始化自动更新开关
        boolean wifiAutoUpdate = PrefUtils.getBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
        mtbWifi.setChecked(wifiAutoUpdate);

        // 初始化新版本小红点
        boolean hasNew = PrefUtils.getBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, false);
        if (hasNew) {
            mivNew.setVisibility(View.VISIBLE);
            ivArrow.setVisibility(View.INVISIBLE);
        } else {
            mivNew.setVisibility(View.INVISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
        }

        //意见反馈点击事件
        rlFeedback.setOnClickListener(this);
        // 检查更新
        rlCheckUpdate.setOnClickListener(this);
        // 更改自动更新按钮
        rlAutoUpdate.setOnClickListener(this);
        // 返回按钮监听
        mBack.setOnClickListener(this);
        //退出按钮监听
        rlExit.setOnClickListener(this);
        // 更改自动更新按钮
        mtbWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, true);
                } else {
                    PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
                }

            }
        });


    }

    // 检查更新
    private void checkForUpdate() {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        utils.send(HttpMethod.POST, GlobalContants.CHECK_UPDATE_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    mVersionCode = jsonObject.getInt("versionCode");
                    mVersionName = jsonObject.getString("versionName");
                    mDescription = jsonObject.getString("description");
                    mDownloadUrl = jsonObject.getString("downloadUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mLocalVersionCode = getVersionCode();

                if (mLocalVersionCode < mVersionCode) {
                    showUpdateDialog(mVersionName, mDescription, mDownloadUrl);
                    PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, true);
                    mivNew.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(SettingActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, false);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(SettingActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void showUpdateDialog(String versionName, String description,
                                  final String mDownloadUrl) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        AlertDialog dialog = builder.setTitle("版本升级提醒")
                .setMessage("发现新版本：" + versionName + "\n" + "升级功能：\n" + description)
                .setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDownload(mDownloadUrl);
                    }
                })
                .create();
        dialog.show();
    }


    private int getVersionCode() {

        int versionCode = 1;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // 包名错误
            e.printStackTrace();
        }

        return versionCode;

    }

    private void startDownload(String downloadUrl) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            HttpUtils utils = new HttpUtils();
            String path = Environment.getExternalStorageDirectory() + "/健康管家/update.apk";

            handler = utils.download(downloadUrl, path, new RequestCallBack<File>() {

                @Override
                public void onStart() {
                    showProcessDialog(handler);
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    progressDialog.setProgress((int) (current * 100 / total));
                    if (current == total) {
                        progressDialog.dismiss();
                    }
                    System.out.println((float) current / total);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {

                    PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, false);
                    // 跳转到系统安装界面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");

                    startActivityForResult(intent, 0);
                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });
        } else {
            Toast.makeText(SettingActivity.this, "sd卡未挂载", Toast.LENGTH_SHORT).show();
        }

    }

    private void showProcessDialog(final HttpHandler handler) {

        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);
        progressDialog.setTitle("版本升级中");
        progressDialog.setIndeterminate(false);
        progressDialog.setButton("取消升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();

    }

    //二逼用户取消升级处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PrefUtils.setBoolean(SettingActivity.this, GlobalContants.HAS_NEW_VERSION, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;

            case R.id.rl_feedback:
                startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
                break;

            case R.id.rl_check_update:
                if (!mStartDownload) {
                    checkForUpdate();
                } else {
                    Toast.makeText(SettingActivity.this, "正在下载更新中", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.rl_auto_update:
                boolean AutoUpdate = PrefUtils.getBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
                if (AutoUpdate == false) {
                    mtbWifi.setChecked(true);
                    PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, true);
                } else {
                    mtbWifi.setChecked(false);
                    PrefUtils.setBoolean(SettingActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false);
                }
                break;
            case R.id.rl_exit:
                PrefUtils.setString(SettingActivity.this, GlobalContants.GET_USER_INFO, "");
                PrefUtils.setString(SettingActivity.this, GlobalContants.TOKEN, "");

                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                ActivityCollector.finishAll();
        }
    }
}
