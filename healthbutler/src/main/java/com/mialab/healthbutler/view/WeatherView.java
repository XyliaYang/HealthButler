package com.mialab.healthbutler.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.mialab.healthbutler.activity.WeatherActivity;

import java.util.List;


@SuppressLint("DrawAllocation")
public class WeatherView extends View {

    //控件
    private int width, height;
    //点的偏移量
    private int offSetX, offSetY;
    //单位长度
    private int unit;

    private Paint paint;

    private int high[] = {20, 15, 18, 26, 18, 22};
    private int low[] = {10, 12, 8, 12, 2, 20};
    List<Integer> highList = WeatherActivity.hightempList;
    List<Integer> lowList = WeatherActivity.lowtempList;


    public WeatherView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public WeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        if (!WeatherActivity.loadcomplete) {
            return;
        }

        highList = WeatherActivity.hightempList;
        lowList = WeatherActivity.lowtempList;
        //1获取布局的宽和高
        width = getWidth();
        height = getHeight();
        offSetX = width / 12;
        offSetY = height / 2;
        unit = height / 50;

        paint = new Paint();
        paint.setAntiAlias(true);// 锯齿
        paint.setTextSize(50);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(15);
        for (int i = 0; i < 6; i++) {
            canvas.drawCircle(offSetX * (2 * i + 1), offSetY - (highList.get(i) - 15) * unit, 8, paint);
            canvas.drawCircle(offSetX * (2 * i + 1), offSetY - (lowList.get(i) - 15) * unit, 8, paint);
        }

        Path pathhigh = new Path();
        Path pathlow = new Path();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);

        pathhigh.moveTo(offSetX, offSetY - (highList.get(0) - 15) * unit);
        pathlow.moveTo(offSetX, offSetY - (lowList.get(0) - 15) * unit);

        for (int i = 1; i < 6; i++) {
            pathhigh.lineTo(offSetX * (2 * i + 1), offSetY - (highList.get(i) - 15) * unit);
            pathlow.lineTo(offSetX * (2 * i + 1), offSetY - (lowList.get(i) - 15) * unit);
        }

        canvas.drawPath(pathhigh, paint);
        canvas.drawPath(pathlow, paint);


        paint.setTextSize(40);
        paint.setStrokeWidth(3);
        for (int i = 0; i < 6; i++) {
            canvas.drawText(highList.get(i) + "°", offSetX * (2 * i + 1) - 20, offSetY - (highList.get(i) - 15) * unit - 30, paint);
            canvas.drawText(lowList.get(i) + "°", offSetX * (2 * i + 1) - 20, offSetY - (lowList.get(i) - 15) * unit + 60, paint);
        }
    }

}
