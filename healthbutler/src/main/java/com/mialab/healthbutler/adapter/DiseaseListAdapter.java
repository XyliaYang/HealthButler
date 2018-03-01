package com.mialab.healthbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.Disease;
import com.mialab.healthbutler.utils.ListItemClickHelp;

import java.util.ArrayList;

/**
 * Created by hp on 2016/2/20.
 */
public class DiseaseListAdapter extends BaseAdapter {
    ;
    private Context context;
    private ArrayList<Disease> disease;
    public ListItemClickHelp callback;
    private LayoutInflater mInflater;

    public DiseaseListAdapter(Context context, ArrayList<Disease> disease,
                              ListItemClickHelp callback) {
        this.context = context;
        this.disease = disease;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return disease.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_disease_item, null);
            holder = new ViewHolder();

            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);

            holder.tv_introduction = (TextView) convertView
                    .findViewById(R.id.tv_introduction);

            holder.tv_introduction_content = (TextView) convertView
                    .findViewById(R.id.tv_introduction_content);

            holder.tv_reminder = (TextView) convertView
                    .findViewById(R.id.tv_reminder);

            holder.rl_commen_sense = (RelativeLayout) convertView
                    .findViewById(R.id.rl_commen_sense);

            holder.rl_symptom = (RelativeLayout) convertView
                    .findViewById(R.id.rl_symptom);

            holder.rl_cure = (RelativeLayout) convertView
                    .findViewById(R.id.rl_cure);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_name.setText(disease.get(position).getIllness_name());
        holder.tv_introduction_content.setText(disease.get(position).getintroduction_content());
        holder.tv_reminder.setText(disease.get(position).getreminder());

        final View view = convertView;
        final int p = position;
        final int rl_commen_sense = holder.rl_commen_sense.getId();
        holder.rl_commen_sense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, rl_commen_sense);
            }
        });
        final int rl_symptom = holder.rl_symptom.getId();
        holder.rl_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, rl_symptom);
            }
        });
        final int rl_cure = holder.rl_cure.getId();
        holder.rl_cure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(view, parent, p, rl_cure);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        TextView tv_name;
        TextView tv_introduction;
        TextView tv_introduction_content;
        TextView tv_reminder;
        RelativeLayout rl_commen_sense;
        RelativeLayout rl_symptom;
        RelativeLayout rl_cure;

    }

}
