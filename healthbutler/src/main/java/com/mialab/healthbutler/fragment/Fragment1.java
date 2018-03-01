package com.mialab.healthbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.MyListViewAdapter1;
import com.mialab.healthbutler.domain.Info;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by LFZ on 2016/6/10.
 */
public class Fragment1 extends Fragment {

    ListView lv_info;
    List<Info> infolist=new ArrayList<Info>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment1,null);
        lv_info=(ListView)view.findViewById(R.id.lv_info);

        getData();

        MyListViewAdapter1 adapter=new MyListViewAdapter1(getContext(),infolist);
        lv_info.setAdapter(adapter);
        return view;
    }

    public void getData(){

        for(int i=0;i<3;i++){
            Info info=new Info();
            info.setId(i);
            info.setDay("2016-12-15");
            info.setLocation("医院");
            info.setName("doctor");
            info.setResult("胃炎");
            info.setMethod("好好吃药");
            infolist.add(info);
        }


    }
}
