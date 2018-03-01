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
import com.mialab.healthbutler.activity.RegisterListActivity;
import com.mialab.healthbutler.adapter.BranchRegiAdapter;
import com.mialab.healthbutler.domain.Branch;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/7/20.
 */
public class BranchRegisterFragment extends Fragment {
    private static final String branches = getData();

    @BindView(R.id.lv_branchRegi)
    ListView lv_branchRegi;

    List<Branch> list_branchs = new ArrayList<Branch>();
    private  BranchRegiAdapter branchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branchregi_list, container, false);
        ButterKnife.bind(this, view);
        initDate();
        return view;
    }


    private  static  String getData(){
        String result="{\n" +
                "  \"error\": false,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": \"1\",\n" +
                "      \"branch_name\":\"内科\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2\",\n" +
                "      \"branch_name\":\"外科\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"3\",\n" +
                "      \"branch_name\":\"妇产科学\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"4\",\n" +
                "      \"branch_name\":\"生殖中心\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"5\",\n" +
                "      \"branch_name\":\"骨外科\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"6\",\n" +
                "      \"branch_name\":\"眼科学\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"7\",\n" +
                "      \"branch_name\":\"五官科\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"8\",\n" +
                "      \"branch_name\":\"肿瘤科\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"9\",\n" +
                "      \"branch_name\":\"口腔科学\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"10\",\n" +
                "      \"branch_name\":\"皮肤性病科\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"11\",\n" +
                "      \"branch_name\":\"男科\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"12\",\n" +
                "      \"branch_name\":\"皮肤美容\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        return result;
    }

    private void initDate() {


        Gson gson = new Gson();
        Type userType = new TypeToken<ResponseResult<List<Branch>>>() {
        }.getType();
        ResponseResult<List<Branch>> result = gson.fromJson(branches, userType);
        list_branchs = result.getResults();

        branchAdapter = new BranchRegiAdapter(getActivity(), list_branchs);
        lv_branchRegi.setAdapter(branchAdapter);

        lv_branchRegi.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RegisterListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("branch",list_branchs.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    public void notifyDataChange(int id) {
        branchAdapter.notifyDataSetChanged();
        lv_branchRegi.setSelection(0);
    }
}
