package com.mialab.healthbutler.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.HosDetailActivity;
import com.mialab.healthbutler.adapter.HospitalAdapter;
import com.mialab.healthbutler.domain.Hospital;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wesly186 on 2016/6/8.
 */
public class HospitalListFragment extends Fragment {

    @BindView(R.id.lv_hos)
    ListView listView;

    private static final String results = "{\n" +
            "  \"error\": false,\n" +
            "  \"results\": [\n" +
            "  {\n" +
            "    \"id\": \"1\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"江苏省人民医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"2\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"江苏省中医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"3\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京市儿童医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"4\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京军区南京总医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"5\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京鼓楼医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"6\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京市口腔医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"7\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"东南大学附属中大医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"8\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京市第一医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"9\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京脑科医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"10\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京市妇幼保健院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"11\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"江苏省肿瘤医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"12\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京医科大学第二附属医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"13\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"江苏省中西医结合医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"14\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京市第二医院\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"15\",\n" +
            "    \"city_id\":\"1\",\n" +
            "    \"hospital_name\":\"南京市胸科医院\"\n" +
            "  }\n" +
            "]\n" +
            "}";

    List<Hospital> hospitals = new ArrayList<Hospital>();
    private HospitalAdapter hospitalAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hos_list, container, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    private void initData() {

        Gson gson = new Gson();
        Type type = new TypeToken<ResponseResult<List<Hospital>>>() {
        }.getType();
        ResponseResult<List<Hospital>> result = gson.fromJson(results, type);
        hospitals = result.getResults();
        hospitalAdapter = new HospitalAdapter(getActivity(), hospitals);
        listView.setAdapter(new HospitalAdapter(getActivity(), hospitals));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HosDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hospital", hospitals.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void notifyDataChange(int id) {
        hospitalAdapter.notifyDataSetChanged();
        listView.setSelection(0);
    }
}
