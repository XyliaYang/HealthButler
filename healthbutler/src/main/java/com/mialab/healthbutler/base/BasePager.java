package com.mialab.healthbutler.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.MainActivity;

/**
 * MainActivity的ViewPager基类
 *
 * @author Wesly
 */
public abstract class BasePager {

    public MainActivity mainActivity;
    public View mRootView;
    public FrameLayout flContent;

    public BasePager(Activity activity) {
        mainActivity = (MainActivity) activity;
        initViews();
    }

    public void initViews() {

        mRootView = View.inflate(mainActivity, R.layout.pager_base, null);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);

    }

    public void initData() {

    }


}
