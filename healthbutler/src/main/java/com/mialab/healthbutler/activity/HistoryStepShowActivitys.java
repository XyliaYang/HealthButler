package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.stepListViewAdapter;
import com.mialab.healthbutler.db.DBHelper;
import com.mialab.healthbutler.domain.SportRecords;
import com.mialab.healthbutler.utils.EveryDaySportRecords;
import com.mialab.healthbutler.utils.FormatsUtils;
import com.mialab.healthbutler.utils.TopSytle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class HistoryStepShowActivitys extends Activity implements OnClickListener, OnItemClickListener {

    private TextView tv_sport;
    private View histroyView;
    private Button btn_back;
    private int gonetimes = 0;

    private ListView lv_steprecord;

    private List recordList;
    //路线集合
    private List<SportRecords> sportList;
    //数据库
    private DBHelper dbHelper;
    TextView tv_distance, tv_calorie, tv_stepcount;

    public static int sevendayStep[] = {0, 0, 0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.stepcount_view);
        TopSytle.setColor(HistoryStepShowActivitys.this, Color.rgb(51, 195, 201));

        //数据库
        dbHelper = new DBHelper(HistoryStepShowActivitys.this);
        sportList = new ArrayList<SportRecords>();

        tv_sport = (TextView) findViewById(R.id.tv_sportstring);
        histroyView = (View) findViewById(R.id.history_view);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        tv_sport.setOnClickListener(this);
        tv_calorie = (TextView) findViewById(R.id.tv_caloria);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_stepcount = (TextView) findViewById(R.id.tv_stepcount);


        //列表查询运动记录(线路)
        lv_steprecord = (ListView) findViewById(R.id.lv_steprecord);
        sportList = dbHelper.getAllSportRecord();
        stepListViewAdapter adapter = new stepListViewAdapter(HistoryStepShowActivitys.this, sportList);
        lv_steprecord.setAdapter(adapter);
        lv_steprecord.setOnItemClickListener(this);

        //查询七日记录
        DBHelper dbHelper = new DBHelper(HistoryStepShowActivitys.this);

        Double sevendayCalorie = 0.0;
        Double sevendayDistance = 0.0;
        int sevendaySteps = 0;
        for (int i = -7; i < 0; i++) {
            Date date = new Date();// 取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);

            EveryDaySportRecords records = new EveryDaySportRecords();
            records = dbHelper.selectOneDaySportRecordByDate(dateString);
            if (records == null) {
                sevendayStep[i + 7] = 0;
                System.out.println("没有取到数据" + dateString);
            } else {
                sevendayStep[i + 7] = Integer.parseInt(records.getStepcount());
                sevendaySteps = sevendaySteps + Integer.parseInt(records.getStepcount());
                sevendayCalorie = sevendayCalorie + Double.parseDouble(records.getCalorie());
                sevendayDistance = sevendayDistance + Double.parseDouble(records.getDistance());
                Toast.makeText(HistoryStepShowActivitys.this, i + ":" + records.getCalorie() + "", Toast.LENGTH_SHORT).show();
                System.out.println("取到了数据");
            }
        }
        tv_distance.setText(FormatsUtils.formatDouble(sevendayDistance / 1000, HistoryStepShowActivitys.this));
        tv_calorie.setText(FormatsUtils.formatDouble(sevendayCalorie, HistoryStepShowActivitys.this));
        tv_stepcount.setText(sevendaySteps + "");

    }

    @Override
    public void onClick(View v) {
        if (tv_sport.getId() == v.getId()) {
            gonetimes++;
            if (gonetimes % 2 == 1) {
                histroyView.setVisibility(View.GONE);
            } else {
                histroyView.setVisibility(View.VISIBLE);
            }
        } else if (btn_back.getId() == v.getId()) {
            HistoryStepShowActivitys.this.finish();
        }
    }

    /*
     * 每个列表的单击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(HistoryStepShowActivitys.this, ShowRecordline.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sportrecords", sportList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
