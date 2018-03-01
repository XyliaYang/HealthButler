package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.Disease;

/**
 * Created by hp on 2016/6/11.
 */
public class DiseaseDetailActivity extends Activity {

    TextView tv_name;
    TextView tv_introduction;
    TextView tv_introduction_content;
    TextView tv_reminder;
    ImageButton ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_disease_detail);
        tv_name = (TextView) findViewById(R.id.tv_name);

        tv_introduction = (TextView) findViewById(R.id.tv_introduction);

        tv_introduction_content = (TextView) findViewById(R.id.tv_introduction_content);

        tv_reminder = (TextView) findViewById(R.id.tv_reminder);

        ib_back = (ImageButton) findViewById(R.id.ib_back);
        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        Disease disease = (Disease) intent.getSerializableExtra("disease");
        tv_name.setText(disease.getIllness_name());
        tv_introduction_content.setText(disease.getintroduction_content());
        tv_reminder.setText(disease.getreminder());
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
