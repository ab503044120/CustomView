package com.gangganghao.basegraph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * User: Administrator
 * Date: 2016-10-21 {HOUR}:05
 */
public class CanvasView extends View {
    private Paint mPaint;
    private Path mPath;
    private int width;
    private int height;
    private Bitmap mCacheBitmap;
    private Canvas mCanvas;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10f);
        mPath = new Path();
        mPaint.setStyle(Paint.Style.STROKE);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (mCanvas == null) {
            mCanvas = new Canvas();

            mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas.setBitmap(mCacheBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//      canvas.drawBitmap(mCacheBitmap,0,0,mPaint);
//        canvas.drawPath(mPath,mPaint);
//        RectF oval = new RectF(0,0,100,100);
//        canvas.drawOval(oval,mPaint);

//        canvas.drawCircle(width/2,height/2,200,mPaint);
//        RectF rectF = new RectF(0,0,width,width);
//        canvas.drawRect(rectF,mPaint);
//        mPaint.setColor(Color.BLUE);
//        canvas.drawArc(rectF,20,100,true,mPaint);

//        canvasOperation(canvas);
        //画时钟
//        drawClock(canvas);


    }

    private void drawClock(Canvas canvas) {
        float radioClock = 250;
        float radioNumber = 25;
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.rotate(-90);
        for (double i = 0; i < 360; i += 360 / 12) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.YELLOW);
            canvas.drawCircle(radioClock, 0, radioNumber, mPaint);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(20);
            mPaint.setColor(Color.BLUE);
            canvas.drawText(((int) (i*12/360)+1) + "",radioClock,0,mPaint);
            canvas.rotate(360 / 12);
        }
    }

    private void canvasOperation(Canvas canvas) {
        //移动中心点
        //画笔轨迹是 left + stroke/2
        canvas.drawCircle(15, 15, 10, mPaint);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.drawCircle(getLeft() + 0, getTop() + 0, 10, mPaint);
        //画布放缩

        canvas.scale(0.5f, 0.5f);
        canvas.drawCircle(0, 0, 100, mPaint);

        canvas.scale(0.5f, 0.5f);
        canvas.drawCircle(0, 0, 100, mPaint);

        canvas.scale(0.5f, 0.5f);
        canvas.drawCircle(0, 0, 100, mPaint);

        canvas.scale(10f, 10f);
        canvas.drawCircle(0, 0, 100, mPaint);

        //旋转
        canvas.rotate(180);

        canvas.drawCircle(getLeft() + 0, getTop() + 0, 10, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPath.moveTo(event.getRawX(),event.getRawY());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mPath.lineTo(event.getRawX(),event.getRawY());
//                break;
//            case MotionEvent.ACTION_UP:
//                mCanvas.drawPath(mPath,mPaint);
//                break;
//        }
//        invalidate();
        return super.onTouchEvent(event);
    }
}