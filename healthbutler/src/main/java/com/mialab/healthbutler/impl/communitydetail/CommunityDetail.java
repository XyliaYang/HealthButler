package com.mialab.healthbutler.impl.communitydetail;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.LoginActivity;
import com.mialab.healthbutler.activity.MainActivity;
import com.mialab.healthbutler.activity.WriteActivity;
import com.mialab.healthbutler.base.CommunityBasePager;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.PrefUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * CommunityDetail的flcontent内容
 *
 * @author Wesly
 */
public class CommunityDetail implements OnPageChangeListener {

    public MainActivity mainActivity;
    public View mRootView;
    public ViewPager vpNewsDetail;
    public TabPageIndicator indicator;
    public ArrayList<CommunityBasePager> pgList;
    NewsPager newsPager;
    GrandPage grandPage;
    int currentSelected;
    private ImageView mibWrite;

    public CommunityDetail(Activity activity) {
        mainActivity = (MainActivity) activity;
        initViews();
    }

    public void initViews() {
        mRootView = View.inflate(mainActivity, R.layout.detail_community, null);
        vpNewsDetail = (ViewPager) mRootView.findViewById(R.id.vp_news);
        indicator = (TabPageIndicator) mRootView.findViewById(R.id.indicator);
        mibWrite = (ImageView) mRootView.findViewById(R.id.ib_write);
    }

    public void initData() {

        pgList = new ArrayList<CommunityBasePager>();
        newsPager = new NewsPager(mainActivity);
        grandPage = new GrandPage(mainActivity);
        pgList.add(newsPager);
        pgList.add(grandPage);

        vpNewsDetail.setAdapter(new vpNewsDetail());
        indicator.setViewPager(vpNewsDetail);
        // 设置当前页面为新闻还是社区
        currentSelected = Integer
                .parseInt(PrefUtils.getString(mainActivity, GlobalContants.COMMUNITY_CURRENT_SELECTED, -1 + ""));
        if (currentSelected != -1) {
            indicator.setCurrentItem(currentSelected);
            if (currentSelected == 1) {
                mibWrite.setVisibility(View.VISIBLE);
            } else {
                mibWrite.setVisibility(View.INVISIBLE);
            }
        } else {
            indicator.setCurrentItem(0);
            mibWrite.setVisibility(View.INVISIBLE);
        }

        indicator.setOnPageChangeListener(this);
        mibWrite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String token = PrefUtils.getString(mainActivity, GlobalContants.TOKEN, "");
                if (!TextUtils.isEmpty(token)) {
                    mainActivity.startActivity(new Intent(mainActivity, WriteActivity.class));
                } else {
                    mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                }


            }
        });

    }

    // indicator的OnPageChangeListener
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        PrefUtils.setString(mainActivity, GlobalContants.COMMUNITY_CURRENT_SELECTED, arg0 + "");
        if (arg0 == 0) {
            mibWrite.setVisibility(View.INVISIBLE);
        } else {
            mibWrite.setVisibility(View.VISIBLE);
        }

    }

    class vpNewsDetail extends PagerAdapter {

        @Override
        public int getCount() {

            return pgList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "资讯";
            } else {
                return "社区";
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pgList.get(position).mRootView);
            pgList.get(position).initData();
            return pgList.get(position).mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
