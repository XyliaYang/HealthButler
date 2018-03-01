package com.mialab.healthbutler.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.MyViewPagerAdapter;
import com.mialab.healthbutler.fragment.Fragment1;
import com.mialab.healthbutler.fragment.Fragment2;
import com.mialab.healthbutler.utils.TopSytles;

import java.util.ArrayList;
import java.util.List;

public class DiagnoseActivity extends FragmentActivity implements TabLayout.OnTabSelectedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<Fragment> fragmentList=new ArrayList<Fragment>();
    private List<String> titleList=new ArrayList<String>();
    TextView tv_head;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnose2);
        TopSytles.setTranslucent(this);
        initView();
    }

    private void initView() {
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        viewPager=(ViewPager)findViewById(R.id.vp);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.app_bar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.inflateMenu(R.menu.toolbar_menu2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://wapjbk.39.net/"));
                    startActivity(intent);
                }
                return true;
            }
        });

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        //collapsingToolbarLayout.setTitle("诊断记录");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        tv_head=(TextView)findViewById(R.id.tv_head);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset<=-appBarLayout.getHeight()/2){
                    collapsingToolbarLayout.setTitle("诊断记录");
                }
                else if(verticalOffset>-appBarLayout.getHeight()/2&&verticalOffset<=-appBarLayout.getHeight()/3){
                    tv_head.setVisibility(View.INVISIBLE);
                    collapsingToolbarLayout.setTitle("");
                }
                else{
                    tv_head.setVisibility(View.VISIBLE);
                    tv_head.setText("诊断记录");
                }
            }
        });
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
