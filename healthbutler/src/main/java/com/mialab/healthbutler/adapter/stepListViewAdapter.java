package com.mialab.healthbutler.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.domain.SportRecords;


public class stepListViewAdapter extends BaseAdapter {

	private Context context;
	private List<SportRecords> recordList;
	private LayoutInflater inflater;

	public stepListViewAdapter(Context context,List<SportRecords> recordList) {
		this.context=context;
		this.recordList=recordList;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return recordList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null)
		{
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.step_listview_item, null);
			viewHolder.tv_steptime=(TextView)convertView.findViewById(R.id.tv_step_time);
			viewHolder.tv_stepcount=(TextView)convertView.findViewById(R.id.tv_lv_step_count);
			viewHolder.tv_stepcaloria=(TextView)convertView.findViewById(R.id.tv_lv_step_caloria);
			viewHolder.tv_steptimes=(TextView)convertView.findViewById(R.id.tv_lv_step_times);
			viewHolder.img_run=(ImageView)convertView.findViewById(R.id.img_run);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(ViewHolder) convertView.getTag();
		}

		viewHolder.tv_steptime.setText(recordList.get(position).getDate());
		viewHolder.tv_stepcount.setText(recordList.get(position).getStepcount()+"步");
		viewHolder.tv_stepcaloria.setText(recordList.get(position).getCalorie()+"");
		viewHolder.tv_steptimes.setText("用时 "+recordList.get(position).getTime());
		return convertView;
	}

	private class ViewHolder{
		TextView tv_steptime;
		ImageView img_run;
		TextView tv_stepcount;
		TextView tv_stepcaloria;
		TextView tv_steptimes;
	}

}
