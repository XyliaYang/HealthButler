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
import com.mialab.healthbutler.domain.Branch;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class BranchListFragment extends Fragment {
    private static final String branches = "{\n" +
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

    @BindView(R.id.lv_branch)
    ListView lv_branch;

    List<Branch> list_branchs = new ArrayList<Branch>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branch_list, container, false);
        ButterKnife.bind(this, view);
        initDate();
        return view;
    }

    private void initDate() {


        Gson gson = new Gson();
        Type userType = new TypeToken<ResponseResult<List<Branch>>>() {
        }.getType();
        ResponseResult<List<Branch>> result = gson.fromJson(branches, userType);
        list_branchs = result.getResults();

        final BranchAdapter branchAdapter = new BranchAdapter(getActivity(), list_branchs);
        lv_branch.setAdapter(branchAdapter);
        lv_branch.setSelection(0);
        branchAdapter.setSelectedItem(0);


        lv_branch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private IllnessListFragment illnessListFragment;

            /**
             * 将变化传递给疾病fragment
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                branchAdapter.setSelectedItem(position);
                illnessListFragment = (IllnessListFragment) getActivity().getFragmentManager().findFragmentById(R.id.fg_illness);
                illnessListFragment.notifyDataChange(list_branchs.get(position).getId());
            }
        });

    }
}
