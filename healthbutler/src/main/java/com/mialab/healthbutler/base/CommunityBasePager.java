package com.mialab.healthbutler.base;

import android.app.Activity;
import android.view.View;

import com.mialab.healthbutler.activity.MainActivity;

/**
 * CommunityPager的viewPager基类
 *
 * @author Wesly
 */
public abstract class CommunityBasePager {

    public MainActivity mainActivity;
    public View mRootView;

    public CommunityBasePager(Activity aty) {
        mainActivity = (MainActivity) aty;
        initView();
    }

    public abstract void initView();

    public void initData() {


    }
}
