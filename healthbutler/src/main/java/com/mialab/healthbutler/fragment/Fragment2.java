package com.mialab.healthbutler.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.mialab.healthbutler.R;

import com.mialab.healthbutler.adapter.MyListViewAdapter2;
import com.mialab.healthbutler.domain.Methods;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LFZ on 2016/6/10.
 */
public class Fragment2 extends Fragment implements AdapterView.OnItemClickListener{

    public ListView lv;
    List<Methods>  methodsList=new ArrayList<Methods>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment2,null);
        lv=(ListView)view.findViewById(R.id.lv_method);
        for(int i=0;i<3;i++){
            getData();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            lv.setNestedScrollingEnabled(true);
        lv.setOnItemClickListener(this);
        MyListViewAdapter2 adapter=new MyListViewAdapter2(getContext(),methodsList);
        lv.setAdapter(adapter);
        return view;
    }

    private void getData(){
        Methods methods=new Methods();
        methods.setName("慢性胃炎");
        methods.setMethod("胃肠疾病的治疗需要症结合查作全面诊断，而大部分反复发作的胃肠疾病只是单纯治了症。\n" +
                "       例如胃溃疡，治疗的根本不是在于抑制胃酸分泌，而是修复受损粘膜之余，阻断幽门螺杆菌的感染。");
        methods.setImg(R.drawable.method1);
        methodsList.add(methods);


        Methods methods1=new Methods();
        methods1.setName("肺炎");
        methods1.setMethod("合理治疗，大多可以痊愈。婴幼儿、年老体弱及重症肺炎常可危及生命。");
        methods1.setImg(R.drawable.method2);
        methodsList.add(methods1);

        Methods methods2=new Methods();
        methods2.setName("鼻炎");
        methods2.setMethod("鼻炎(包括过敏性、萎缩性和鼻窦炎，有的流脓流水、鼻涕多、有的闻味不灵敏)：用黄砖一块，放火上烧烫，取下，将一调羹醋倒在热砖上，此时有大量热气上冒，患者用鼻闻其热气，一日二次，连用7天，消热、消炎，解毒通窍，治各类鼻炎，有特效。");
        methods2.setImg(R.drawable.background2);
        methodsList.add(methods2);

        Methods methods3=new Methods();
        methods3.setName("咽炎");
        methods3.setMethod("刷牙恶心干呕，感觉喉咙里面有东西咳又咳不出来，这就是咽炎。治疗咽炎一般就用西药，但是不能彻底。");
        methods3.setImg(R.drawable.background3);
        methodsList.add(methods3);
    }

    //列表
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        if(position==0){
            intent.setData(Uri.parse("http://jbk.39.net/wy2/"));
            startActivity(intent);
        }
        else if(position==1){
            intent.setData(Uri.parse("http://jbk.39.net/fy/"));
            startActivity(intent);
        }
        else if(position==2){
            intent.setData(Uri.parse("http://jbk.39.net/by/"));
            startActivity(intent);
        }
        else if(position==3){
            intent.setData(Uri.parse("http://jbk.39.net/mxdcxyy/"));
            startActivity(intent);
        }
    }
}
