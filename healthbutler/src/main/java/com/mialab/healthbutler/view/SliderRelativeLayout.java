package com.mialab.healthbutler.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mialab.healthbutler.R;

public class SliderRelativeLayout extends RelativeLayout {
	private final static String TAG = "SliderRelativeLayout";
	private Context context;
	private Bitmap dragBitmap = null; //拖拽图片
	private int locationX = 0; //bitmap初始绘图位置，足够大，可以认为看不见
	private Handler handler = null; //信息传递
	private static int BACK_DURATION = 3 ;   // 回退动画时间间隔值  5ms
	private static float VE_HORIZONTAL = 2.0f ;  // 水平方向前进速率 1dip/ms
	private ImageView iv_slider_icon = null;

	public SliderRelativeLayout(Context context) {
		super(context);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	public SliderRelativeLayout(Context context, AttributeSet attrs,
								int defStyle) {
		super(context, attrs, defStyle);
		SliderRelativeLayout.this.context = context;
		intiDragBitmap();
	}

	/**
	 * 得到拖拽图片
	 */
	private void intiDragBitmap() {
		if(dragBitmap == null){
			dragBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.manual_run_slide);
		}
	}

	/**
	 * 这个方法里可以得到一个其他资源
	 */
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.iv_slider_icon = ((ImageView) findViewById(R.id.imageview_slide));
	}

	/**
	 * 对拖拽图片不同的点击事件处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int X = (int) event.getX();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				locationX = (int) event.getX();
				Log.i(TAG, "是否点击到位=" + isActionDown(event));
				return isActionDown(event);

			case MotionEvent.ACTION_MOVE:
				locationX = X;
				invalidate(); //重新绘图
				return true;

			case MotionEvent.ACTION_UP: //判断是否解锁成功
				if(!isLocked()){ //没有解锁成功,动画应该回退
					handleActionUpEvent(event); //动画回退
				}
				return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 回退动画(到开始的位置)
	 * @param event
	 */
	private void handleActionUpEvent(MotionEvent event) {
		int x = (int) event.getX();
		int toLeft = iv_slider_icon.getWidth();

		locationX = x - toLeft;
		if(locationX >= 0){
			handler.postDelayed(ImageBack, BACK_DURATION); //回退
		}
	}

	/**
	 * 未解锁时，图片回退
	 */
	private Runnable ImageBack = new Runnable() {
		@Override
		public void run() {
			locationX = locationX - (int) (VE_HORIZONTAL*BACK_DURATION);
			if(locationX >= 0){
				handler.postDelayed(ImageBack, BACK_DURATION); //回退,线程和线程等待的时间
				invalidate();
			}
		}
	};

	/**
	 * 判断是否点击到了滑动区域
	 * @param event
	 * @return
	 */
	private boolean isActionDown(MotionEvent event) {
		Rect rect = new Rect();
		iv_slider_icon.getHitRect(rect);
		//是否包含在iv_slider_icon里面
		boolean isIn = rect.contains((int)event.getX(), (int)event.getY());

		if(isIn){
			iv_slider_icon.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	/**
	 * 绘制拖动时的图片
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		invalidateDragImg(canvas);
	}

	/**
	 * 图片随手势移动
	 * @param canvas
	 */
	private void invalidateDragImg(Canvas canvas) {
		int drawX = locationX - dragBitmap.getWidth();
		int drawY = iv_slider_icon.getTop();
		//108
		if(drawX < iv_slider_icon.getWidth()){ //划到最左边
			iv_slider_icon.setVisibility(View.VISIBLE);
			return;
		} else {
			if(isLocked()){ //判断是否成功
				return;
			}
			iv_slider_icon.setVisibility(View.GONE);
			canvas.drawBitmap(dragBitmap, drawX < 0 ? 5 : drawX,drawY,null);
		}
	}

	/**
	 * 判断是否解锁
	 */
	private boolean isLocked(){
		if(locationX > (getScreenWidth() - iv_slider_icon.getWidth())){
			handler.sendEmptyMessage(1);
			//ShowMapActivity.iscontinue=true;
			return true;
		}
		return false;
	}

	private int getScreenWidth(){
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);

		return dm.widthPixels;
	}


	public void setMainHandler(Handler handler){
		this.handler = handler;
	}
}