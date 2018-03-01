package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.Methods;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LFZ on 2016/6/11.
 */
public class MyListViewAdapter2 extends BaseAdapter {

    private Context context;
    private List<Methods> methodsList=new ArrayList<Methods>();
    private LayoutInflater inflater;

    public MyListViewAdapter2(Context context,List<Methods> methodsList){
        this.context=context;
        this.methodsList=methodsList;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return methodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return methodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 viewHolder=null;

        if(convertView==null){
            viewHolder=new ViewHolder2();
            convertView=inflater.inflate(R.layout.everymethod,null);
            viewHolder.tv_method=(TextView)convertView.findViewById(R.id.tv_method);
            viewHolder.tv_result=(TextView)convertView.findViewById(R.id.tv_result);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder2)convertView.getTag();
        }
        ImageView img=(ImageView)convertView.findViewById(R.id.img_disease);
        viewHolder.tv_method.setText(methodsList.get(position).getMethod());
        viewHolder.tv_result.setText(methodsList.get(position).getName());
        Glide.with(context).load(methodsList.get(position).getImg()).centerCrop().into(img);
        return convertView;
    }

    class ViewHolder2{
        TextView tv_result;
        TextView tv_method;
    }
}
