package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

/**
 * 新闻详情页
 *
 * @author Wesly
 */
public class NewsBodyActivity extends Activity {

    ImageButton mBack;
    TextView mTitle;
    WebView mWebView;

    String mBodyURL;
    String mNewsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_newsbody);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.mainactivity));

        initView();

        initData();

    }

    public void initView() {

        mBack = (ImageButton) findViewById(R.id.ib_back);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mWebView = (WebView) findViewById(R.id.wv_web);

    }

    public void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("newsInf");
        mBodyURL = bundle.getString("BodyURL");
        mNewsTitle = bundle.getString("newsTitle");

        mTitle.setText(mNewsTitle);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(0); // 设置背景色

        mWebView.loadUrl(mBodyURL);

        mWebView.setVisibility(View.VISIBLE);


    }
}
