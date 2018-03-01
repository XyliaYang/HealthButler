package com.mialab.healthbutler.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.CityAdapter;
import com.mialab.healthbutler.adapter.DoctorListAdapter;
import com.mialab.healthbutler.domain.City;
import com.mialab.healthbutler.domain.Doctor;
import com.mialab.healthbutler.domain.ResponseResult;
import com.mialab.healthbutler.utils.ListItemClickHelp;
import com.mialab.healthbutler.utils.TranslucentBarsUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class DoctorListActivity  extends Activity  implements ListItemClickHelp{

    private static final String results = "{\n" +
            "  \"error\": false,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"刘如恩\",\n" +
            "      \"hospital_name\":\"中日友好医院\",\n" +
            "      \"head_image\":\"http://xs3.op.xywy.com/api.iu1.xywy.com/zhuanjia/20151112/6c09a9083f0817f371fc9d5576c24d6c67855_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/liuruen\",\n" +
            "      \"doctor_intro\":\"擅长：特别擅长对面肌痉挛、三叉神经痛、舌咽神经痛、癫痫外科治疗、各种颅内肿瘤特别是颅咽管瘤及巨大垂体腺瘤的全切、听神经瘤的显微外科治疗有较为深入的研究\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"2\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"李维新\",\n" +
            "      \"hospital_name\":\"唐都医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20141218/4d93512af31c4415f03e5aba6f5d1a4c19898_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/liweixindr\",\n" +
            "      \"doctor_intro\":\"擅长：脑内肿瘤（胶质瘤、垂体瘤、脑膜瘤、颅咽管瘤、听神经瘤等），脊髓肿瘤、脊髓空洞、小脑扁桃体下疝、颅底畸形、脊柱裂，脊髓栓系综合征、脑积水等。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"3\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"贾栋\",\n" +
            "      \"hospital_name\":\"唐都医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20140923/26497696a37e21832099b95cbdb44da057600_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/xajiadong\",\n" +
            "      \"doctor_intro\":\"擅长：擅长颅内肿瘤、颅底肿瘤、脊髓肿瘤的显微神经外科手术。对颅神经疾病（三叉神经痛面肌抽搐及痉挛舌咽神经痛）手术治疗也具有较高水平。尤其专长于神经内窥镜微创治疗脑积水脑室肿瘤及不开颅经蝶切除垂体瘤。长期从事脑肿瘤、颅脑外伤、脊柱脊髓疾病的治疗和研究工作\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"4\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"王学廉\",\n" +
            "      \"hospital_name\":\"唐都医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20140610/cfcb95f05cd02371285039d94e7743a377139_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/wangxuelian\",\n" +
            "      \"doctor_intro\":\"擅长：帕金森病、抽动秽语综合征、抽动症、肌张力障碍、难治性精神病、吸毒成瘾、酒精依赖、顽固性疼痛、脑瘫、三叉神经痛、面肌痉挛、扭转痉挛、痉挛性斜颈、舞蹈症、特发性震颤等功能性脑疾病。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"5\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"赵振伟\",\n" +
            "      \"hospital_name\":\"唐都医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20150925/a400aacf87a3bd3e9cf1d38a0728351166131_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/zhaozhenweidr\",\n" +
            "      \"doctor_intro\":\"擅长：出血和缺血性脑卒中的介入治疗，包括脑动脉瘤、脑动静脉畸形、动静脉瘘、颈内动脉海绵窦瘘、肿瘤术前栓塞、胶质瘤化疗、颅内及颈动脉狭窄等各类检查和治疗。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"6\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"骆纯\",\n" +
            "      \"hospital_name\":\"上海长征医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20141231/d875ad05766c59932cc11f892570c9ca94192_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/luocdr\",\n" +
            "      \"doctor_intro\":\"擅长：垂体瘤、脑膜瘤、胶质瘤、听神经瘤、颅底肿瘤、脊髓肿瘤、三叉神经痛、面肌痉挛的微创手术，脑积水、颅骨整形、脊髓空洞的综合治疗。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"7\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"郭宗泽\",\n" +
            "      \"hospital_name\":\"中国医科大学附属第一医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20150625/ac04fca5b201c7c713497c938f2e21a232590_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/guozongze\",\n" +
            "      \"doctor_intro\":\"擅长：擅长复杂脑胶质瘤诊断、手术及综合治疗；听神经瘤全切手术、垂体瘤、复杂血管畸形和血管瘤等治疗。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"8\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"江荣才\",\n" +
            "      \"hospital_name\":\"天津医科大学总医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20140611/192a31eeebfcf3ff97853929e4967e8169071_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/jiangrongcai\",\n" +
            "      \"doctor_intro\":\"擅长：慢性硬膜下血肿（保守治疗、无痛苦、副作用少，已治愈多例患者）；难治性颅咽管瘤（囊性颅咽管瘤立体定向囊内放疗+放疗，15年随访未复发）；重型颅脑伤：综合治疗重型颅脑损伤效果好。擅长重症感染、颅内高压救治。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"9\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"贺晓生\",\n" +
            "      \"hospital_name\":\"西京医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20150623/9b9eb5a2b8306249b1c698536dd18baa8732_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/hexiaosheng\",\n" +
            "      \"doctor_intro\":\"擅长：颅内肿瘤：神经胶质瘤 颅咽管瘤 脑膜瘤，听神经瘤，经鼻孔切除垂体瘤 显微镜下精准切除颅底肿瘤 新生儿脑积水 成人脑积水 颅脑肿瘤，颅内囊肿的治疗。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"10\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"尹龙\",\n" +
            "      \"hospital_name\":\"天津市环湖医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20140611/dceb9bf0bc35b3bc92b096927f98816c29242_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/dryinlong\",\n" +
            "      \"doctor_intro\":\"擅长：脑血管病的介入治疗。\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"11\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"杭春华\",\n" +
            "      \"hospital_name\":\"南京军区总院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20140819/317509b6190d57aab8b636c248b48e8f49012_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/hang1965\",\n" +
            "      \"doctor_intro\":\"擅长：脑血管疾病的手术治疗，包括动脉瘤、脑血管畸形、烟雾病及缺血性脑血管病的外科治疗；颅内肿瘤的外科治疗，包括颅底肿瘤、脑干肿瘤、鞍区肿瘤等复杂病变；颅脑外伤的综合治疗；椎管内肿瘤和颅颈交界区畸形；颅脑先天性疾患的诊治\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"12\",\n" +
            "      \"illness_name\":\"神经外科\",\n" +
            "      \"doctor_name\":\"吴安华\",\n" +
            "      \"hospital_name\":\"中国医科大学附属第一医院\",\n" +
            "      \"head_image\":\"http://static.i2.xywy.com/zhuanjia/20140711/01172591bd115bfda33ef8d01382690328887_a.jpg\",\n" +
            "      \"personal_website\":\"http://z.xywy.com/doc/wuanhua\",\n" +
            "      \"doctor_intro\":\"擅长：神经系统肿瘤的诊断与治疗，颅底手术（垂体瘤，听神经瘤，及其他侵袭颅底的疾病），胶质瘤综合治疗（手术，放化疗及生物治疗），微创神经内镜手术（脑积水，垂体瘤，颅底肿瘤），脊柱固定及椎管内肿瘤等。\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";



    ImageButton iv_back;
    ListView lv_doctors;
    List<Doctor>  doctors=new ArrayList<Doctor>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_doctor_list);


        initview();
        initData();
    }

    private void initview() {
        setContentView(R.layout.activity_doctor_list);
        iv_back= (ImageButton) findViewById(R.id.ib_back);
        lv_doctors= (ListView) findViewById(R.id.lv_doctors);
    }


    private void initData() {

        Gson gson = new Gson();
        Type userType = new TypeToken<ResponseResult<List<Doctor>>>() {
        }.getType();
        ResponseResult<List<Doctor>> result = gson.fromJson(results, userType);

        doctors = result.getResults();

        final DoctorListAdapter doctorListAdapter = new DoctorListAdapter(DoctorListActivity.this, (ArrayList<Doctor>) doctors,this);
        lv_doctors.setAdapter(doctorListAdapter);
        lv_doctors.setSelection(0);

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

        Doctor doctor=doctors.get(position);
        switch (which){

            //医生详情页
            case R.id.rl_detail:

                Intent intent=new Intent(DoctorListActivity.this,DoctorDetailActivity.class);
//                Toast.makeText(DoctorListActivity.this,"详情页",Toast.LENGTH_LONG).show();
                Doctor doctor1=doctors.get(position);
                Bundle data=new Bundle();
                data.putSerializable("doctor", (Serializable) doctor1);
                intent.putExtras(data);
                startActivity(intent);

                break;
            case R.id.rl_personal_web:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(doctor.getPersonal_website()));
                startActivity(intent1);

                break;
        }




    }
}
