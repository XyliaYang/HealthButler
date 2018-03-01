package com.mialab.healthbutler.impl;

import android.app.Activity;
import android.util.Log;

import com.mialab.healthbutler.base.BasePager;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.impl.communitydetail.CommunityDetail;
import com.mialab.healthbutler.utils.PrefUtils;

/**
 * 社区实现
 *
 * @author Wesly
 */
public class CommunityPager extends BasePager {

    public CommunityDetail communityDetail;
    boolean initDataFirstTime = true;

    public CommunityPager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {

        super.initData();

        System.out.println("community initdata...");

        //点击社区时具体刷新那个pager
        Log.i("communityinitfirst", initDataFirstTime + "");
        if (initDataFirstTime) {
            communityDetail = new CommunityDetail(mainActivity);
            communityDetail.initData();
            initDataFirstTime = false;
        } else {
            int currentSelected = Integer.parseInt(PrefUtils.getString(mainActivity, GlobalContants.COMMUNITY_CURRENT_SELECTED, 0 + ""));
            communityDetail.pgList.get(currentSelected).initData();
        }

        // 向FrameLayout中动态添加布局
        flContent.removeAllViews();
        flContent.addView(communityDetail.mRootView);
    }

}
