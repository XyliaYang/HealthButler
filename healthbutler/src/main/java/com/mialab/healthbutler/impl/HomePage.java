package com.mialab.healthbutler.impl;

import android.app.Activity;

import com.mialab.healthbutler.base.BasePager;
import com.mialab.healthbutler.impl.homedetail.HomeDetail;

/**
 * Created by XiangWei on 2016/3/24.
 */
public class HomePage extends BasePager {

    public HomeDetail homeDetail;

    public HomePage(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {

        super.initData();

        System.out.println("main initdata...");

        homeDetail = new HomeDetail(mainActivity);
        homeDetail.initData();


        // 向FrameLayout中动态添加布局
        flContent.removeAllViews();
        flContent.addView(homeDetail.mRootView);
    }
}
