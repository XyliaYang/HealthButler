package com.mialab.healthbutler.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mialab.healthbutler.service.StepCounterService;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if(intent.getAction().equals( Intent.ACTION_BOOT_COMPLETED ))
		{
			Intent intent2=new Intent(context,StepCounterService.class);
			context.startService(intent2);

//			intent2.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//			//使用Receiver直接启动Activity时候需要加入此flag，否则系统会出现异常
//			context.startActivity(intent2);
		}
	}

}
