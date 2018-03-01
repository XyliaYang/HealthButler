package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mialab.healthbutler.utils.TopSytles;
import com.mialab.healthbutler.R;

/**
 * Created by LFZ on 2016/6/7.
 */
public class HealthRecordActivity extends Activity implements View.OnClickListener {

    private ImageView img_back, img_editer;
    private RelativeLayout rl_name,diagnose_layout;
    private Boolean Editable = false;
    private Button btn_save;
    private TextView tv_name;
    EditText et_editname;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_health_recods);

        TopSytles.setColor(this, Color.rgb(0, 187, 156));

        img_back = (ImageView) findViewById(R.id.img_back);
        img_editer = (ImageView) findViewById(R.id.img_edit_record);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        diagnose_layout=(RelativeLayout)findViewById(R.id.diagnose_layout);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_name= (TextView)findViewById(R.id.tv_name);
        img_back.setOnClickListener(this);
        img_editer.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        diagnose_layout.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (img_back.getId() == v.getId()) {
            finish();
        } else if (img_editer.getId() == v.getId()) {
            changeEditable();
        } else if (btn_save.getId() == v.getId()) {
            changeEditable();
        } else if (rl_name.getId() == v.getId()) {
            view = LayoutInflater.from(HealthRecordActivity.this).inflate(R.layout.view_name, null,false);
            //用户名
            new AlertDialog.Builder(this).setTitle("用户名").setView(view).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_editname = (EditText) view.findViewById(R.id.et_name);
                    String name = et_editname.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        tv_name.setText(name);
                    }
                }
            }).setPositiveButton("取消", null).show();
        }else if(diagnose_layout.getId()==v.getId()){
            startActivity(new Intent(this,DiagnoseActivity.class));
        }
    }

    /**
     * 改变编辑状态
     */
    private void changeEditable() {
        if (Editable) {
            Editable = false;
            img_editer.setVisibility(View.GONE);
            btn_save.setVisibility(View.VISIBLE);
        } else {
            Editable = true;
            img_editer.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
        }
    }
}
