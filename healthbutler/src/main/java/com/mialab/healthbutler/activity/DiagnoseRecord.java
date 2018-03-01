package com.mialab.healthbutler.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;


import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.MyViewPagerAdapter;
import com.mialab.healthbutler.fragment.Fragment1;
import com.mialab.healthbutler.fragment.Fragment2;
import com.mialab.healthbutler.utils.TopSytles;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LFZ on 2016/6/10.
 */
public class DiagnoseRecord extends FragmentActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private List<String> titleList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose);
        TopSytles.setTranslucent(this);

        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("诊断详情"));
        tabLayout.addTab(tabLayout.newTab().setText("诊断结果"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        fragmentList.add(new Fragment1());
        fragmentList.add(new Fragment2());

        titleList.add("诊断详情");
        titleList.add("治疗推荐");
        MyViewPagerAdapter adapter=new MyViewPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
