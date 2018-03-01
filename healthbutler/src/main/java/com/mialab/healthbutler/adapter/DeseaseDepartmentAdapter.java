package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.DiseaseDepartments;
import com.mialab.healthbutler.domain.Illness;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class DeseaseDepartmentAdapter extends BaseAdapter {

    private Context context;
    private List<DiseaseDepartments> diseaseDepartmentses;

    public DeseaseDepartmentAdapter(Context context, List<DiseaseDepartments> diseaseDepartmentses) {
        this.context = context;
        this.diseaseDepartmentses = diseaseDepartmentses;
    }

    @Override
    public int getCount() {
        return diseaseDepartmentses.size();
    }

    @Override
    public DiseaseDepartments getItem(int position) {
        return diseaseDepartmentses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.list_desease_departments_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvName.setText(getItem(position).getIllness_name());
        return convertView;
    }

    static final class ViewHolder {
        @BindView(R.id.tv_desease_department_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
