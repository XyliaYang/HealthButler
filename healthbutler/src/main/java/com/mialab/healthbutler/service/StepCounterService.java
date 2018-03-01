package com.mialab.healthbutler.service;

import android.app.Notification;
import android.app.Service;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.utils.StepDetector;

//service负责后台的需要长期运行的任务
// 计步器服务
// 运行在后台的服务程序，完成了界面部分的开发后
// 就可以开发后台的服务类StepService
// 注册或注销传感器监听器，在手机屏幕状态栏显示通知，与StepActivity进行通信，走过的步数记到哪里了？？？
public class StepCounterService extends Service {

	public static Boolean FLAG = false;// 服务运行标志

	private SensorManager mSensorManager;// 传感器服务
	private StepDetector detector;// 传感器监听对象

	private PowerManager mPowerManager;// 电源管理服务
	private WakeLock mWakeLock;// 屏幕灯

	//private NotificationManager manager;


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		FLAG = true;// 标记为服务正在运行
		//发送通知
		//doNotification();
		/*Notification notification = new Notification(R.drawable.ic_launcher, getString(R.string.app_name),System.currentTimeMillis());
		PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(this, StepCounterActivity.class), 0);
		notification.setLatestEventInfo(this, "健身小助手", "当前走了"+StepDetector.CURRENT_SETP+"步", pendingintent);
		startForeground(0x111, notification);*/

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 创建监听器类，实例化监听对象
				detector = new StepDetector();
				// 获取传感器的服务，初始化传感器
				mSensorManager = (SensorManager) StepCounterService.this.getSystemService(SENSOR_SERVICE);
				// 注册传感器，注册监听器
				mSensorManager.registerListener(detector,
						mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_FASTEST);

			}
		}).start();


		// 电源管理服务
		mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
		mWakeLock.acquire();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, START_STICKY, startId);
	}


	@Override
	public void onDestroy() {
		doNotification();
		super.onDestroy();
		FLAG = false;// 服务停止
		if (detector != null) {
			mSensorManager.unregisterListener(detector);
		}

		if (mWakeLock != null) {
			mWakeLock.release();
		}
//		 Intent service = new Intent(this, StepCounterService.class);
//		 startService(service);
	}

	private void doNotification() {
		//manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		Builder builder=new Builder(this);
		builder.setContentTitle("健康小助手");
		builder.setContentText("今天走了"+StepDetector.CURRENT_SETP+"步");
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setWhen(System.currentTimeMillis());
		//常驻型广播
		builder.setOngoing(true);

		//manager.notify(1327405019, builder.build());
	}

}
