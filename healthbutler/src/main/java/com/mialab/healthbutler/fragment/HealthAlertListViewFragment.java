package com.mialab.healthbutler.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.activity.HealthAlertActviity;

/**
 * Created by LFZ on 2016/7/20.
 */
public class HealthAlertListViewFragment extends Fragment {

    private ListView lv;
    private String[] items={"运动提醒","饮食提醒","睡眠提醒","医疗提醒"};
    public int selectPosition=-1;
    private ItemClickListener itemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_health_alert_list,null);

        lv=(ListView)view.findViewById(R.id.lv);

        final MyBaseAdapter adapter=new MyBaseAdapter(getActivity(),items);
        lv.setAdapter(adapter);
        adapter.setSelection(0);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);
                itemClickListener.onItemSelect(position);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        itemClickListener=(HealthAlertActviity)activity;
    }

    class MyBaseAdapter extends BaseAdapter{

        private Context context;
        private String []items;
        private LayoutInflater inflater;

        public MyBaseAdapter(Context context,String items[]){
            this.context=context;
            this.items=items;
            this.inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder=new MViewHolder();
                convertView=inflater.inflate(R.layout.layout,null);
                viewHolder.tv=(TextView)convertView.findViewById(R.id.tv);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder=(MViewHolder)convertView.getTag();
            }

            viewHolder.tv.setText(items[position]);
            if(selectPosition==position){
                viewHolder.tv.setTextColor(Color.parseColor("#ffc712"));
                convertView.setBackgroundColor(Color.parseColor("#eeeeee"));
            }
            else{
                viewHolder.tv.setTextColor(Color.parseColor("#000000"));
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            return convertView;
        }

        public void setSelection(int position){
            selectPosition=position;
            notifyDataSetChanged();
        }
        class MViewHolder{
            TextView tv;
        }
    }

    public interface ItemClickListener{
        void onItemSelect(int Postion);
    }
}


