package com.mialab.healthbutler.impl.usercenterdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.CheckDiseaseActivity;
import com.mialab.healthbutler.activity.FindDoctorActivity;
import com.mialab.healthbutler.activity.FindHospitalActivity;
import com.mialab.healthbutler.activity.FoodSearchActivity;
import com.mialab.healthbutler.activity.HealthAlertActviity;
import com.mialab.healthbutler.activity.HealthRecordActivity;
import com.mialab.healthbutler.activity.HealthTestActivity;
import com.mialab.healthbutler.activity.KeepFitActivity;
import com.mialab.healthbutler.activity.LandActivity;
import com.mialab.healthbutler.activity.LoginActivity;
import com.mialab.healthbutler.activity.MainActivity;
import com.mialab.healthbutler.activity.MusicActivity;
import com.mialab.healthbutler.activity.RegisterActivity;
import com.mialab.healthbutler.activity.SettingActivity;
import com.mialab.healthbutler.activity.TaskListActivity;
import com.mialab.healthbutler.activity.UserInfActivity;
import com.mialab.healthbutler.activity.WeatherActivity;
import com.mialab.healthbutler.domain.GrandData;
import com.mialab.healthbutler.domain.UserDetailInfo;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * UserCenterDetail的flcontent内容
 *
 * @author Wesly
 */
public class UserCenterDetail implements OnClickListener {

    public View mRootView;
    private MainActivity mainActivity;
    private RelativeLayout rlUser;
    private CircleImageView ivHead;
    private TextView tvName;
    private TextView tvSignature;
    private ImageView ivUpdateInf;
    private GridView gvMenu;
    UserDetailInfo mUserInf;
    private String mToken;
    private GridAdapter mGridAdapter;
    private ArrayList<String> mGridName;
    private int[] mGridPic = {R.drawable.grid_menu_1, R.drawable.grid_menu_2, R.drawable.grid_menu_3,
            R.drawable.grid_menu_4, R.drawable.grid_menu_5, R.drawable.grid_menu_6, R.drawable.doctor,
            R.drawable.hospital, R.drawable.disease, R.drawable.file, R.drawable.encourage, R.drawable.user_center,
            R.drawable.ic_register,R.drawable.ic_keepfit,R.drawable.ic_health_warn
    };
    private ListView lv_center_show;

    public UserCenterDetail(Activity aty) {
        mainActivity = (MainActivity) aty;
        initView();
    }

    private void initView() {
        mRootView = View.inflate(mainActivity, R.layout.detail_usercenter, null);

        rlUser = (RelativeLayout) mRootView.findViewById(R.id.rl_user);
        ivHead = (CircleImageView) mRootView.findViewById(R.id.iv_head);
        tvName = (TextView) mRootView.findViewById(R.id.tv_name);
        tvSignature = (TextView) mRootView.findViewById(R.id.tv_signature);
        ivUpdateInf = (ImageView) mRootView.findViewById(R.id.iv_update_inf);

        gvMenu = (GridView) mRootView.findViewById(R.id.gv_menu);


    }

