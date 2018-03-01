package com.mialab.healthbutler.activity;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.HosDetailAdapter;
import com.mialab.healthbutler.domain.Hospital;
import com.mialab.healthbutler.fragment.HospitalDeptFragment;
import com.mialab.healthbutler.fragment.HospitalDesFragment;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class HosDetailActivity extends AppCompatActivity {

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_tab)
    TabLayout tabLayout;
    @BindView(R.id.vp_container)
    ViewPager viewPager;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.iv_background)
    ImageView ivBackround;
    @BindView(R.id.rl_head)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_hos_name)
    TextView tvName;
    @BindView(R.id.tv_hos_level)
    TextView tvLevel;

    List<Fragment> fragments = new ArrayList<Fragment>();
    List<String> tabTiles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hos_detail);
        TranslucentBarsUtils.setTranslucent(this);

        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        final Hospital hospital = (Hospital) intent.getExtras().get("hospital");
        tvName.setText(hospital.getName());
        tvLevel.setText("三甲医院");

        Glide.with(this)
                .load(R.drawable.head)
                .bitmapTransform(new BlurTransformation(this))
                .into(ivBackround);
        Glide.with(this)
                .load(R.drawable.head)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivHead);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.inflateMenu(R.menu.toolbar_menu);

        collapsingToolbarLayout.setTitle(hospital.getName());
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.website) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.jsph.net/"));
                    startActivity(intent);

                } else if (menuItemId == R.id.setting) {
                    Toast.makeText(HosDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -collapsingToolbarLayout.getHeight() / 3 && verticalOffset > -collapsingToolbarLayout.getHeight() / 2) {
                    collapsingToolbarLayout.setTitle(" ");
                    rlHeader.setVisibility(View.INVISIBLE);
                } else if (verticalOffset <= -collapsingToolbarLayout.getHeight() / 2) {
                    collapsingToolbarLayout.setTitle(hospital.getName());
                } else {
                    collapsingToolbarLayout.setTitle(" ");
                    rlHeader.setVisibility(View.VISIBLE);
                }
            }
        });


        fragments.add(new HospitalDesFragment());
        fragments.add(new HospitalDeptFragment());
        tabTiles.add("医院简介");
        tabTiles.add("医院科室");

        HosDetailAdapter adapter = new HosDetailAdapter(getSupportFragmentManager(), fragments, tabTiles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
