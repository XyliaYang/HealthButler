package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.io.File;
import java.util.Date;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Wesly186 on 2016/3/29.
 */
public class ImageDetailActivity extends Activity implements View.OnClickListener {

    private PhotoView ivPhoto;
    private Button btnSave;

    private String mImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_detail);
        ActivityCollector.addActivity(this);
        TranslucentBarsUtils.setTranslucent(this);

        initView();

        initData();
    }

    private void initView() {
        ivPhoto = (PhotoView) findViewById(R.id.iv_photo);
        btnSave = (Button) findViewById(R.id.btn_save);

    }

    private void initData() {
        BitmapUtils utils = new BitmapUtils(this);
        mImageURL = getIntent().getStringExtra("imageurl");
        utils.display(ivPhoto, mImageURL);

        ivPhoto.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
                overridePendingTransition(R.anim.grand_scale_in, R.anim.photo_scale_out);// 进入动画和退出动画
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
        btnSave.setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.grand_scale_in, R.anim.photo_scale_out);// 进入动画和退出动画
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                savePhoto();
                break;
        }
    }

    private void savePhoto() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            HttpUtils utils = new HttpUtils();
            Date nowDate = new Date();
            String path = Environment.getExternalStorageDirectory() + "/SleepAngel/picture/" + nowDate.getTime() + ".jpg";

            utils.download(mImageURL, path, new RequestCallBack<File>() {

                @Override
                public void onStart() {
                    btnSave.setEnabled(false);
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {

                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    btnSave.setEnabled(true);
                    Toast.makeText(ImageDetailActivity.this, "图片已保存（SleepAngel>picture）", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    btnSave.setEnabled(true);
                    Toast.makeText(ImageDetailActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ImageDetailActivity.this, "sd卡未挂载", Toast.LENGTH_SHORT).show();
        }

    }
}
