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
import com.lidroid.xutils.util.LogUtils;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.DiseaseListAdapter;
import com.mialab.healthbutler.adapter.DoctorListAdapter;
import com.mialab.healthbutler.domain.Disease;
import com.mialab.healthbutler.domain.Doctor;
import com.mialab.healthbutler.domain.ResponseResult;
import com.mialab.healthbutler.utils.ListItemClickHelp;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2016/6/10.
 */
public class DiseaseListActivity extends Activity implements ListItemClickHelp {

    private static final String results = "{\n" +
            "  \"error\": false,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"2\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"3\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"4\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"5\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"6\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"7\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"8\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"9\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"10\",\n" +
            "      \"illness_name\":\"感冒\",\n" +
            "      \"introduction_content\":\"感冒，总体上分为普通感冒和流行感冒，在这里先讨论普通感冒。普通感冒，祖国医学称“伤风”，是由多种病毒引起的一种呼吸道常见病，其中30%-50%是由某种血清型的鼻病毒引起，普通感冒虽多发于初冬，但任何季节，如春天，夏天也可发生，不同季节的感冒的致病病毒并非完全一样。流行性感冒，是由流感病毒引起的急性呼吸道传染病。病毒存在于病人的呼吸道中，在病人咳嗽，打喷嚏时经飞沫传染给别人。流感的传染性很强，由于这种病毒容易变异，即使是患过流感的人，当下次再遇上流感流行，他仍然会感染，所以流感容易引起暴发性流行。一般在冬春季流行的机会较多，每次可能有 20～40%的人会传染上流感。\",\n" +
            "      \"reminder\":\"温馨提示：重在预防，加强锻炼、增强体质、生活饮食规律、改善营养。避免受凉和过度劳累，有助于降低易感性，是预防感冒最好的办法。\",\n" +
            "      \"symptom_website\":\"http://jib.xywy.com/il_sii/symptom/38.htm\",\n" +
            "      \"curemethod_website\":\"http://jib.xywy.com/il_sii/treat/38.htm\"\n" +
            "    },\n" +
            "  ]\n" +
            "}";


    ImageButton iv_back;
    ListView lv_disease;
    List<Disease> disease = new ArrayList<Disease>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_doctor_list);


        initview();
        initData();
    }

    private void initview() {
        setContentView(R.layout.activity_disease_list);
        iv_back = (ImageButton) findViewById(R.id.ib_back);
        lv_disease = (ListView) findViewById(R.id.lv_disease);
    }


    private void initData() {

        Gson gson = new Gson();
        Type userType = new TypeToken<ResponseResult<List<Disease>>>() {
        }.getType();
        ResponseResult<List<Disease>> result = gson.fromJson(results, userType);

        disease = result.getResults();

        final DiseaseListAdapter diseaseListAdapter = new DiseaseListAdapter(DiseaseListActivity.this, (ArrayList<Disease>) disease, this);
        lv_disease.setAdapter(diseaseListAdapter);
        lv_disease.setSelection(0);

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

        Disease disease_1 = disease.get(position);
        switch (which) {

            //疾病详情页
            case R.id.rl_commen_sense:

                Intent intent = new Intent(DiseaseListActivity.this, DiseaseDetailActivity.class);
//                Toast.makeText(DoctorListActivity.this,"详情页",Toast.LENGTH_LONG).show();
                Disease disease_2 = disease.get(position);
                Bundle data = new Bundle();
                data.putSerializable("disease", (Serializable) disease_2);
                intent.putExtras(data);
                LogUtils.d("msg============"+data);
                startActivity(intent);
                break;
            case R.id.rl_symptom:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(disease_1.getsymptom_website()));
                startActivity(intent1);
                break;
            case R.id.rl_cure:
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(disease_1.getcuremethod_website()));
                startActivity(intent2);
                break;
            default:
                break;
        }


    }
}
