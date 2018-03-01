package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.DoctorListAdapter;
import com.mialab.healthbutler.adapter.RegisterListAdapter;
import com.mialab.healthbutler.domain.Doctor;
import com.mialab.healthbutler.domain.DoctorRegi;
import com.mialab.healthbutler.domain.ResponseResult;
import com.mialab.healthbutler.utils.ListItemClickHelp;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/6/10.
 */
public class RegisterListActivity extends Activity  implements ListItemClickHelp{

    private static final String results =getData();

    ImageButton iv_back;
    ListView lv_doctors;
    List<DoctorRegi>  doctors= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_list);


        initview();
        initData();
    }

    private void initview() {
        setContentView(R.layout.activity_register_list);
        iv_back= (ImageButton) findViewById(R.id.ib_back);
        lv_doctors= (ListView) findViewById(R.id.lv_doctors);
    }


    private static String getData(){
        String result="{\n" +
                "  \"error\": false,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": \"1\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"3\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"4\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"5\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"6\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"7\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"8\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"9\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"10\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"11\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"12\",\n" +
                "      \"illness_name\":\"神经外科\",\n" +
                "      \"doctor_name\":\"刘如恩\",\n" +
                "      \"hospital_name\":\"中日友好医院\",\n" +
                "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
                "      \"register_telephone\":\"1111\",\n" +
                "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return result;
    }

    private void initData() {

        Gson gson = new Gson();
        Type userType = new TypeToken<ResponseResult<List<DoctorRegi>>>() {
        }.getType();
        ResponseResult<List<DoctorRegi>> result = gson.fromJson(results, userType);

        doctors = result.getResults();

        final RegisterListAdapter registerListAdapter = new RegisterListAdapter(RegisterListActivity.this, (ArrayList<DoctorRegi>) doctors,this);
        lv_doctors.setAdapter(registerListAdapter);
//        lv_doctors.setSelection(0);

        //返回
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View item, View widget, int position, int which) {

        DoctorRegi doctor=doctors.get(position);
        switch (which){

            //电话咨询
            case R.id.tv_regi_telephone:

                DoctorRegi doctor1=doctors.get(position);
                String telephone=doctor1.getRegister_telephone();

                Intent  intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+telephone));
                startActivity(intent);

                break;

            //预约挂号
            case R.id.tv_regi:
                Intent intent1 =new Intent(this,RegisterDateActivity.class);
                startActivity(intent1);

                break;
        }

    }
}
