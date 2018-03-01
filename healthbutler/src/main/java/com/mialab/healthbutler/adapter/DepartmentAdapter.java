package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.Departments;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class DepartmentAdapter extends BaseAdapter {

    private Context context;
    private List<Departments> departments;
    private int selectItem = -1;

    public DepartmentAdapter(Context context, List<Departments> departments) {
        this.context = context;
        this.departments = departments;
    }



    @Override
    public int getCount() {
        return departments.size();
    }

    @Override
    public Departments getItem(int position) {
        return departments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.list_department_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvName.setText(getItem(position).getdepartments_name());
        if (position == selectItem) {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.cityname));
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.black));
            convertView.setBackgroundColor(Color.parseColor("#f8f8f8"));
        }
        return convertView;
    }

    public void setSelectedItem(int pos) {
        selectItem = pos;
        notifyDataSetChanged();
    }

    static final class ViewHolder {
        @BindView(R.id.tv_department_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
