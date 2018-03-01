package com.mialab.healthbutler.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amap.api.maps.model.LatLng;
import com.mialab.healthbutler.domain.SportRecords;
import com.mialab.healthbutler.utils.EveryDaySportRecords;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "sportsrecord.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table latlngs(id integer primary key,savetime varchar(30),lat varchar(30),lng varchar(30))");
		db.execSQL("create table sportrecords(id integer primary key,date varchar(20),time varchar(20),calorie varchar(20),distance varchar(20),stepcount varchar(20),speed varchar(20),savetime varchar(20))");
		db.execSQL("create table onedaysportrecord(id integer primary key,date varchar(30),distance varchar(30),calorie varchar(30),stepcount varchar(30))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// //获取所有用户数据
	// public List<User> getUserList()
	// {
	// SQLiteDatabase db=DBHelper.this.getReadableDatabase();
	// Cursor cursor=db.rawQuery("select *from user;", null);
	// List<User> list=new ArrayList<User>();
	// if(cursor!=null&& cursor.getCount()>0)
	// {
	// while(cursor.moveToNext())
	// {
	// User user=new User();
	// user.setId(cursor.getInt(cursor.getColumnIndex("id")));
	// user.setName(cursor.getString(cursor.getColumnIndex("name")));
	// user.setImg(cursor.getString(cursor.getColumnIndex("img")));
	// list.add(user);
	// }
	// }
	// cursor.close();
	// db.close();
	// return list;
	//
	// }
	//
	// public String findLastTalk(int id)
	// {
	// SQLiteDatabase db=DBHelper.this.getReadableDatabase();
	// Cursor cursor=db.rawQuery("select *from talk where userid="+id, null);
	// String talk_value = null;
	// if(cursor!=null&& cursor.getCount()>0)
	// {
	// while(cursor.moveToNext())
	// {
	// talk_value=cursor.getString(cursor.getColumnIndex("talkvalue"));
	// }
	// }
	// cursor.close();
	// db.close();
	// return talk_value;
	// }
	//
	// public List<Chat> getChatByUserId(User user) {
	// SQLiteDatabase db=DBHelper.this.getReadableDatabase();
	// Cursor cursor=db.rawQuery("select *from talk where userid="+user.getId(),
	// null);
	// List<Chat> chatList=new ArrayList<Chat>();
	// if(cursor!=null&& cursor.getCount()>0)
	// {
	// while(cursor.moveToNext())
	// {
	// Chat chat=new Chat();
	// chat.setId(cursor.getInt(cursor.getColumnIndex("id")));
	// chat.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
	// chat.setTalkvalue(cursor.getString(cursor.getColumnIndex("talkvalue")));
	// chat.setIsme(cursor.getInt(cursor.getColumnIndex("isme")));
	// chat.setTime(cursor.getString(cursor.getColumnIndex("time")));
	// chatList.add(chat);
	// }
	// }
	// cursor.close();
	// db.close();
	// return chatList;
	// }
	//

	// 插入一条运动记录
	public void insertSportRecord(SportRecords record) {
		SQLiteDatabase db = DBHelper.this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("date", record.getDate());
		values.put("time", record.getTime());
		values.put("calorie", record.getCalorie());
		values.put("stepcount", record.getStepcount());
		values.put("distance", record.getDistance());
		values.put("speed", record.getSpeed());
		values.put("savetime", record.getSavetime());
		db.insert("sportrecords", null, values);
		db.close();
	}

	// 获取所有的运动记录
	public List<SportRecords> getAllSportRecord() {
		SQLiteDatabase db = DBHelper.this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select *from sportrecords order by id desc", null);
		List<SportRecords> records = new ArrayList<SportRecords>();
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				SportRecords sportRecords = new SportRecords();
				sportRecords.setCalorie(cursor.getString(cursor
						.getColumnIndex("calorie")));
				sportRecords.setDate(cursor.getString(cursor
						.getColumnIndex("date")));
				sportRecords.setDistance(cursor.getString(cursor
						.getColumnIndex("distance")));
				sportRecords.setSavetime(cursor.getString(cursor
						.getColumnIndex("savetime")));
				sportRecords.setSpeed(cursor.getString(cursor
						.getColumnIndex("speed")));
				sportRecords.setStepcount(cursor.getString(cursor
						.getColumnIndex("stepcount")));
				sportRecords.setTime(cursor.getString(cursor
						.getColumnIndex("time")));
				records.add(sportRecords);
			}
		}
		cursor.close();
		db.close();
		return records;

	}

	// 插入一条运动轨迹
	public void insertLatlngRecord(String savetime, List<LatLng> latlngs) {
		SQLiteDatabase db = DBHelper.this.getWritableDatabase();
		ContentValues values = new ContentValues();
		for (int i = 0; i < latlngs.size(); i++) {
			values.put("savetime", savetime);
			values.put("lat", String.valueOf(latlngs.get(i).latitude));
			values.put("lng", String.valueOf(latlngs.get(i).longitude));
			System.out.println(latlngs.size() + "jd"
					+ String.valueOf(latlngs.get(i).latitude) + " " + savetime);
			db.insert("latlngs", null, values);
		}
		db.close();
	}

	// 通过保存时间查询轨迹集合
	public List<LatLng> queryLatlngs(String savetime) {
		SQLiteDatabase db = DBHelper.this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select *from latlngs where savetime="
				+ savetime, null);
		List<LatLng> latlngList=new ArrayList<LatLng>();
		if(cursor!=null&&cursor.getCount()>0)
		{
			while(cursor.moveToNext())
			{
				String stringLat=cursor.getString(cursor.getColumnIndex("lat"));
				String stringLng=cursor.getString(cursor.getColumnIndex("lng"));
				Double lat=Double.parseDouble(stringLat);
				Double lng=Double.parseDouble(stringLng);
				LatLng latlng=new LatLng(lat, lng);
				latlngList.add(latlng);
			}
		}
		cursor.close();
		db.close();
		return latlngList;
	}

	//插入一天的运动数据
	public void insertOneDaySportRecord(EveryDaySportRecords records)
	{
		SQLiteDatabase db=DBHelper.this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("date", records.getDate());
		values.put("distance", records.getDistance());
		values.put("calorie", records.getCalorie());
		values.put("stepcount", records.getStepcount());
		db.insert("onedaysportrecord", null, values);
		db.close();
	}

	//按日期查询每日的运动数据
	public EveryDaySportRecords selectOneDaySportRecordByDate(String date)
	{
		SQLiteDatabase db=DBHelper.this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from onedaysportrecord where date="+"'"+date+"'", null);
		System.out.println("cursor的数量"+cursor.getCount());
		if(cursor!=null&&cursor.getCount()>0)
		{
			if(cursor.moveToNext())
			{
				EveryDaySportRecords records=new EveryDaySportRecords();
				records.setDate(date);
				records.setCalorie(cursor.getString(cursor.getColumnIndex("calorie")));
				records.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
				records.setStepcount(cursor.getString(cursor.getColumnIndex("stepcount")));
				return records;
			}
		}

		cursor.close();
		db.close();
		return null;
	}

	//
	// public void clearUserData() {
	// SQLiteDatabase db=DBHelper.this.getWritableDatabase();
	//
	// db.delete("user", "id<"+1000, null);
	// db.close();
	// }

}
