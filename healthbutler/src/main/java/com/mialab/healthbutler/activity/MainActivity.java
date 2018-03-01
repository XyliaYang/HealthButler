package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.base.BasePager;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.impl.CommunityPager;
import com.mialab.healthbutler.impl.HomePage;
import com.mialab.healthbutler.impl.UserCenterPager;
import com.mialab.healthbutler.service.MusicService;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.StepDetector;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * 程序主界面
 *
 * @author Wesly
 */
public class MainActivity extends Activity {

    private LinearLayout rootView;
    public ViewPager vpDetail;
    public RadioGroup rgButton;
    public RadioButton mrbHome;
    public RadioButton mrbCommunity;
    public RadioButton mrbUserCenter;

    public static ArrayList<BasePager> pagerArr;

    public boolean mHomeFirstTime = true;
    public boolean mCommunityFirstTime = true;
    public boolean mUserCenterFirstTime = true;
    public int mHomeClickTimes = 0;
    public int mCommunityClickTimes = 0;
    public int mUserCenterClickTimes = 0;

    public HttpHandler handler;
    ProgressDialog progressDialog;
    boolean mCheckUpdate;
    // 版本信息
    private int mVersionCode;
    private String mVersionName;
    private String mDescription;
    private String mDownloadUrl;
    private int mLocalVersionCode;
    // 上次点击返回键时间
    private long exitTime;

    //是否已经在下载更新
    public boolean mStartDownload;

    //在其他页面更新page
    UpdatePageReceiver mUpdatePageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        TranslucentBarsUtils.setTranslucent(this);
        ActivityCollector.addActivity(this);

