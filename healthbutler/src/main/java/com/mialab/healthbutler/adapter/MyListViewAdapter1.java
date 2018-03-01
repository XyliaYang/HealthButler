package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mialab.healthbutler.domain.Info;
import com.mialab.healthbutler.R;

import java.util.List;


/**
 * Created by LFZ on 2016/6/10.
 */
public class MyListViewAdapter1 extends BaseAdapter {
    Context context;
    List<Info> list;
    LayoutInflater inflater;
    public MyListViewAdapter1(Context context,List<Info> list){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.everyinfo,null);
            holder.tv_time=(TextView)convertView.findViewById(R.id.tv_day);
            holder.tv_result=(TextView)convertView.findViewById(R.id.result);
            holder.tv_location=(TextView)convertView.findViewById(R.id.loc);
            holder.tv_doctor=(TextView)convertView.findViewById(R.id.doc);
            holder.tv_medical=(TextView)convertView.findViewById(R.id.med);

            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.tv_time.setText(list.get(position).getDay());
        holder.tv_result.setText(list.get(position).getResult());
        holder.tv_location.setText(list.get(position).getLocation());
        holder.tv_doctor.setText(list.get(position).getName());
        holder.tv_medical.setText(list.get(position).getMethod());
        return convertView;
    }

    class ViewHolder{
        TextView tv_time;
        TextView tv_result;
        TextView tv_day;
        TextView tv_location;
        TextView tv_doctor;
        TextView tv_medical;
    }
}
