package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.TaskList.Task;

import java.util.ArrayList;

/**
 * Created by hp on 2016/2/20.
 */
public class TaskListClickAdapter extends BaseAdapter {
    private Context contxet;
    private ArrayList<Task> mTasklist;
    public ListItemClickListener callback;

    public interface ListItemClickListener {
        void onItemClick(View item, View widget, int position, int which);
    }

    public TaskListClickAdapter(Context contxet, ArrayList<Task> mTasklist, ListItemClickListener callback) {
        this.contxet = contxet;
        this.mTasklist = mTasklist;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return mTasklist.size();
    }

    @Override
    public Task getItem(int position) {
        return mTasklist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(contxet, R.layout.list_task_now_item, null);
            holder = new ViewHolder();
            holder.tvTaskContent = (TextView) convertView.findViewById(R.id.tv_task_content);
            holder.ivTaskComplete = (ImageView) convertView.findViewById(R.id.iv_complete);
            holder.ivTaskDetail = (ImageView) convertView.findViewById(R.id.iv_task_detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTaskContent.setText(mTasklist.get(position).getmTaskDetail());

        final View view = convertView;
        final int pos = position;
        final int completeID = holder.ivTaskComplete.getId();
        holder.ivTaskComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(view, parent, pos, completeID);
            }
        });
        final int detailID = holder.ivTaskDetail.getId();
        holder.ivTaskDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(view, parent, pos, detailID);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        TextView tvTaskContent;
        ImageView ivTaskComplete;
        ImageView ivTaskDetail;
    }

}
