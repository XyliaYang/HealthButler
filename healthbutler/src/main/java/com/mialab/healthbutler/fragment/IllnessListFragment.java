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
import com.mialab.healthbutler.activity.DoctorListActivity;
import com.mialab.healthbutler.activity.HosDetailActivity;
import com.mialab.healthbutler.adapter.HospitalAdapter;
import com.mialab.healthbutler.adapter.IllnessAdapter;
import com.mialab.healthbutler.domain.Hospital;
import com.mialab.healthbutler.domain.Illness;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class IllnessListFragment  extends Fragment {

    @BindView(R.id.lv_illness)
    ListView lv_illness;


    private static final String illnesses =getData();

    List<Illness> list_illness = new ArrayList<Illness>();
    private IllnessAdapter illnessAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_illness_list, container, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    private static String getData(){
        String result="{\n" +
                "  \"error\": false,\n" +
                "  \"results\": [\n" +
                "  {\n" +
                "    \"id\": \"1\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"神经外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"2\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"功能神经外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"3\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"心血管外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"4\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"胸外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"整形科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"6\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"乳腺外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"7\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"泌尿外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"8\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"肝胆外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"9\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"肛肠科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"10\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"血管外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"11\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"微创外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"12\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"普外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"13\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"illness_name\":\"器官移植\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"14\",\n" +
                "    \"illness_name\":\"1\",\n" +
                "    \"illness_name\":\"综合外科\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"15\",\n" +
                "    \"branch_id\":\"1\",\n" +
                "    \"illness_name\":\"普通内科\"\n" +
                "  }\n" +
                "]\n" +
                "}";
        return result;
    }

    private void initData() {

        Gson gson = new Gson();
        Type type = new TypeToken<ResponseResult<List<Illness>>>() {
        }.getType();
        ResponseResult<List<Illness>> result = gson.fromJson(illnesses, type);
        list_illness = result.getResults();

        illnessAdapter = new IllnessAdapter(getActivity(), list_illness);
        lv_illness.setAdapter(new IllnessAdapter(getActivity(), list_illness));

        lv_illness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DoctorListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("illness", list_illness.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }




    public void notifyDataChange(int id) {
        illnessAdapter.notifyDataSetChanged();
        lv_illness.setSelection(0);
    }
}
