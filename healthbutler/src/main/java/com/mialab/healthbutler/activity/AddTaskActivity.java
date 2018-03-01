package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mialab.healthbutler.receiver.MyReceiver;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.DateTimePickDialogUtils;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends Activity implements View.OnClickListener {

    private static final int REMARK_ACTIVITY = 1;
    private static final int TYPE_LIST_ACTIVITY = 2;
    private static final int MINUTE = 3;
    private static final int HOUR = 4;
    private static final int DAY = 5;
    private static final int WEEK = 6;
    private static final int MONTH = 7;

    ImageButton ivBack;     //回到主界面按钮
    ImageView ivComplete;   //确认按钮
    EditText etTaskContent;  //任务文本框
    TextView tvType;        //分类文本框
    TextView tvDate;        //日期文本框
    TextView tvAlarm;       //重复周期文本框
    TextView tvRemark;      //备注文本框
    LinearLayout llType;    //分类按钮
    LinearLayout llDate;       //日期选择按钮
    LinearLayout llAlarm;      //设置提醒按钮
    LinearLayout llRemark;     //备注按钮
    Spinner spAlarm;        //选择周期对话框  数目
    Spinner spAlarmUnit;    //周期单位
    LinearLayout repeatLayout;
    private AlertDialog mDialog;  //重复周期设置对话框

    SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private Calendar calendar = Calendar.getInstance();
    Date mCurDate = new Date(System.currentTimeMillis());
    String mInitEndDateTime = mFormatter.format(mCurDate);  // 初始化点击开始时间
    private AlarmManager mAlarm; //定时器

    String mCycleTime;
    private int mAlarmId = 0; //区别不同的任务id


    private String mToken;   //？？
    private TaskList mTask;
    private ArrayList<TaskList.Task> mNowTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_task);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.addtaskactivity));

        initView();

        initDate();
    }

    private void initView() {
        repeatLayout = (LinearLayout) AddTaskActivity.this.getLayoutInflater().inflate(R.layout.dialog_set_alarm, null);   //自定义周期界面

        ivBack = (ImageButton) findViewById(R.id.iv_back);  //回到主界面按钮
        ivComplete = (ImageView) findViewById(R.id.btn_modify);  //确认按钮
        tvType = (TextView) findViewById(R.id.tv_set_type);  //分类文本框
        tvRemark = (TextView) findViewById(R.id.tv_set_remark);//备注文本框
        tvDate = (TextView) findViewById(R.id.tv_set_date); //日期文本框
        etTaskContent = (EditText) findViewById(R.id.et_task_detail); //任务文本框
        tvAlarm = (TextView) findViewById(R.id.tv_set_alarm);  //重复周期文本框
        llType = (LinearLayout) findViewById(R.id.ll_set_type); //分类按钮
        llDate = (LinearLayout) findViewById(R.id.ll_set_date);  //日期选择按钮
        llAlarm = (LinearLayout) findViewById(R.id.ll_set_alarm); //提醒按钮
        llRemark = (LinearLayout) findViewById(R.id.ll_set_remark);  //备注按钮
        spAlarm = (Spinner) repeatLayout.findViewById(R.id.spinner2);
        spAlarmUnit = (Spinner) repeatLayout.findViewById(R.id.spinner3);
    }

    private void initDate() {
        mAlarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        mToken = PrefUtils.getString(this, GlobalContants.TOKEN, "");
        String cache = CacheUtils.getCache(this, GlobalContants.GET_TASK_LIST, "");
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        //回主界面
        ivBack.setOnClickListener(this);
        //保存到数据库并返回主界面
        ivComplete.setOnClickListener(this);
        //日期选择按钮
        llDate.setOnClickListener(this);
        //提醒周期
        llAlarm.setOnClickListener(this);
        //分类按钮
        llType.setOnClickListener(this);
        //备注按钮
        llRemark.setOnClickListener(this);
    }

    private void parseData(String cache) {
        Gson gson = new Gson();
        mTask = gson.fromJson(cache, TaskList.class);
        mNowTaskList = mTask.mUncompleteTaskList;
    }

    //更新缓存
    private void updateCache(String taskContent, String taskDate, int alarmTime, int alarmUnit, String taskType, String taskRemark) {
        TaskList list = new TaskList();
        TaskList.Task task = list.new Task();
        task.setmTaskDetail(taskContent);
        task.setmTaskDate(taskDate);
        task.setmRemindTime(alarmTime);
        task.setmRemindUnit(alarmUnit);
        task.setmTaskType(taskType);
        task.setmRemarks(taskRemark);
        mNowTaskList.add(task);
        mTask.mUncompleteTaskList = mNowTaskList;
        Gson gson = new Gson();
        String cache = gson.toJson(mTask);
        CacheUtils.setCache(this, GlobalContants.GET_TASK_LIST, cache);
    }

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_modify:
                String taskContent = etTaskContent.getText().toString();
                if (TextUtils.isEmpty(taskContent)) {
                    Toast.makeText(AddTaskActivity.this, "不能添加空任务", Toast.LENGTH_SHORT).show();
                } else {                                            //每次创建完成任务之后需要执行提醒的广播
                    String taskDate = tvDate.getText().toString();
                    String taskAlarm = tvAlarm.getText().toString();
                    String taskType = tvType.getText().toString();
                    String taskRemark = tvRemark.getText().toString();
                    int alarmTime;
                    int alarmUnit;
                    if (!TextUtils.isEmpty(taskDate)) {

                        String year = taskDate.substring(0, 4);
                        String month = taskDate.substring(5, 7);
                        String day = taskDate.substring(8, 10);
                        String hour = taskDate.substring(12, 14);
                        String minute = taskDate.substring(15, 17);

                        int year_int = Integer.parseInt(year);
                        int month_int = Integer.parseInt(month);
                        int day_int = Integer.parseInt(day);
                        int hour_int = Integer.parseInt(hour);
                        int minute_int = Integer.parseInt(minute);


                        calendar.set(Calendar.YEAR, year_int);
                        calendar.set(Calendar.MONTH, month_int - 1);//也可以填数字，0-11,一月为0
                        calendar.set(Calendar.DAY_OF_MONTH, day_int);
                        calendar.set(Calendar.HOUR_OF_DAY, hour_int);
                        calendar.set(Calendar.MINUTE, minute_int);
                        calendar.set(Calendar.SECOND, 0);


                        Intent intent = new Intent(AddTaskActivity.this, MyReceiver.class);
                        intent.putExtra("task", taskContent);
                        intent.putExtra("date", taskDate);

                        long cycle_int;
                        alarmTime = Integer.parseInt(taskAlarm.substring(1, 3));
                        if (taskAlarm.substring(3).equals("分")) {
                            alarmUnit = MINUTE;
                            cycle_int = alarmTime * 60000;
                        } else if (taskAlarm.substring(3).equals("时")) {
                            alarmUnit = HOUR;
                            cycle_int = alarmTime * 3600000;
                        } else if (taskAlarm.substring(3).equals("天")) {
                            alarmUnit = DAY;
                            cycle_int = alarmTime * 3600000 * 24;
                        } else if (taskAlarm.substring(3).equals("周")) {
                            alarmUnit = WEEK;
                            cycle_int = alarmTime * 3600000 * 24 * 7;
                        } else {
                            alarmUnit = MONTH;
                            cycle_int = alarmTime * 3600000 * 24 * 30;
                        }
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTaskActivity.this, mAlarmId, intent, 0);
                        if (taskAlarm.equals("")) {
                            mAlarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


                        } else {
                            mAlarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), cycle_int, pendingIntent);

                        }


                    } else {
                        alarmTime = -1;
                        alarmUnit = -1;
                    }
                    //更新缓存
                    updateCache(taskContent, taskDate, alarmTime, alarmUnit, taskType, taskRemark);
                    //提交数据
                    postData2Server(taskContent, taskDate, alarmTime, alarmUnit, taskType, taskRemark);
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.ll_set_date:
                DateTimePickDialogUtils dateTimePicKDialog = new DateTimePickDialogUtils(
                        AddTaskActivity.this, mInitEndDateTime);
                dateTimePicKDialog.dateTimePicKDialog(tvDate);
                break;
            case R.id.ll_set_alarm:
                if (mDialog == null) {
                    mDialog = new AlertDialog.Builder(AddTaskActivity.this)
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
                Intent intent = new Intent(AddTaskActivity.this, TypeListActivity.class);
                startActivityForResult(intent, TYPE_LIST_ACTIVITY);
                break;

            case R.id.ll_set_remark:
                Intent i = new Intent(AddTaskActivity.this, RemarkActivity.class);
                startActivityForResult(i, REMARK_ACTIVITY);
                break;

        }
    }

    private void postData2Server(String taskContent, String taskDate, int alarmTime, int alarmUnit, String taskType, String taskRemark) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", mToken);
        params.addBodyParameter("isComplete", false + "");
        params.addBodyParameter("mTaskDetail", taskContent);
        params.addBodyParameter("mTaskDate", taskDate);
        params.addBodyParameter("mRemindTime", alarmTime + "");
        params.addBodyParameter("mRemindUnit", alarmUnit + "");
        params.addBodyParameter("mTaskType", taskType);
        params.addBodyParameter("mRemarks", taskRemark);

        utils.send(HttpRequest.HttpMethod.POST, GlobalContants.ADD_TASK, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(AddTaskActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(AddTaskActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
