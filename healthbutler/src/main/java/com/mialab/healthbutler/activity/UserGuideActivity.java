package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.PrefUtils;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;

/**
 * 新手引导页
 *
 * @author Wesly
 */
public class UserGuideActivity extends Activity implements OnClickListener {

    int[] imageIds = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    ArrayList<ImageView> ivArr;
    private ViewPager vpguide;
    private PageIndicator indicator;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_userguide);

        initView();

        vpguide.setAdapter(new UserGuidePagerAdapter());
        indicator.setViewPager(vpguide);
        indicator.setOnPageChangeListener(new GuidePageListener());
        btnStart.setOnClickListener(this);
    }

    private void initView() {
        vpguide = (ViewPager) findViewById(R.id.vp_guide);
        btnStart = (Button) findViewById(R.id.btn_start);
        indicator = (PageIndicator) findViewById(R.id.indicator);
        ivArr = new ArrayList<ImageView>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(imageIds[i]);
            ivArr.add(iv);
        }

    }

    @Override
    public void onClick(View v) {
        PrefUtils.setBoolean(UserGuideActivity.this, GlobalContants.USER_GUIDE_PREF, true);
        Intent intent = new Intent(UserGuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    class UserGuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageIds.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(ivArr.get(position));
            return ivArr.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class GuidePageListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == imageIds.length - 1) {
                btnStart.setVisibility(View.VISIBLE);
            } else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }

    }
}
