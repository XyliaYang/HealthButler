package com.mialab.healthbutler.utils;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//走步检测器，用于检测走步并计数
/**
 * 本算法是从谷歌Pedometer计步算法
 *
 */
@SuppressLint("NewApi")
public class StepDetector implements SensorEventListener {

	public static int CURRENT_SETP = 0;

	public static float SENSITIVITY = 6;   //SENSITIVITY灵敏度

	public static int CURRENT_SETPINBACK;

	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;
	//连续的八步才开始计算
	public static int tempStep=8;

	private static long t1=0;
	private static long t2=0;

	/**
	 * 最后加速度方向
	 */
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;

	public static int firstStep=0;

	/**
	 * 传入上下文的构造函数
	 *
	 * @param context
	 */
	public StepDetector(/*Context context*/) {
		super();
		int h = 480;
		mYOffset = h * 0.5f;
		//g=9.8，地球表面最大磁力
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
//		if (SettingsActivity.sharedPreferences == null) {
//			SettingsActivity.sharedPreferences = context.getSharedPreferences(
//					SettingsActivity.SETP_SHARED_PREFERENCES,
//					Context.MODE_PRIVATE);
//		}
		SENSITIVITY=7;
//		SENSITIVITY = SettingsActivity.sharedPreferences.getInt(
//				SettingsActivity.SENSITIVITY_VALUE, 6);//默认灵敏度为6
	}

	@SuppressLint("NewApi")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// Log.i(Constant.STEP_SERVER, "StepDetector");
		Sensor sensor = event.sensor;
		// Log.i(Constant.STEP_DETECTOR, "onSensorChanged");
		synchronized (this) {
			//系统的方向传感器
			if (sensor.getType() == Sensor.TYPE_ORIENTATION)
			{

			}
			else {
				int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
				//加速传感器
				if (j == 1) {
					float vSum = 0;
					for (int i = 0; i < 3; i++) {
						final float v = mYOffset + event.values[i] * mScale[j];
						vSum += v;
					}
					int k = 0;
					float v = vSum / 3;

					float direction = (v > mLastValues[k] ? 1: (v < mLastValues[k] ? -1 : 0));
					if (direction == -mLastDirections[k]) {
						// Direction changed
						int extType = (direction > 0 ? 0 : 1); // minumum or
						// maximum?
						mLastExtremes[extType][k] = mLastValues[k];
						float diff = Math.abs(mLastExtremes[extType][k]- mLastExtremes[1 - extType][k]);

						if (diff > SENSITIVITY) {
							boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
							boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
							boolean isNotContra = (mLastMatch != 1 - extType);

							if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
								end = System.currentTimeMillis();
								/***
								 *
								 * 计步
								 */
								//相隔四毫秒以上
								if (end - start > 420) {// 此时判断为走了一步

									t1=System.currentTimeMillis();
									//超过两秒五未走，则停止走动
									if(t1-t2>=2500)
									{
										firstStep=0;
										tempStep=8;

									}

//									//过了0.8到1.0未走动，衰退
//									else if(t1-t2>800&&t1-t2<1000)
//									{
//										firstStep=6;
//										tempStep=1;
//									}
									//过了1.8到2未走动，衰退
									else if(t1-t2>1800&&t1-t2<2000)
									{
										firstStep=4;
										tempStep=3;
									}
									//过了2到2.5未走动，衰退
									else if(t1-t2>=2000&&t1-t2<2500)
									{
										firstStep=2;
										tempStep=5;
									}
									t2=System.currentTimeMillis();

									//行走八步开始计算
									if(firstStep>=8)
									{
										CURRENT_SETP++;
										CURRENT_SETP+=tempStep;
										CURRENT_SETPINBACK=CURRENT_SETP;
										tempStep=0;
									}
									if(firstStep<8)
										firstStep++;
									System.out.println("CURRENT_Setp---->"+CURRENT_SETP);
									System.out.println("first_Setp---->"+firstStep);
									System.out.println(System.currentTimeMillis());
									mLastMatch = extType;
									start = end;


								}
							} else {
								mLastMatch = -1;
								//firstStep=0;
							}
						}
						mLastDiff[k] = diff;
					}
					mLastDirections[k] = direction;
					mLastValues[k] = v;
				}
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

}
