package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.Branch;
import com.mialab.healthbutler.domain.Illness;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hp on 2016/6/10.
 */
public class BranchRegiAdapter extends BaseAdapter {

    private Context context;
    private List<Branch> branches;

    public BranchRegiAdapter(Context context, List<Branch> branches) {
        this.context = context;
        this.branches = branches;
    }

    @Override
    public int getCount() {
        return branches.size();
    }

    @Override
    public Branch getItem(int position) {
        return branches.get(position);
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
            convertView = View.inflate(context, R.layout.list_illness_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvName.setText(getItem(position).getBranch_name());
        return convertView;
    }

    static final class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
