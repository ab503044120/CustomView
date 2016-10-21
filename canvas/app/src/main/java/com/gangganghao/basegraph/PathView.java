package com.gangganghao.basegraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: Administrator
 * Date: 2016-10-21 {HOUR}:25
 */
public class PathView extends View {
    private Paint mPaint;

    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);


        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        canvas.drawLine(-getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, 0, mPaint);
        canvas.drawLine(0, -getMeasuredHeight() / 2, 0, getMeasuredHeight() / 2, mPaint);


        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        Path path = new Path();
        path.moveTo(0,0);
        path.lineTo(10,20);
        path.lineTo(20,30);
//        path.addCircle(0,0,20, Path.Direction.CCW);
//        path.addArc(new RectF(0,0,100,100),0,90);
        path.arcTo(new RectF(0,0,40,40),0,90,false);
//        path.close();
        canvas.drawPath(path,mPaint);

//        path.addCircle(0,0,50, Path.Direction.CCW);
//        Path src = new Path();
//        src.addRect(-20,-40,20,40, Path.Direction.CCW);
//
//        path.addPath(src,0,20);

        canvas.drawPath(path, mPaint);
    }
}