        initView();
        initData();

    }

    private void initView() {
        rootView = (LinearLayout) findViewById(R.id.ll_main);
        vpDetail = (ViewPager) findViewById(R.id.vp_detail);
        rgButton = (RadioGroup) findViewById(R.id.rg_botton_tab);
        mrbHome = (RadioButton) findViewById(R.id.rb_home);
        mrbCommunity = (RadioButton) findViewById(R.id.rb_community);
        mrbUserCenter = (RadioButton) findViewById(R.id.rb_usercenter);

    }

    private void initData() {

        //注册MusicReceiver
        mUpdatePageReceiver = new UpdatePageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mialab.sleepangel.updateusercenterpage");
        registerReceiver(mUpdatePageReceiver, filter);

        mCheckUpdate = PrefUtils.getBoolean(MainActivity.this, GlobalContants.CHECK_UPDATE_IN_MAINACTIVITY, true);

        if (mCheckUpdate) {
            checkForUpdate();
        }

        rgButton.check(R.id.rb_home);
        mHomeClickTimes = 1;
        pagerArr = new ArrayList<BasePager>();
        pagerArr.add(new HomePage(this));
        pagerArr.add(new CommunityPager(this));
        pagerArr.add(new UserCenterPager(this));

        mrbHome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeClickTimes++;
                if (mHomeClickTimes >= 2) {
                    //pagerArr.get(0).initData();
                }

            }
        });

        mrbCommunity.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCommunityClickTimes++;
                if (mCommunityClickTimes >= 2) {
                    //pagerArr.get(1).initData();
                }
            }
        });

        mrbUserCenter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mUserCenterClickTimes++;
                if (mUserCenterClickTimes >= 2) {
                    pagerArr.get(2).initData();
                }

            }
        });
        vpDetail.setAdapter(new vpDetailAdapter());
        vpDetail.setCurrentItem(0);
        pagerArr.get(0).initData();
        mHomeFirstTime = false;
        vpDetail.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0 && mHomeFirstTime) {
                    pagerArr.get(0).initData();
                    mHomeFirstTime = false;
                } else if (arg0 == 1 && mCommunityFirstTime) {
                    pagerArr.get(1).initData();
                    mCommunityFirstTime = false;
                } else if (arg0 == 2 && mUserCenterFirstTime) {
                    pagerArr.get(2).initData();
                    mUserCenterFirstTime = false;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        rgButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        rootView.setBackgroundResource(R.drawable.page_1_bg);
                        rgButton.setBackgroundResource(R.color.transparent);
                        vpDetail.setCurrentItem(0, false);

                        mCommunityClickTimes = 0;
                        mUserCenterClickTimes = 0;
                        break;
                    case R.id.rb_community:
                        rootView.setBackgroundResource(R.drawable.page_2_bg);
                        rgButton.setBackgroundColor(Color.parseColor("#F6F6F6"));
                        vpDetail.setCurrentItem(1, false);

                        mHomeClickTimes = 0;
                        mUserCenterClickTimes = 0;
                        break;
                    case R.id.rb_usercenter:
                        rootView.setBackgroundResource(R.drawable.page_3_bg);
                        rgButton.setBackgroundColor(Color.parseColor("#F6F6F6"));
                        vpDetail.setCurrentItem(2, false);

                        mHomeClickTimes = 0;
                        mCommunityClickTimes = 0;
                        break;
                }

            }
        });

    }

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
                    PrefUtils.setBoolean(MainActivity.this, GlobalContants.HAS_NEW_VERSION, true);
                    if (wifiIsAvailable()
                            && PrefUtils.getBoolean(MainActivity.this, GlobalContants.WIFI_AUTO_UPDATE, false)) {
                        startDownload(mDownloadUrl, true);
                    } else {
                        showUpdateDialog(mVersionName, mDescription, mDownloadUrl);
                    }

                } else {
                    PrefUtils.setBoolean(MainActivity.this, GlobalContants.HAS_NEW_VERSION, false);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }

        });

    }

    // 检查wifi网络状态
    protected boolean wifiIsAvailable() {

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            int type = networkInfo.getType();
            return type == ConnectivityManager.TYPE_WIFI;

        }
        return false;
    }

    private void showUpdateDialog(String versionName, String description,
                                  final String mDownloadUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        startDownload(mDownloadUrl, false);
                    }
                })
                .create();
        dialog.show();
    }

    private void startDownload(String downloadUrl, final boolean wifiState) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //设置当前正在下载更新
            mStartDownload = true;

            HttpUtils utils = new HttpUtils();
            String path = Environment.getExternalStorageDirectory() + "/健康管家/update.apk";

            handler = utils.download(downloadUrl, path, new RequestCallBack<File>() {

                @Override
                public void onStart() {
                    if (!wifiState) {
                        showProcessDialog(handler);
                    }
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {

                    if (!wifiState) {

                        progressDialog.setProgress((int) (current * 100 / total));
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {

                    //PrefUtils.setBoolean(MainActivity.this, GlobalContants.HAS_NEW_VERSION, false);
                    if (!wifiState) {
                        // 非wifi状态跳转到系统安装界面

                        progressDialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setDataAndType(Uri.fromFile(responseInfo.result),
                                "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivityForResult(intent, 0);
                    } else {
                        mStartDownload = false;
                        showNotify(responseInfo.result);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    mStartDownload = false;
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "sd卡未挂载", Toast.LENGTH_SHORT).show();
        }

    }

    protected void showNotify(File result) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
        PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notify;
        notify = new Notification.Builder(MainActivity.this).setAutoCancel(true) // 设置点击后取消Notification
                .setTicker("新版本下载完成").setSmallIcon(R.drawable.new_version).setContentTitle("点击安装") // 标题
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app))
                .setContentText("健康管家新版本") // 内容
                .setWhen(System.currentTimeMillis()) // 设置通知时间
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS).setContentIntent(pIntent) // 设置PendingIntent
                .getNotification();
        notifyMgr.notify(0, notify);

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

    private void showProcessDialog(final HttpHandler handler) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("版本升级中");
        progressDialog.setIndeterminate(false);
        progressDialog.setButton("取消升级", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStartDownload = false;
                handler.cancel();
            }
        });

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();

    }

    // 再按一次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //判断当前音乐服务需不需要停止
                if (TextUtils.isEmpty(PrefUtils.getString(MainActivity.this, "togleOn", ""))) {
                    stopService(new Intent(MainActivity.this, MusicService.class));
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mStartDownload = false;
        PrefUtils.setBoolean(MainActivity.this, GlobalContants.HAS_NEW_VERSION, true);
    }

    class vpDetailAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return pagerArr.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager bp = pagerArr.get(position);
            container.addView((View) bp.mRootView);
            return bp.mRootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        HomePage page = (HomePage) pagerArr.get(0);
        page.homeDetail.isBack = false;

        /**
         * 将当前步数存储起来
         */
        SharedPreferences.Editor editor = getSharedPreferences("step", MODE_PRIVATE).edit();
        editor.putInt("stepCount", page.homeDetail.total_step);
        editor.commit();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在本次启动中不在主动提醒升级
        PrefUtils.setBoolean(MainActivity.this, GlobalContants.CHECK_UPDATE_IN_MAINACTIVITY, false);

        HomePage page = (HomePage) pagerArr.get(0);
        page.homeDetail.isBack = true;
        StepDetector.CURRENT_SETPINBACK = 0;

        unregisterReceiver(mUpdatePageReceiver);

    }

    class UpdatePageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            pagerArr.get(2).initData();
        }
    }

}
