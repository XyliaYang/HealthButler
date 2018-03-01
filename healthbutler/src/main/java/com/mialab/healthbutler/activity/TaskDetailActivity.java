package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.TaskList;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.DateTimePickDialogUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskDetailActivity extends Activity implements View.OnClickListener {

    private static final int REMARK_ACTIVITY = 1;
    private static final int TYPE_LIST_ACTIVITY = 2;

    LinearLayout llType;    //分类按钮
    LinearLayout llDate;       //日期选择按钮
    LinearLayout llAlarm;      //设置提醒按钮
    LinearLayout llRemark;     //备注按钮
    EditText etContent; //任务文本框
    TextView tvDate; //日期文本框
    TextView tvType; //任务类型文本框
    TextView tvAlarm; //周期文本框
    TextView tvRemark; //备注文本框
    Button btnModify; //修改按钮&&保存按钮
    LinearLayout repeatLayout;
    ImageView ivComplete;//删除按钮

    ImageButton ibBack; //返回主界面

    String mCycleTime;
    private AlertDialog mDialog;  //重复周期设置对话框
    boolean isread = true; //是否在阅读状态

    TaskList list = new TaskList();
    TaskList.Task task = list.new Task();
    private String initEndDateTime = "2016年2月22日 17:44"; // 初始化结束时间

    private AlarmManager mAlarm; //定时器
    private Calendar c = Calendar.getInstance();

    private TaskList mTask;
    private ArrayList<TaskList.Task> mNowTaskList;

    Spinner spAlarm; //选择周期对话框  数目
    Spinner spAlarmUnit; //             周期单位


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REMARK_ACTIVITY) {
                String result = data.getStringExtra("remark");
                tvRemark.setText(result);
            } else if (requestCode == TYPE_LIST_ACTIVITY) {
                String type = data.getStringExtra("type");
                tvType.setText(type);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_detail);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.primary_2));

        initView();

        initData();


        /*final SQLiteDatabase db = mySqliteHelper.getWritableDatabase();

        //final Intent intent = getIntent();
        String tasktext = intent.getStringExtra("task");
        String datetext = intent.getStringExtra("date");
        String task_typetext = intent.getStringExtra("task_type");
        String cycle_time = intent.getStringExtra("cycle_time");
        final String remark = intent.getStringExtra("RemarkActivity");

        final int id = intent.getExtras().getInt("_id");

        setText(tasktext, datetext, task_typetext, cycle_time, remark);
        setDisable();
        setColortoWhite();

        //备注点击按钮
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(TaskDetailActivity.this, RemarkActivity.class);
                Bundle data = new Bundle();
                data.putString("RemarkActivity", remark);
                intent1.putExtras(data);
                startActivityForResult(intent1, 1);


            }
        });

        //删除&&保存按钮
        ivComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isread) {   //阅读状态可删除
                    new AlertDialog.Builder(TaskDetailActivity.this)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.delete("my_table", "_id=" + String.valueOf(id), null);

                                    Intent intent1 = new Intent(TaskDetailActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setTitle("是否要删除").create().show();

                } else {  //修改状态可以保存

                    ContentValues values = new ContentValues();
                    String task_text = etContent.getText().toString();
                    String date_text = tvDate.getText().toString();
                    String task_type_text = tvType.getText().toString();
                    String cycle_timetext = tvAlarm.getText().toString();
                    String remarktext = tvRemark.getText().toString();

                    values.put("task", task_text);
                    values.put("date", date_text);
                    values.put("task_type", task_type_text);
                    values.put("cycle_time", cycle_timetext);
                    values.put("RemarkActivity", remarktext);

                    db.update("my_table", values, "_id=" + String.valueOf(id), null);


                    if (!(date_text.equals(""))) {

                        String year = date_text.substring(0, 4);
                        String month = date_text.substring(5, 7);
                        String day = date_text.substring(8, 10);
                        String hour = date_text.substring(12, 14);
                        String minute = date_text.substring(15, 17);

                        int year_int = Integer.parseInt(year);
                        int month_int = Integer.parseInt(month);
                        int day_int = Integer.parseInt(day);
                        int hour_int = Integer.parseInt(hour);
                        int minute_int = Integer.parseInt(minute);


                        c.set(Calendar.YEAR, year_int);
                        c.set(Calendar.MONTH, month_int - 1);//也可以填数字，0-11,一月为0
                        c.set(Calendar.DAY_OF_MONTH, day_int);
                        c.set(Calendar.HOUR_OF_DAY, hour_int);
                        c.set(Calendar.MINUTE, minute_int);
                        c.set(Calendar.SECOND, 0);


                        mAlarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                        Intent intent = new Intent(TaskDetailActivity.this, MyReceiver.class);
                        Bundle data = new Bundle();
                        data.putString("task", task_text);
                        data.putString("date", date_text);
                        intent.putExtras(data);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(TaskDetailActivity.this, id, intent, 0);


                        if (cycle_timetext.equals("")) {
                            mAlarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);


                        } else {
                            String number_ofCycle = cycle_timetext.substring(1, 2);
                            String unit_ofCycle = cycle_timetext.substring(3);

                            int number_int = Integer.parseInt(number_ofCycle);
                            long cycle_int;
                            if (unit_ofCycle.equals("分"))
                                cycle_int = number_int * 60000;
                            else if (unit_ofCycle.equals("时"))
                                cycle_int = number_int * 3600000;
                            else if (unit_ofCycle.equals("天"))
                                cycle_int = number_int * 3600000 * 24;
                            else if (unit_ofCycle.equals("周"))
                                cycle_int = number_int * 3600000 * 24 * 7;
                            else
                                cycle_int = number_int * 3600000 * 24 * 30;

                            Log.d("有重复提醒周期", cycle_timetext + "");

                            mAlarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), cycle_int, pendingIntent);


                        }

                    }


                    setDisable();
                    setColortoWhite();
                    isread = true;
                    ivComplete.setBackgroundResource(R.drawable.lajitong_2);

                }


            }
        });*/


    }

    private void initView() {
        repeatLayout = (LinearLayout) TaskDetailActivity.this.getLayoutInflater().inflate(R.layout.dialog_set_alarm, null);   //自定义周期界面

        ibBack = (ImageButton) findViewById(R.id.iv_back);
        llType = (LinearLayout) findViewById(R.id.ll_set_type); //分类按钮
        llDate = (LinearLayout) findViewById(R.id.ll_set_date);  //日期选择按钮
        llAlarm = (LinearLayout) findViewById(R.id.ll_set_alarm); //提醒按钮
        llRemark = (LinearLayout) findViewById(R.id.ll_set_remark);  //备注按钮
        etContent = (EditText) findViewById(R.id.et_task_detail);
        tvDate = (TextView) findViewById(R.id.tv_set_date);
        tvType = (TextView) findViewById(R.id.tv_set_type);
        tvAlarm = (TextView) findViewById(R.id.tv_set_alarm);
        tvRemark = (TextView) findViewById(R.id.tv_set_remark);
        btnModify = (Button) findViewById(R.id.btn_modify);
        ivComplete = (ImageView) findViewById(R.id.iv_complete);
        spAlarm = (Spinner) repeatLayout.findViewById(R.id.spinner2);
        spAlarmUnit = (Spinner) repeatLayout.findViewById(R.id.spinner3);
    }

    private void initData() {
        setDisable();
        Bundle bundle = getIntent().getExtras();
        task = (TaskList.Task) bundle.getSerializable("task");
        String content = task.getmTaskDetail();
        String date = task.getmTaskDate();
        int time = task.getmRemindTime();
        int unit = task.getmRemindUnit();
        String type = task.getmTaskType();
        String remarks = task.getmRemarks();
        String cycleTime;
        if (unit == 3) {
            cycleTime = "每" + time + "分";
        } else if (unit == 4) {
            cycleTime = "每" + time + "时";
        } else if (unit == 5) {
            cycleTime = "每" + time + "天";
        } else if (unit == 6) {
            cycleTime = "每" + time + "周";
        } else {
            cycleTime = "每" + time + "月";
        }
        setText(content, date, type, cycleTime, remarks);

        //返回主界面
        ibBack.setOnClickListener(this);
        ivComplete.setOnClickListener(this);
        //日期选择按钮
        llDate.setOnClickListener(this);
        //提醒周期
        llAlarm.setOnClickListener(this);
        //分类按钮
        llType.setOnClickListener(this);
        //备注按钮
        llRemark.setOnClickListener(this);
        //修改
        btnModify.setOnClickListener(this);
    }

    //设置文本框的显示内容
    public void setText(String task, String date, String task_type, String cycle_time, String remark) {
        etContent.setText(task);
        tvDate.setText(date);
        tvType.setText(task_type);
        tvAlarm.setText(cycle_time);
        tvRemark.setText(remark);
    }

    //设置文本框不可编辑&&按钮不可点击
    public void setDisable() {
        etContent.setEnabled(false);
        llDate.setEnabled(false);
        llAlarm.setEnabled(false);
        llType.setEnabled(false);
        llRemark.setEnabled(false);
    }

    //设置文本框可编辑&&按钮可点击
    public void setAble() {
        etContent.setEnabled(true);
        llDate.setEnabled(true);
        llAlarm.setEnabled(true);
        llType.setEnabled(true);
        llRemark.setEnabled(true);
    }


    //设置字体颜色为黑色
    public void setColortoBlack() {
        etContent.setTextColor(getResources().getColor(R.color.black));
        tvDate.setTextColor(getResources().getColor(R.color.black));
        tvType.setTextColor(getResources().getColor(R.color.black));
        tvAlarm.setTextColor(getResources().getColor(R.color.black));
        tvRemark.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_complete:
                if (isread) {
                    setResult(RESULT_OK);
                    deleteTask();
                    finish();
                }
                break;
            case R.id.ll_set_date:
                DateTimePickDialogUtils dateTimePicKDialog = new DateTimePickDialogUtils(
                        TaskDetailActivity.this, initEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(tvDate);
                break;
            case R.id.ll_set_alarm:
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(TaskDetailActivity.this)
                            .setTitle("设置重复周期")
                            .setView(repeatLayout)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    mCycleTime = "每" + spAlarm.getSelectedItem().toString() + spAlarmUnit.getSelectedItem().toString();
                                    tvAlarm.setText(mCycleTime);
                                    mDialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }).create();
                }
                mDialog.show();
                break;
            case R.id.ll_set_type:
                Intent intent = new Intent(TaskDetailActivity.this, TypeListActivity.class);
                startActivityForResult(intent, TYPE_LIST_ACTIVITY);
                break;

            case R.id.ll_set_remark:
                Intent i = new Intent(TaskDetailActivity.this, RemarkActivity.class);
                startActivityForResult(i, REMARK_ACTIVITY);
                break;
            case R.id.btn_modify:
                if (isread)   //阅读状态
                {
                    setAble();
                    setColortoBlack();
                    isread = false;
                    ivComplete.setBackgroundResource(R.drawable.after_bianji_queren);
                }
                break;
        }
    }

    private void parseData(String cache) {
        Gson gson = new Gson();
        mTask = gson.fromJson(cache, TaskList.class);
        mNowTaskList = mTask.mUncompleteTaskList;
    }

    //更新缓存
    private void updateCache() {
        String cache = CacheUtils.getCache(this, GlobalContants.GET_TASK_LIST, "");
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        for (int i = 0; i < mNowTaskList.size(); i++) {
            if (mNowTaskList.get(i).getmId() == task.getmId()) {
                mNowTaskList.remove(i);
                break;
            }
        }
        mTask.mUncompleteTaskList = mNowTaskList;
        Gson gson = new Gson();
        String newCache = gson.toJson(mTask);
        CacheUtils.setCache(this, GlobalContants.GET_TASK_LIST, newCache);
    }

    private void deleteTask() {
        //更新缓存
        updateCache();
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("ID", task.getmId() + "");

        utils.send(HttpRequest.HttpMethod.POST, GlobalContants.DELETE_TASK, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }
}