    public void initData() {

        mToken = PrefUtils.getString(mainActivity, GlobalContants.TOKEN, "");

        String cache = CacheUtils.getCache(mainActivity, GlobalContants.GET_USER_INFO, null);
        if (!TextUtils.isEmpty(cache)) {
            paraseData(cache);
        }


        if (mToken.equals("")) {
            tvName.setText("点击登陆");
            ivUpdateInf.setVisibility(View.INVISIBLE);
        } else {
            getDataFromServer();
        }

        mGridName = new ArrayList<String>();
        mGridName.add("我的任务");
        mGridName.add("天气助手");
        mGridName.add("饮食贴士");
        mGridName.add("健康测评");
        mGridName.add("睡前音乐");
        mGridName.add("应用设置");
        mGridName.add("寻医生");
        mGridName.add("找医院");
        mGridName.add("查疾病");
        mGridName.add("档案管理");
        mGridName.add("自我激励");
        mGridName.add("个人中心");
        mGridName.add("预约挂号");
        mGridName.add("减肥助手");
        mGridName.add("健康提醒");

        mGridAdapter = new GridAdapter();
        gvMenu.setAdapter(mGridAdapter);

        //gridView点击监听
        gvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        if (!TextUtils.isEmpty(mToken)) {
                            intent.setClass(mainActivity, TaskListActivity.class);
                            mainActivity.startActivity(intent);
                        } else {
                            intent.setClass(mainActivity, LoginActivity.class);
                            mainActivity.startActivity(intent);
                        }
                        break;
                    case 1:
                        intent.setClass(mainActivity, WeatherActivity.class);
                        mainActivity.startActivity(intent);
                        break;

                    case 2:
                        intent.setClass(mainActivity, FoodSearchActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(mainActivity, HealthTestActivity.class);
                        mainActivity.startActivity(intent);

                        break;
                    case 4:
                        intent.setClass(mainActivity, MusicActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 5:
                        intent.setClass(mainActivity, SettingActivity.class);
                        if (mainActivity.mStartDownload) {
                            intent.putExtra("mStartDownload", true);
                        } else {
                            intent.putExtra("mStartDownload", false);
                        }
                        mainActivity.startActivity(intent);
                        break;

                    case 6:
                        intent.setClass(mainActivity, FindDoctorActivity.class);
                        mainActivity.startActivity(intent);
                        break;

                    case 7:
                        intent.setClass(mainActivity, FindHospitalActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 8:
                        intent.setClass(mainActivity, CheckDiseaseActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 9:
                        intent.setClass(mainActivity, HealthRecordActivity.class);
                        mainActivity.startActivity(intent);
                        break;
                    case 10:
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                        Date curDate = new Date(System.currentTimeMillis());
                        String str = formatter.format(curDate).substring(0, 11);
                        Bundle data = new Bundle();
                        data.putString("curTime", str);
                        intent.setClass(mainActivity, LandActivity.class);
                        intent.putExtras(data);
                        mainActivity.startActivity(intent);
                        break;

                    case 12:
                        intent.setClass(mainActivity, RegisterActivity.class);
                        mainActivity.startActivity(intent);
                        break;

                    case 13:
                        intent.setClass(mainActivity, KeepFitActivity.class);
                        mainActivity.startActivity(intent);
                        break;

                    case 14:
                        intent.setClass(mainActivity, HealthAlertActviity.class);
                        mainActivity.startActivity(intent);
                        break;

                }
            }
        });
        rlUser.setOnClickListener(this);
        ivUpdateInf.setOnClickListener(this);


   
    }

    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", mToken);
        utils.send(HttpMethod.POST, GlobalContants.GET_USER_INFO, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtils.setCache(mainActivity, GlobalContants.GET_USER_INFO, result);
                paraseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private void paraseData(String result) {
        Gson gson = new Gson();
        mUserInf = gson.fromJson(result, UserDetailInfo.class);
        if (mUserInf != null) {
            tvName.setText(mUserInf.getmUserName());
            if (!TextUtils.isEmpty(mUserInf.getmSignature())) {
                tvSignature.setText(mUserInf.getmSignature());
            }
            BitmapUtils utils = new BitmapUtils(mainActivity);
            if (!TextUtils.isEmpty(mUserInf.getmHeadURL())) {
                utils.display(ivHead, mUserInf.getmHeadURL());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user:
                if (mToken.equals("")) {
                    mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                }
                break;
            case R.id.iv_update_inf:
                mainActivity.startActivity(new Intent(mainActivity, UserInfActivity.class));
                break;
        }

    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mGridName.size();
        }

        @Override
        public String getItem(int position) {
            return mGridName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(mainActivity, R.layout.grid_menu_item, null);
            ImageView ivGrid = (ImageView) view.findViewById(R.id.iv_menu);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ivGrid.setImageResource(mGridPic[position]);
            tvTitle.setText(getItem(position));
            return view;
        }
    }
}
