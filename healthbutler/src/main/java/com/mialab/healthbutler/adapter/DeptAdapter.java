package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.Department;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wesly186 on 2016/6/9.
 */
public class DeptAdapter extends RecyclerView.Adapter<DeptAdapter.MyViewHolder> {

    private Context context;
    private List<Department> departments;

    public DeptAdapter(Context context, List<Department> departments) {
        this.context = context;
        this.departments = departments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(View.inflate(context, R.layout.list_dept_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvName.setText(departments.get(position).getName());
        holder.tvDesc.setText(departments.get(position).getDesc());
        Glide.with(context)
                .load(departments.get(position).getPhoto())
                .placeholder(R.drawable.default_head)
                .error(R.drawable.head)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }


    static final class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_desc)
        TextView tvDesc;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
