package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.TaskListClickAdapter.*;
import com.mialab.healthbutler.domain.TaskList;


import java.util.ArrayList;

/**
 * Created by hp on 2016/2/20.
 */
public class CompleteTaskListClickAdapter extends BaseAdapter {
    private Context contxet;
    private ArrayList<TaskList.Task> list;
    public ListItemClickListener callback;
    private LayoutInflater mInflater;

    public CompleteTaskListClickAdapter(Context contxet, ArrayList<TaskList.Task> list,
                                        ListItemClickListener callback) {
        this.contxet = contxet;
        this.list = list;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        mInflater = (LayoutInflater) contxet
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_task_complete_item, null);
            holder = new ViewHolder();
            holder.mAd_tv_show = (TextView) convertView
                    .findViewById(R.id.done_textview);
            holder.mAd_tv_show.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mAd_btn_one = (ImageView) convertView
                    .findViewById(R.id.iv_not_complete);
            holder.mAd_btn_two = (ImageView) convertView
                    .findViewById(R.id.iv_done_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mAd_tv_show.setText(list.get(position).getmTaskDetail());

        final View view = convertView;
        final int p = position;
        final int one = holder.mAd_btn_one.getId();
        holder.mAd_btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(view, parent, p, one);
            }
        });
        final int two = holder.mAd_btn_two.getId();
        holder.mAd_btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(view, parent, p, two);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        TextView mAd_tv_show;
        ImageView mAd_btn_one;
        ImageView mAd_btn_two;
    }

}
