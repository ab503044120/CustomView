package com.gangganghao.basegraph;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * User: Administrator
 * Date: 2016-11-02 {HOUR}:32
 */
public class LoadingView extends View {

    private Paint mPaint;
    private int mViewWidth;
    private int mViewHeight;
    private Path mPath;
    private ValueAnimator mValueAnimator;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);


        mPath = new Path();
        mPath.addCircle(0, 0, 100, Path.Direction.CCW);

        mValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                invalidate();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
                                       @Override
                                       public void onAnimationStart(Animator animation) {
                                       }

                                       @Override
                                       public void onAnimationEnd(Animator animation) {
                                       }

                                       @Override
                                       public void onAnimationCancel(Animator animation) {

                                       }

                                       @Override
                                       public void onAnimationRepeat(Animator animation) {

                                       }
                                   }
        );
        mValueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xff0082D7);
        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        float fraction = (float) mValueAnimator.getAnimatedValue();
        PathMeasure pathMeasure = new PathMeasure(mPath, false);
        float startD = pathMeasure.getLength() * fraction;
        float endD = (float) (startD + (0.5f - Math.abs(0.5 - fraction)) * 200f);

        Path dst = new Path();
        ;

        Log.e("LoadingView", startD + "   " + endD + "   " + pathMeasure.getSegment(startD, endD, dst, true));

        canvas.drawPath(dst, mPaint);

    }
}