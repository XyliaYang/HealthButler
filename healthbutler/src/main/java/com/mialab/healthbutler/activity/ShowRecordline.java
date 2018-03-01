package com.mialab.healthbutler.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.mialab.healthbutler.R;
import com.mialab.healthbutler.db.DBHelper;
import com.mialab.healthbutler.domain.SportRecords;

public class ShowRecordline extends Activity implements OnClickListener{

	private MapView mapView;
	private AMap aMap;

	private TextView tv_step_count,tv_minute_count,tv_speed,tv_distance,tv_caloria;
	private PolylineOptions ployOptions = new PolylineOptions();

	ImageView img_back;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);

		mapView = (MapView) findViewById(R.id.mapview);

		// 必须重写
		mapView.onCreate(savedInstanceState);
		// 初始化aMap
		init();

		tv_step_count = (TextView) findViewById(R.id.tv_step_count);
		tv_minute_count = (TextView) findViewById(R.id.tv_minute_count);
		tv_speed = (TextView) findViewById(R.id.tv_speed);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_caloria = (TextView) findViewById(R.id.tv_caloria);

		img_back=(ImageView)findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		//获取传过来的数据
		Intent intent=ShowRecordline.this.getIntent();
		SportRecords sportRecords=(SportRecords)intent.getSerializableExtra("sportrecords");
		tv_step_count.setText(sportRecords.getStepcount());
		tv_caloria.setText(sportRecords.getCalorie().substring(0,sportRecords.getCalorie().indexOf("k")));
		tv_distance.setText(sportRecords.getDistance());
		tv_speed.setText(sportRecords.getSpeed().substring(0,sportRecords.getSpeed().indexOf("m")));
		tv_minute_count.setText(sportRecords.getTime());

		String savetime=sportRecords.getSavetime();
		DBHelper dbHelper=new DBHelper(ShowRecordline.this);

		List<LatLng> latlngList=new ArrayList<LatLng>();
		latlngList=dbHelper.queryLatlngs(savetime);
		if(latlngList.size()>1)
		{
			ployOptions.addAll(latlngList).color(0xff33c3c9).width(10);
			ployOptions.visible(true);
			aMap.addPolyline(ployOptions);

			CameraUpdate cu=CameraUpdateFactory.changeLatLng(latlngList.get((int)(latlngList.size()/2)));
			aMap.moveCamera(cu);

			MarkerOptions options=new MarkerOptions();
			options.position(latlngList.get(0));
			options.title("起点");
			options.draggable(true);
			BitmapDescriptor descriptor=BitmapDescriptorFactory.defaultMarker();
			options.icon(descriptor);
			aMap.addMarker(options);

			MarkerOptions options1=new MarkerOptions();
			options1.position(latlngList.get(latlngList.size()-1));
			options1.title("终点");

			BitmapDescriptor descriptor1=BitmapDescriptorFactory.defaultMarker();
			options1.icon(descriptor1);
			aMap.addMarker(options1);
		}
	}

	// 初始化地图
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();

			CameraUpdate cu = CameraUpdateFactory.zoomTo(16);
			aMap.moveCamera(cu);
			aMap.setTrafficEnabled(true);
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==img_back.getId())
		{
			finish();
		}
	}

}
