package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends Activity implements View.OnClickListener {

    private static final int CAMERA_ACTIVITY = 0;
    private static final int ALBUM_ACTIVITY = 1;

    EditText metText;
    ImageButton mibBack;
    ImageButton mibPublish;
    ImageView mivPicture;
    ImageView mivCamera;
    ImageView mivAlbum;
    ImageView mivEmoji;
    String mPhotoPath;
    String mContent;

    //发送结果通知
    NotificationManager notifyMgr;
    Notification notify;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                notifyMgr.cancel(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_write);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.writeactivity));
        ActivityCollector.addActivity(this);

        initView();
        initData();
    }

    private void initView() {
        mibBack = (ImageButton) findViewById(R.id.ib_back);
        mibPublish = (ImageButton) findViewById(R.id.ib_publish);
        mivPicture = (ImageView) findViewById(R.id.iv_picture);
        mivCamera = (ImageView) findViewById(R.id.iv_camera);
        mivAlbum = (ImageView) findViewById(R.id.iv_album);
        mivEmoji = (ImageView) findViewById(R.id.iv_emoji);
        metText = (EditText) findViewById(R.id.et_text);
    }

    private void initData() {
        File file = new File("/sdcard/健康管家/picture");
        file.mkdirs();
        mibBack.setOnClickListener(this);
        mibPublish.setOnClickListener(this);
        mivCamera.setOnClickListener(this);
        mivAlbum.setOnClickListener(this);
        mivEmoji.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.iv_camera:
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    mPhotoPath = "/sdcard/健康管家/picture/" + dateFormat.format(new Date()) + ".jpg";
                    File file = new File(mPhotoPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, CAMERA_ACTIVITY);
                } else {
                    Toast.makeText(WriteActivity.this, "SD卡未挂载", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_album:
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType("image/*");
                startActivityForResult(getAlbum, ALBUM_ACTIVITY);
                break;
            case R.id.ib_publish:
                if (TextUtils.isEmpty(metText.getText().toString().trim())) {
                    Toast.makeText(WriteActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    publish2Server();
                    finish();
                }
                break;
            case R.id.iv_emoji:
                Toast.makeText(WriteActivity.this, "开发中", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void publish2Server() {

        mContent = metText.getText().toString();
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", PrefUtils.getString(WriteActivity.this, GlobalContants.TOKEN, ""));
        params.addBodyParameter("content", mContent);
        if (mPhotoPath != null) {
            params.addBodyParameter(mPhotoPath.replace("/", ""), new File(mPhotoPath));
        }
        //防止重复发送
        mibPublish.setEnabled(false);

        utils.send(HttpRequest.HttpMethod.POST, GlobalContants.PUBLISH_URL, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                showNotify("发送动态成功");
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                showNotify("发送动态失败");
            }
        });

    }

    private void showNotify(String state) {
        notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notify = new Notification.Builder(WriteActivity.this)
                .setSmallIcon(R.drawable.new_version)
                .setContentTitle("发送提醒")
                .setContentText(state)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_app))
                .setWhen(System.currentTimeMillis()) // 设置通知时间
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .getNotification();
        notifyMgr.notify(0, notify);

        mHandler.sendEmptyMessageDelayed(0, 4000);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        BitmapUtils utils = new BitmapUtils(this);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_ACTIVITY) {                          //相机返回结果
                utils.display(mivPicture, mPhotoPath);
            } else if (requestCode == ALBUM_ACTIVITY) {                   //图库返回结果
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mPhotoPath = cursor.getString(columnIndex);
                    cursor.close();
                } else {
                    mPhotoPath = selectedImage.getPath();
                }
                utils.display(mivPicture, mPhotoPath);
            }
        }
    }
}