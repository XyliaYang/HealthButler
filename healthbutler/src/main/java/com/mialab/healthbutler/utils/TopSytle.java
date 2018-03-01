package com.mialab.healthbutler.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class TopSytle {
	
	/**
	 *    ����״̬����color
	 * 
	 * @param activity Ҫ���õ�activity
	 * @param color	        Ҫ���õ���ɫ
	 */
	public static void setColor(Activity activity,int color){
		//android 4.4�汾����
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
		{
			//����״̬��͸��
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//����һ��״̬����С�ľ���
			View statusView=createStatusView(activity,color);
			//��statusView�ӵ�����
			ViewGroup decorView=(ViewGroup)activity.getWindow().getDecorView();
			decorView.addView(statusView);
			//���ø����ֵĲ���
			ViewGroup rootView=(ViewGroup)((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setFitsSystemWindows(true);
			rootView.setClipToPadding(true);
		}
	}
	
	/**
	 * ���ɺ�״̬��һ����С�ľ�����
	 * @param activity Ҫ���õ�activity
	 * @param color    ����color
	 * @return
	 */
	public static View createStatusView(Activity activity,int color){
		//���״̬���߶�
		int resourceId=activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		int statusBarHeight=activity.getResources().getDimensionPixelSize(resourceId);
		//����һ����״̬��һ���ߵľ���
		View statusView =new View(activity);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
		statusView.setLayoutParams(params);
		statusView.setBackgroundColor(color);
		return statusView;
	}
	
	/**
     * ʹ״̬��͸��
     * 
     * ������ͼƬ��Ϊ�����Ľ���,��ʱ��ҪͼƬ��䵽״̬��
     *
     * @param activity ��Ҫ���õ�activity
     */
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // ����״̬��͸��
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // ���ø����ֵĲ���
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

}
