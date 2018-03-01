package com.mialab.healthbutler.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;


import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.CompleteTaskListClickAdapter;
import com.mialab.healthbutler.adapter.TaskListClickAdapter;
import com.mialab.healthbutler.domain.TaskList;
import com.mialab.healthbutler.domain.TaskList.Task;
import com.mialab.healthbutler.global.GlobalContants;
import com.mialab.healthbutler.utils.CacheUtils;
import com.mialab.healthbutler.utils.PrefUtils;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.util.ArrayList;


public class TaskListActivity extends Activity implements TaskListClickAdapter.ListItemClickListener, View.OnClickListener {

    private static final int TASK_NOW = 1;
    private static final int TASK_COMPLETE = 2;
    private static final int ADD_TASK_ACTIVITY = 3;
    private static final int TASK_DETAIL_ACTIVITY = 4;

    private ImageView ivBack;
    private Button btnTaskNow;
    private Button btnTaskComplete;
    private ImageView ivAddTask;
    private ListView lvTaskNow;
    private ListView lvTaskComplete;

    private String mToken;
    private int mNowSelected = TASK_NOW;

    private TaskList mTask;
    private ArrayList<Task> mNowTaskList;
    private ArrayList<Task> mCompleteTaskList;
    private TaskListClickAdapter mAdapterNow;
    private CompleteTaskListClickAdapter mAdapterComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_list);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.tasklistactivity));

        initView();

        initData();

    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btnTaskNow = (Button) findViewById(R.id.btn_task_now);
        btnTaskComplete = (Button) findViewById(R.id.btn_task_complete);
        ivAddTask = (ImageView) findViewById(R.id.iv_task_detail);
        lvTaskNow = (ListView) findViewById(R.id.lv_task);
        lvTaskComplete = (ListView) findViewById(R.id.lv_task_complete);
    }

    private void initData() {

        //获取用户id
        mToken = PrefUtils.getString(this, GlobalContants.TOKEN, "");
        //解析缓存
        if (!TextUtils.isEmpty(mToken)) {
            String cache = CacheUtils.getCache(this, GlobalContants.GET_TASK_LIST, "");
            if (!TextUtils.isEmpty(cache)) {
                parseData(cache);
            }

            getDataFromServer();
        }
        //返回
        ivBack.setOnClickListener(this);
        //显示未完成
        btnTaskNow.setOnClickListener(this);
        // 显示完成
        btnTaskComplete.setOnClickListener(this);
        //添加task
        ivAddTask.setOnClickListener(this);

    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("UUID", mToken);

        utils.send(HttpRequest.HttpMethod.POST, GlobalContants.GET_TASK_LIST, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.e("RESULT", result);
                parseData(result);
                CacheUtils.setCache(TaskListActivity.this, GlobalContants.GET_TASK_LIST, result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("RESULT", "error");
            }
        });
    }

    private void parseData(String cache) {
        Gson gson = new Gson();
        mTask = gson.fromJson(cache, TaskList.class);
        if (mTask.mUncompleteTaskList != null) {
            mNowTaskList = mTask.mUncompleteTaskList;
            mAdapterNow = new TaskListClickAdapter(this, mNowTaskList, this);
            lvTaskNow.setAdapter(mAdapterNow);
            if (mTask.mCompleteTaskList != null) {
                mCompleteTaskList = mTask.mCompleteTaskList;
                mAdapterComplete = new CompleteTaskListClickAdapter(this, mCompleteTaskList, this);
                lvTaskComplete.setAdapter(mAdapterComplete);
            }
        }
    }

    @Override
    public void onItemClick(View item, View widget, final int position, int which) {
        final Gson gson = new Gson();
        String newCache;
        if (mNowSelected == TASK_NOW) {
            switch (which) {
                case R.id.iv_complete:
                    //跟新List
                    Task taskComplete = mNowTaskList.get(position);
                    mNowTaskList.remove(position);
                    mCompleteTaskList.add(taskComplete);
                    mAdapterNow.notifyDataSetChanged();
                    mAdapterComplete.notifyDataSetChanged();
                    //更新缓存
                    mTask.mUncompleteTaskList = mNowTaskList;
                    mTask.mCompleteTaskList = mCompleteTaskList;
                    newCache = gson.toJson(mTask);
                    CacheUtils.setCache(TaskListActivity.this, GlobalContants.GET_TASK_LIST, newCache);
                    //提交数据
                    postData2Server(taskComplete.getmId(), false);
                    break;
                case R.id.iv_task_detail:
                    Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("task", mNowTaskList.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, TASK_DETAIL_ACTIVITY);
                    break;
            }
        } else {
            switch (which) {
                case R.id.iv_not_complete:
                    //跟新List
                    Task taskNotComplete = mCompleteTaskList.get(position);
                    mNowTaskList.add(taskNotComplete);
                    mCompleteTaskList.remove(position);
                    mAdapterNow.notifyDataSetChanged();
                    mAdapterComplete.notifyDataSetChanged();
                    //更新缓存
                    mTask.mUncompleteTaskList = mNowTaskList;
                    mTask.mCompleteTaskList = mCompleteTaskList;
                    newCache = gson.toJson(mTask);
                    CacheUtils.setCache(TaskListActivity.this, GlobalContants.GET_TASK_LIST, newCache);
                    //提交数据
                    postData2Server(taskNotComplete.getmId(), false);
                    break;
                case R.id.iv_done_delete:
                    new AlertDialog.Builder(TaskListActivity.this)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跟新List
                                    Task taskNotComplete = mCompleteTaskList.get(position);
                                    mCompleteTaskList.remove(position);
                                    mAdapterComplete.notifyDataSetChanged();
                                    //更新缓存
                                    mTask.mCompleteTaskList = mCompleteTaskList;
                                    String newCache = gson.toJson(mTask);
                                    CacheUtils.setCache(TaskListActivity.this, GlobalContants.GET_TASK_LIST, newCache);
                                    //提交数据
                                    deleteFromServer(taskNotComplete.getmId());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("是否要删除该任务?").create().show();
                    break;
            }
        }
    }

    private void deleteFromServer(int id) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("ID", id + "");

        utils.send(HttpRequest.HttpMethod.POST, GlobalContants.DELETE_TASK, params, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private void postData2Server(int id, boolean isComplete) {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        //params.addBodyParameter("UUID", mToken);
        params.addBodyParameter("ID", id + "");
        //params.addBodyParameter("isComplete", isComplete + "");

        utils.send(HttpRequest.HttpMethod.POST, GlobalContants.EXCHANGE_TASK, params, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                Log.e("exchange", "success");
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("exchange", "failure");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_TASK_ACTIVITY) {
                String cache = CacheUtils.getCache(this, GlobalContants.GET_TASK_LIST, "");
                if (!TextUtils.isEmpty(cache)) {
                    parseData(cache);
                }
            } else if (requestCode == TASK_DETAIL_ACTIVITY) {
                String cache = CacheUtils.getCache(this, GlobalContants.GET_TASK_LIST, "");
                if (!TextUtils.isEmpty(cache)) {
                    parseData(cache);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_task_now:
                //未完成列表显示
                lvTaskNow.setVisibility(View.VISIBLE);
                lvTaskComplete.setVisibility(View.INVISIBLE);
                btnTaskNow.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                btnTaskComplete.setBackgroundColor(getResources().getColor(R.color.touming));
                //设置当前状态
                mNowSelected = TASK_NOW;
                break;
            case R.id.btn_task_complete:
                //完成列表显示
                lvTaskNow.setVisibility(View.INVISIBLE);
                lvTaskComplete.setVisibility(View.VISIBLE);
                btnTaskComplete.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                btnTaskNow.setBackgroundColor(getResources().getColor(R.color.touming));
                //设置当前状态
                mNowSelected = TASK_COMPLETE;
                break;
            case R.id.iv_task_detail:
                Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_ACTIVITY);
                break;
        }
    }
}
