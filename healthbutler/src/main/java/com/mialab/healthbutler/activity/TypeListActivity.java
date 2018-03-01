package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.mialab.healthbutler.R;
import com.mialab.healthbutler.db.MySqliteHelper;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeListActivity extends Activity {

    private View view;
    private ListView listview;  //任务分类
    private EditText et1;  //编辑分类文本
    private ImageButton ib1;  //确认按钮
    ImageButton ibBack; //返回按钮
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  //存储分类信息
    MySqliteHelper mySqliteHelper = new MySqliteHelper(this, "mydata.db", null, 1);  //mydata.db的数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_type_list);
        TranslucentBarsUtils.setColor(this, getResources().getColor(R.color.primary_2));


        et1 = (EditText) findViewById(R.id.editText4);  //编辑分类文本
        ib1 = (ImageButton) findViewById(R.id.imageButton7);  //确认按钮
        listview = (ListView) findViewById(R.id.listView2);
        ibBack = (ImageButton) findViewById(R.id.iv_back); //返回按钮


        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //确认按钮返回给add_task文本框的值
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = et1.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("type", type);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        //显示分类列表
        listItem = getALLlist();
        SimpleAdapter listadapet = new SimpleAdapter(TypeListActivity.this, listItem, R.layout.list_adapter, new String[]{"image", "task_type"}, new int[]{R.id.iv_task_detail, R.id.tv_task_content});

        listview.setAdapter(listadapet);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap item = (HashMap) parent.getItemAtPosition(position);
                String task_type_choose = String.valueOf(item.get("task_type"));
                et1.setText(task_type_choose);
            }
        });


    }


    //获取数据库的所有不重复的分类信息到List
    public ArrayList<HashMap<String, Object>> getALLlist() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = mySqliteHelper.getReadableDatabase();
        Cursor c = db.query("my_table", new String[]{"_id", "task", "date", "task_type"}, null, null, null, null, null);
        boolean isreapt = false; //重复
        boolean isaddnotsorted = false; //有没有添加未分类选项

        if (c != null) {
            while (c.moveToNext()) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                for (HashMap<String, Object> temp : list) {
                    if (c.getString(c.getColumnIndex("task_type")).equals(temp.get("task_type"))) {
                        isreapt = true;
                        break;
                    }
                }

                if (isreapt == false) {
                    if ((c.getString(c.getColumnIndex("task_type")).equals("")) && (isaddnotsorted == false)) {
                        item.put("_id", c.getInt(c.getColumnIndex("_id")));
                        item.put("task_type", "未分类");
                        item.put("image", R.drawable.type_two);

                        list.add(item);
                        isaddnotsorted = true;
                    } else if (!(c.getString(c.getColumnIndex("task_type")).equals(""))) {
                        item.put("_id", c.getInt(c.getColumnIndex("_id")));
                        item.put("task_type", c.getString(c.getColumnIndex("task_type")));
                        item.put("image", R.drawable.type_two);

                        list.add(item);
                    }
                }
                isreapt = false;
            }
        }

        c.close();
        return list;
    }

}


