package com.mialab.healthbutler.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mialab.healthbutler.activity.HistoryStepShowActivitys;

public class HistoryStepShowView extends View {

	private Paint paint;
	private Boolean isTouching = false;
	private float touchX = 0, touchY = 0;
	private int stepcount[]={0,0,0,0,0,0,0};

	public HistoryStepShowView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HistoryStepShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HistoryStepShowView(Context context, AttributeSet attrs,
							   int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		stepcount= HistoryStepShowActivitys.sevendayStep;

		paint = new Paint();

		paint.setAntiAlias(true);// 锯齿
		paint.setTextSize(50);
		paint.setColor(Color.argb(200, 179, 179, 179));
		canvas.drawText("历史步数统计", 200, 150, paint);

		// 坐标轴
		paint.setColor(Color.argb(200, 239, 239, 239));
		paint.setStrokeWidth(2);
		canvas.drawLine(200, 200, 200, 800, paint);
		canvas.drawLine(195, 800, 1000, 800, paint);

		// 刻度
		paint.setStrokeWidth(2);
		canvas.drawLine(300, 800, 300, 790, paint);
		canvas.drawLine(400, 800, 400, 790, paint);
		canvas.drawLine(500, 800, 500, 790, paint);
		canvas.drawLine(600, 800, 600, 790, paint);
		canvas.drawLine(700, 800, 700, 790, paint);
		canvas.drawLine(800, 800, 800, 790, paint);

		canvas.drawLine(200, 300, 1000, 300, paint);
		canvas.drawLine(200, 400, 1000, 400, paint);
		canvas.drawLine(200, 500, 1000, 500, paint);
		canvas.drawLine(200, 600, 1000, 600, paint);
		canvas.drawLine(200, 700, 1000, 700, paint);

		// 刻度数
		paint.setTextSize(40);
		paint.setColor(Color.argb(200, 120, 110, 120));
		paint.setStrokeWidth(5);
		for (int i = 0; i < 8; i++) {
			canvas.drawText(i + "", 193 + i * 100, 850, paint);
		}
		canvas.drawText("天数", 193 + 8 * 100-50, 850, paint);

		paint.setTextSize(30);
		paint.setColor(Color.argb(200, 179, 179, 179));
		paint.setStrokeWidth(5);
		for (int i = 0; i < 5; i++) {
			canvas.drawText((i + 1) * 2000 + "", 104, 220 + (5 - i) * 100,
					paint);
		}
		canvas.drawText("计步", 104, 220 + (5 - 5) * 100, paint);

		//绘制历史步数曲线
		Path path = new Path();
		paint.setStrokeWidth(5);
		paint.setColor(Color.argb(200, 51, 195, 201));
		paint.setStyle(Paint.Style.STROKE);
		path.moveTo(300, 800-stepcount[0]/20);
		for(int i=1;i<stepcount.length;i++)
		{
			path.lineTo(300+i*100, 800-stepcount[i]/20);
		}
//		path.lineTo(400, 600);
//		path.lineTo(500, 400);
//		path.lineTo(600, 500);
//		path.lineTo(700, 300);
//		path.lineTo(800, 558);
//		path.lineTo(900, 600);
		canvas.drawPath(path, paint);

		//绘制历史步数的点
		paint.setColor(Color.rgb(10, 192, 198));
		paint.setStrokeWidth(15);
		for(int i=0;i<stepcount.length;i++)
		{
			canvas.drawCircle(300+i*100, 800-stepcount[i]/20, 8, paint);
		}
//		canvas.drawCircle(300, 500, 8, paint);
//		canvas.drawCircle(400, 600, 8, paint);
//		canvas.drawCircle(500, 400, 8, paint);
//		canvas.drawCircle(600, 500, 8, paint);
//		canvas.drawCircle(700, 300, 8, paint);
//		canvas.drawCircle(800, 555, 8, paint);
//		canvas.drawCircle(900, 600, 8, paint);

//		//绘制运动信息
//		paint.setTextSize(30);
//		paint.setColor(Color.argb(200, 179, 179, 179));
//		paint.setStrokeWidth(2);
//		paint.setSubpixelText(true);
//		int width = this.getWidth() / 6;
//
//		canvas.drawText("步数", width - 50, 1000, paint);
//		canvas.drawText("热量", width * 3 - 50, 1000, paint);
//		canvas.drawText("距离", width * 5 - 50, 1000, paint);
//
//		canvas.drawLine(width * 2, 1000, width * 2, 1150, paint);
//		canvas.drawLine(width * 4, 1000, width * 4, 1150, paint);
//
//		canvas.drawText("步", width - 40, 1180, paint);
//		canvas.drawText("千卡", width * 3 - 50, 1180, paint);
//		canvas.drawText("二里", width * 5 - 50, 1180, paint);
//
//		paint.reset();
//		paint.setTextSize(80);
//		paint.setColor(Color.argb(200, 80, 80, 80));
//		paint.setAntiAlias(true);
//		canvas.drawText("5712", width - 100, 1100, paint);
//		canvas.drawText("327.7", width * 3 - 100, 1100, paint);
//		canvas.drawText("4.39", width * 5 - 100, 1100, paint);


		//绘制触摸时的
		if (isTouching) {

			paint.setColor(Color.argb(200, 200, 200, 200));
			paint.setStrokeWidth(1);
			canvas.drawLine(touchX, 200, touchX, 800, paint);
			paint.setTextSize(50);
			paint.setColor(Color.argb(255, 220, 220, 220));
			if ((touchX > 292 && touchX < 308))
			{
				canvas.drawText(stepcount[0]+"步", touchX+10, 250, paint);
			}
			else if(touchX > 392 && touchX < 408)
			{
				canvas.drawText(stepcount[1]+"步", touchX+10, 250, paint);
			}
			else if(touchX > 492 && touchX < 508)
			{
				canvas.drawText(stepcount[2]+"步", touchX+10, 250, paint);
			}
			else if(touchX > 592 && touchX < 608)
			{
				canvas.drawText(stepcount[3]+"步", touchX+10, 250, paint);
			}
			else if(touchX > 692 && touchX < 708)
			{
				canvas.drawText(stepcount[4]+"步", touchX+10, 250, paint);
			}
			else if(touchX > 792 && touchX < 808)
			{
				canvas.drawText(stepcount[5]+"步", touchX+10, 250, paint);
			}
			else if(touchX > 892 && touchX < 908)
			{
				canvas.drawText(stepcount[6]+"步", touchX+10, 250, paint);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		this.touchX = x;
		this.touchY = y;

		if (event.getAction() == MotionEvent.ACTION_UP) {
			isTouching = false;
		}

		if (!(x >= 200 && x <= 900 && y >= 200 & y <= 800)) {
			return false;
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isTouching = true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			isTouching = true;
		}

		postInvalidate();
		return true;
	}
}
