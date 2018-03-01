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
import com.mialab.healthbutler.adapter.HospitalRegisterAdapter;
import com.mialab.healthbutler.domain.Hospital;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/7/20.
 */
public class HospitalRegisterFragment extends Fragment {

    @BindView(R.id.lv_hosRegi)
    ListView listView;

    private static final String results =getData();

    List<Hospital> hospitals = new ArrayList<Hospital>();
    private HospitalRegisterAdapter hospitalRegisterAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hosregi_list, container, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    /**
     * 获取服务器接口数据
     * @return
     */
    private static String getData(){
        String result="{\n" +
                "  \"error\": false,\n" +
                "  \"results\": [\n" +
                "  {\n" +
                "    \"id\": \"1\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州大学附属第一医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"2\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州大学附属第二医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"3\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市附属第二医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"4\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市中医医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州大学附属儿童医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"6\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市立医院本部\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"7\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市广济医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"8\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市第五人民医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"9\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市九龙医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"10\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市吴中人民医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"11\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市中西医结合医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"12\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市高新区人民医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"13\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州明基医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"14\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州大学附属理想眼科医院\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"15\",\n" +
                "    \"city_id\":\"1\",\n" +
                "    \"hospital_name\":\"苏州市口腔医院\"\n" +
                "  }\n" +
                "]\n" +
                "}";

        return result;
    }

    private void initData() {

        Gson gson = new Gson();
        Type type = new TypeToken<ResponseResult<List<Hospital>>>() {
        }.getType();
        ResponseResult<List<Hospital>> result = gson.fromJson(results, type);
        hospitals = result.getResults();

        hospitalRegisterAdapter = new HospitalRegisterAdapter(getActivity(), hospitals);
        listView.setAdapter(hospitalRegisterAdapter);
        listView.setSelection(0);
        hospitalRegisterAdapter.setSelectedItem(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private BranchRegisterFragment branchRegisterFragment;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hospitalRegisterAdapter.setSelectedItem(position);
                branchRegisterFragment= (BranchRegisterFragment) getActivity().getFragmentManager().findFragmentById(R.id.fg_branch);
                branchRegisterFragment.notifyDataChange(hospitals.get(position).getId());

            }
        });
    }

}
