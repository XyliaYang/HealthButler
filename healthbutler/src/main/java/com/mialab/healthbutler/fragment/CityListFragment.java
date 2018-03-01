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
import com.mialab.healthbutler.adapter.CityAdapter;
import com.mialab.healthbutler.domain.City;
import com.mialab.healthbutler.domain.ResponseResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wesly186 on 2016/6/8.
 */
public class CityListFragment extends Fragment {

    private static final String citys = "{\n" +
            "  \"error\": false,\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"id\": \"1\",\n" +
            "      \"city_name\":\"南京\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"2\",\n" +
            "      \"city_name\":\"苏州\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"3\",\n" +
            "      \"city_name\":\"淮安\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"4\",\n" +
            "      \"city_name\":\"常州\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"5\",\n" +
            "      \"city_name\":\"无锡\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"6\",\n" +
            "      \"city_name\":\"扬州\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"7\",\n" +
            "      \"city_name\":\"泰州\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"8\",\n" +
            "      \"city_name\":\"上海\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"9\",\n" +
            "      \"city_name\":\"合肥\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"10\",\n" +
            "      \"city_name\":\"深圳\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"11\",\n" +
            "      \"city_name\":\"连云港\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"12\",\n" +
            "      \"city_name\":\"哈尔滨\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @BindView(R.id.lv_city)
    ListView listView;

    List<City> cities = new ArrayList<City>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);
        ButterKnife.bind(this, view);
        initDate();
        return view;
    }

    protected void initDate() {

        Gson gson = new Gson();
        Type userType = new TypeToken<ResponseResult<List<City>>>() {
        }.getType();
        ResponseResult<List<City>> result = gson.fromJson(citys, userType);
        cities = result.getResults();

        final CityAdapter cityAdapter = new CityAdapter(getActivity(), cities);
        listView.setAdapter(cityAdapter);
        listView.setSelection(0);
        cityAdapter.setSelectedItem(0);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private HospitalListFragment hospitalFragment;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setSelectedItem(position);
                hospitalFragment = (HospitalListFragment) getActivity().getFragmentManager().findFragmentById(R.id.fg_hos);
                hospitalFragment.notifyDataChange(cities.get(position).getId());
            }
        });

    }
}
