package com.mialab.healthbutler.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.BranchAdapter;
import com.mialab.healthbutler.adapter.DepartmentAdapter;
import com.mialab.healthbutler.domain.Departments;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class DepartmentsListFragment extends Fragment {
    private static final String departments = "{\n" +
            "  \"error\": false,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"departments_name\":\"内科\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"2\",\n" +
            "      \"departments_name\":\"外科\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"3\",\n" +
            "      \"departments_name\":\"妇产科学\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"4\",\n" +
            "      \"departments_name\":\"生殖中心\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"5\",\n" +
            "      \"departments_name\":\"骨外科\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"6\",\n" +
            "      \"departments_name\":\"眼科学\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"7\",\n" +
            "      \"departments_name\":\"五官科\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"8\",\n" +
            "      \"departments_name\":\"肿瘤科\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"9\",\n" +
            "      \"departments_name\":\"口腔科学\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"10\",\n" +
            "      \"departments_name\":\"皮肤性病科\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"11\",\n" +
            "      \"departments_name\":\"男科\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"12\",\n" +
            "      \"departments_name\":\"皮肤美容\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @BindView(R.id.lv_department_list)
    ListView lv_department_list;

    List<Departments> list_departments = new ArrayList<Departments>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_list, container, false);
        ButterKnife.bind(this, view);
        initDate();
        return view;
    }

    private void initDate() {


        Gson gson = new Gson();
        Type userType = new TypeToken<ResponseResult<List<Departments>>>() {
        }.getType();
        ResponseResult<List<Departments>> result = gson.fromJson(departments, userType);
        list_departments = result.getResults();

        final DepartmentAdapter departmentAdapter = new DepartmentAdapter(getActivity(), list_departments);
        lv_department_list.setAdapter(departmentAdapter);
        lv_department_list.setSelection(0);
        departmentAdapter.setSelectedItem(0);


        lv_department_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private DiseaseDepartmentListFragment diseaseDepartmentListFragment;

            /**
             * 将变化传递给疾病fragment
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                departmentAdapter.setSelectedItem(position);
                diseaseDepartmentListFragment = (DiseaseDepartmentListFragment) getActivity().getFragmentManager().findFragmentById(R.id.fg_disease_department);
                diseaseDepartmentListFragment.notifyDataChange(list_departments.get(position).getId());
            }
        });

    }
}
