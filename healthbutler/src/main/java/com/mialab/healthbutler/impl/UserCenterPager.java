package com.mialab.healthbutler.impl;

import android.app.Activity;

import com.mialab.healthbutler.base.BasePager;
import com.mialab.healthbutler.impl.usercenterdetail.UserCenterDetail;

/**
 * 用户中心实现
 *
 * @author Wesly
 */
public class UserCenterPager extends BasePager {

    UserCenterDetail userCenterDetail;

    public UserCenterPager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {

        super.initData();

        System.out.println("usercenter initdata...");

        userCenterDetail = new UserCenterDetail(mainActivity);
        userCenterDetail.initData();

        // 向FrameLayout中动态添加布局
        flContent.removeAllViews();
        flContent.addView(userCenterDetail.mRootView);

    }

}
