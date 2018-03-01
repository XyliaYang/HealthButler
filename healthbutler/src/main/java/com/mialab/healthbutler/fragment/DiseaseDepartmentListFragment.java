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
import com.mialab.healthbutler.activity.DiseaseListActivity;
import com.mialab.healthbutler.activity.DoctorListActivity;
import com.mialab.healthbutler.adapter.DeseaseDepartmentAdapter;
import com.mialab.healthbutler.adapter.IllnessAdapter;
import com.mialab.healthbutler.domain.DiseaseDepartments;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class DiseaseDepartmentListFragment extends Fragment {

    @BindView(R.id.lv_disease_department)
    ListView lv_disease_department;


    private static final String disease_departments = "{\n" +
            "  \"error\": false,\n" +
            "  \"results\": [\n" +
            "  {\n" +
            "    \"id\": \"1\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"神经外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"2\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"功能神经外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"3\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"心血管外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"4\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"胸外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"5\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"整形科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"6\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"乳腺外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"7\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"泌尿外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"8\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"肝胆外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"9\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"肛肠科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"10\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"血管外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"11\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"微创外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"12\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"普外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"13\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"器官移植\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"14\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"综合外科\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"15\",\n" +
            "    \"department_id\":\"1\",\n" +
            "    \"disease_department_name\":\"普通内科\"\n" +
            "  }\n" +
            "]\n" +
            "}";



    List<DiseaseDepartments> list_disease_departments = new ArrayList<DiseaseDepartments>();
    private DeseaseDepartmentAdapter deseaseDepartmentAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_department_list, container, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    private void initData() {

        Gson gson = new Gson();
        Type type = new TypeToken<ResponseResult<List<DiseaseDepartments>>>() {
        }.getType();
        ResponseResult<List<DiseaseDepartments>> result = gson.fromJson(disease_departments, type);
        list_disease_departments = result.getResults();

        deseaseDepartmentAdapter = new DeseaseDepartmentAdapter(getActivity(), list_disease_departments);
        lv_disease_department.setAdapter(new DeseaseDepartmentAdapter(getActivity(), list_disease_departments));
        lv_disease_department.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DiseaseListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("disease_departments", list_disease_departments.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }




    public void notifyDataChange(int id) {
        deseaseDepartmentAdapter.notifyDataSetChanged();
        lv_disease_department.setSelection(0);
    }
}
