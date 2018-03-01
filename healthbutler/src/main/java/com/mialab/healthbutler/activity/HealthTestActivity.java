package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.TaskListClickAdapter;
import com.mialab.healthbutler.adapter.TaskListClickAdapter.*;
import com.mialab.healthbutler.app.ActivityCollector;
import com.mialab.healthbutler.db.MySqliteHelper;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Wesly186 on 2016/3/26.
 */
public class HealthTestActivity extends Activity implements TaskListClickAdapter.ListItemClickListener {

    private ArrayList<View> dots;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private View view0, view1, view2;
    private int oldPosition = 0;// 记录上一次点的位置
    private int currentItem; // 当前页面
    private List<View> viewList = new ArrayList<View>();// 把需要滑动的页卡添加到这个list中
    private ListView list_page2;
    private ListView list_page1;
    ArrayList<String> list_page2_arr;
    ArrayList<String> list_page1_arr;
    ArrayList<HashMap<String, Object>> allList_page = new ArrayList<HashMap<String, Object>>();
    private ListItemClickAdapter madapter;  //
    MySqliteHelper mySqliteHelper = new MySqliteHelper(this, "mydata.db", null, 1);  //mydata.db的数据库
    private ImageView bt_queren;//
    private Spinner sp_high;
    private Spinner sp_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_health_test);
        TranslucentBarsUtils.setTranslucent(this);
        ActivityCollector.addActivity(this);


        initView();

        initData();
    }

    private void initView() {

        // 添加当前Acitivity到ancivitylist里面去，为了方便统一退出
        // 显示的点
        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot_1));
        dots.add(findViewById(R.id.dot_2));
        dots.add(findViewById(R.id.dot_3));
        // 得到viewPager的布局
        LayoutInflater lf = LayoutInflater.from(HealthTestActivity.this);
        view0 = lf.inflate(R.layout.health_test_page1, null);
        view1 = lf.inflate(R.layout.health_test_page2, null);
        view2 = lf.inflate(R.layout.health_test_page3, null);
        viewList.add(view0);
        viewList.add(view1);
        viewList.add(view2);
        // 找到点击进入那个按钮
        mViewPager = (ViewPager) findViewById(R.id.vp);

        list_page1 = (ListView) view1.findViewById(R.id.listView);
        list_page2 = (ListView) view2.findViewById(R.id.listView);
        bt_queren = (ImageView) view2.findViewById(R.id.imageView3);
        sp_high = (Spinner) view0.findViewById(R.id.et_task_detail);
        sp_weight = (Spinner) view0.findViewById(R.id.editText1);


    }

    private void initData() {
        create_testQuestion();


        //确认信息
        bt_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isfill = isallFill();

                if (isfill == 1) {
                    String high = sp_high.getSelectedItem().toString();
                    String weight = sp_weight.getSelectedItem().toString();

                    Intent intent = new Intent(HealthTestActivity.this, HealthShowActivity.class);
                    Bundle data = new Bundle();
                    data.putString("high", high);
                    data.putString("weight", weight);
                    intent.putExtras(data);
                    startActivity(intent);
                    HealthTestActivity.this.finish();

                } else {
                    new AlertDialog.Builder(HealthTestActivity.this)
                            .setTitle("还有未完成的测试！").create().show();
                }


            }
        });


        //page1列表显示
        list_page1_arr = getPageList(1);
        madapter = new ListItemClickAdapter(HealthTestActivity.this, list_page1_arr, this);
        list_page1.setAdapter(madapter);

        //page2列表显示
        list_page2_arr = getPageList(2);
        madapter = new ListItemClickAdapter(HealthTestActivity.this, list_page2_arr, this);
        list_page2.setAdapter(madapter);


        adapter = new ViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        dots.get(0).setBackgroundResource(R.drawable.dot_focused);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {  //0，1，2
                // TODO Auto-generated method stub

                dots.get(oldPosition).setBackgroundResource(
                        R.drawable.dot_normal);
                dots.get(position)
                        .setBackgroundResource(R.drawable.dot_focused);


                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        finish();
    }

    @Override
    public void onItemClick(View item, View widget, int position, int which) {  //view为整个listadapter一行
        SQLiteDatabase db = mySqliteHelper.getWritableDatabase();
        String yes_or_no = "2"; //区分当前点击到哪一个按钮 yes为1，no为0
        Button buton_yes = (Button) item.findViewById(R.id.bt_yes);
        Button buton_no = (Button) item.findViewById(R.id.bt_no);

        ArrayList<HashMap<String, Object>> allList_page = new ArrayList<HashMap<String, Object>>();
        allList_page = getAllList_page(currentItem);

        HashMap one = allList_page.get(position);
        int sql_id = Integer.parseInt(String.valueOf(one.get("_id")));
        ContentValues values = new ContentValues();

        switch (which) {
            case R.id.bt_yes:
                if (yes_or_no.equals("2") || yes_or_no.equals("0")) {

                    buton_yes.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    buton_no.setBackgroundColor(getResources().getColor(R.color.touming));
                    yes_or_no = "1";

                    values.put("data_string", yes_or_no);
                    db.update("my_health", values, "_id=" + String.valueOf(sql_id), null);


                }
                break;

            case R.id.bt_no:

                if (yes_or_no.equals("2") || yes_or_no.equals("1")) {

                    buton_no.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    buton_yes.setBackgroundColor(getResources().getColor(R.color.touming));
                    yes_or_no = "0";


                    values.put("data_string", yes_or_no);
                    db.update("my_health", values, "_id=" + String.valueOf(sql_id), null);

                }
                break;

        }


    }

    private class ViewPagerAdapter extends PagerAdapter {

        public ViewPagerAdapter() {
            super();

            // TODO Auto-generated constructor stub
            // 得到viewpager切换的三个布局，并把它们加入到viewList里面,记得view用打气筒生成，否则容易出现空指针异常

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return viewList.size();
        }

        // 是否是同一张图片
        @Override

        public boolean isViewFromObject(View view, Object object) {
            // TODO Auto-generated method stub
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            ((ViewPager) view).removeView(viewList.get(position));


        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ((ViewPager) view).addView(viewList.get(position));
            return viewList.get(position);
        }
    }


    //创建建立测评问题
    private void create_testQuestion() {
        SQLiteDatabase db = mySqliteHelper.getWritableDatabase();
        Cursor c = db.query("my_health", new String[]{"_id", "test_name", "data_string", "question"}, null, null, null, null, null);
        int count = 0;

        if (c != null) {
            while (c.moveToNext()) {
                count++;
            }

            if (count == 0) {


                //---------------------------------------page2

                ContentValues values3 = new ContentValues();
                values3.put("test_name", "sleep_time");
                values3.put("question", "平均每天睡眠时长是否大于7小时？");
                values3.put("data_string", "2");
                db.insert("my_health", null, values3);

                ContentValues values4 = new ContentValues();
                values4.put("test_name", "work_time");
                values4.put("question", "平均每天工作时长是否大于8小时?");
                values4.put("data_string", "2");
                db.insert("my_health", null, values4);

                ContentValues values5 = new ContentValues();
                values5.put("test_name", "week_times");
                values5.put("question", "每周运动次数是否大于3次?");
                values5.put("data_string", "2");
                db.insert("my_health", null, values5);

                //-------------------------------------------page3

                ContentValues values6 = new ContentValues();
                values6.put("test_name", "water_drink");
                values6.put("question", "平均每天饮水量是否大于等于8杯？");
                values6.put("data_string", "2");
                db.insert("my_health", null, values6);

                ContentValues values7 = new ContentValues();
                values7.put("test_name", "cofe_drink");
                values7.put("question", "是否经常喝咖啡饮酒?");
                values7.put("data_string", "2");
                db.insert("my_health", null, values7);

                ContentValues values8 = new ContentValues();
                values8.put("test_name", "stay_up");
                values8.put("question", "是否经常熬夜？");
                values8.put("data_string", "2");
                db.insert("my_health", null, values8);


            }


        }
    }


    //得到数据表中所有字段数据
    private ArrayList<HashMap<String, Object>> getAllList_page(int i) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();

        switch (i) {
            case 1:
                Cursor c1 = db.query("my_health", new String[]{"_id", "test_name", "data_string", "question"}, null, null, null, null, null);

                if (c1 != null) {
                    while (c1.moveToNext()) {
                        if (c1.getString(c1.getColumnIndex("test_name")).equals("sleep_time")) {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("_id", c1.getInt(c1.getColumnIndex("_id")));
                            item.put("test_name", c1.getString(c1.getColumnIndex("test_name")));
                            item.put("data_string", c1.getString(c1.getColumnIndex("data_string")));
                            item.put("question", c1.getString(c1.getColumnIndex("question")));
                            list.add(item);
                            c1.moveToNext();


                            HashMap<String, Object> item1 = new HashMap<String, Object>();
                            item1.put("_id", c1.getInt(c1.getColumnIndex("_id")));
                            item1.put("test_name", c1.getString(c1.getColumnIndex("test_name")));
                            item1.put("data_string", c1.getString(c1.getColumnIndex("data_string")));
                            item1.put("question", c1.getString(c1.getColumnIndex("question")));
                            list.add(item1);
                            c1.moveToNext();

                            HashMap<String, Object> item2 = new HashMap<String, Object>();
                            item2.put("_id", c1.getInt(c1.getColumnIndex("_id")));
                            item2.put("test_name", c1.getString(c1.getColumnIndex("test_name")));
                            item2.put("data_string", c1.getString(c1.getColumnIndex("data_string")));
                            item2.put("question", c1.getString(c1.getColumnIndex("question")));
                            list.add(item2);

                            c1.close();
                            break;


                        }

                    }
                }

                break;

            case 2:

                Cursor c2 = db.query("my_health", new String[]{"_id", "test_name", "data_string", "question"}, null, null, null, null, null);

                if (c2 != null) {
                    while (c2.moveToNext()) {
                        if (c2.getString(c2.getColumnIndex("test_name")).equals("water_drink")) {
                            HashMap<String, Object> item = new HashMap<String, Object>();
                            item.put("_id", c2.getInt(c2.getColumnIndex("_id")));
                            item.put("test_name", c2.getString(c2.getColumnIndex("test_name")));
                            item.put("data_string", c2.getString(c2.getColumnIndex("data_string")));
                            item.put("question", c2.getString(c2.getColumnIndex("question")));
                            list.add(item);
                            c2.moveToNext();


                            HashMap<String, Object> item1 = new HashMap<String, Object>();
                            item1.put("_id", c2.getInt(c2.getColumnIndex("_id")));
                            item1.put("test_name", c2.getString(c2.getColumnIndex("test_name")));
                            item1.put("data_string", c2.getString(c2.getColumnIndex("data_string")));
                            item1.put("question", c2.getString(c2.getColumnIndex("question")));
                            list.add(item1);
                            c2.moveToNext();

                            HashMap<String, Object> item2 = new HashMap<String, Object>();
                            item2.put("_id", c2.getInt(c2.getColumnIndex("_id")));
                            item2.put("test_name", c2.getString(c2.getColumnIndex("test_name")));
                            item2.put("data_string", c2.getString(c2.getColumnIndex("data_string")));
                            item2.put("question", c2.getString(c2.getColumnIndex("question")));
                            list.add(item2);
                            c2.close();
                            break;

                        }
                    }
                }

                break;

        }

        return list;

    }


    //判断是否测试完毕
    private int isallFill() {
        SQLiteDatabase db = mySqliteHelper.getWritableDatabase();
        int isfull = 1;
        Cursor c1 = db.query("my_health", new String[]{"_id", "test_name", "data_string", "question"}, null, null, null, null, null);

        if (c1 != null) {
            while (c1.moveToNext()) {
                if (c1.getString(c1.getColumnIndex("data_string")).equals("2")) {
                    isfull = 0;
                    break;
                }
            }
        }


        String high = sp_high.getSelectedItem().toString();
        String weight = sp_weight.getSelectedItem().toString();

        if (high.equals("") || weight.equals("")) {
            isfull = 0;
        }


        return isfull;
    }

    //页面的列表显示
    private ArrayList<String> getPageList(int i) {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();

        switch (i) {
            case 1:
                Cursor c1 = db.query("my_health", new String[]{"test_name", "question"}, null, null, null, null, null);
                if (c1 != null) {
                    while (c1.moveToNext()) {
                        if (c1.getString(c1.getColumnIndex("test_name")).equals("sleep_time")) {
                            String item = new String();
                            item = c1.getString(c1.getColumnIndex("question"));
                            arrayList.add(item);
                            c1.moveToNext();


                            item = c1.getString(c1.getColumnIndex("question"));
                            arrayList.add(item);
                            c1.moveToNext();

                            item = c1.getString(c1.getColumnIndex("question"));
                            arrayList.add(item);
                            break;

                        }
                    }
                }

                c1.close();
                break;


            case 2:
                Cursor c2 = db.query("my_health", new String[]{"test_name", "question"}, null, null, null, null, null);
                if (c2 != null) {
                    while (c2.moveToNext()) {
                        if (c2.getString(c2.getColumnIndex("test_name")).equals("water_drink")) {
                            String item = new String();
                            item = c2.getString(c2.getColumnIndex("question"));
                            arrayList.add(item);
                            c2.moveToNext();

                            item = c2.getString(c2.getColumnIndex("question"));
                            arrayList.add(item);
                            c2.moveToNext();

                            item = c2.getString(c2.getColumnIndex("question"));
                            arrayList.add(item);
                            break;

                        }
                    }
                }

                c2.close();
                break;
        }

        return arrayList;
    }


    class ListItemClickAdapter extends BaseAdapter {
        private Context contxet;
        public ListItemClickListener callback;
        private ArrayList<String> list;
        private LayoutInflater mInflater;

        public ListItemClickAdapter(Context contxet, ArrayList<String> list,
                                    ListItemClickListener callback) {
            this.contxet = contxet;
            this.list = list;
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            mInflater = (LayoutInflater) contxet
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.health_test_item, null);
                holder = new ViewHolder();
                holder.mAd_tv_show = (TextView) convertView
                        .findViewById(R.id.textView);
                holder.mAd_btn_one = (Button) convertView
                        .findViewById(R.id.bt_yes);
                holder.mAd_btn_two = (Button) convertView
                        .findViewById(R.id.bt_no);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mAd_tv_show.setText(list.get(position));

            final View view = convertView;
            final int p = position;
            final int one = holder.mAd_btn_one.getId();
            holder.mAd_btn_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemClick(view, parent, p, one);
                }
            });
            final int two = holder.mAd_btn_two.getId();
            holder.mAd_btn_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemClick(view, parent, p, two);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView mAd_tv_show;
            Button mAd_btn_one;
            Button mAd_btn_two;


        }

    }


}


