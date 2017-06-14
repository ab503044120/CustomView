package com.gangganghao.basegraph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/6/12.
 */

public class WaveView extends View {
    private int mRadio;
    private Path mPath;
    private Paint mPaint;
    private int mYOffset = 100;
    private int mXOffset;
    private PorterDuffXfermode mXfermode;
    private ValueAnimator mValueAnimator;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mValueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        mValueAnimator.setDuration(20000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mYOffset = (int) (500* ((float) animation.getAnimatedValue()));
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadio = getMeasuredWidth() / 4;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.TRANSPARENT);
        int layer = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        mPaint.setXfermode(null);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadio, mPaint);
        mPaint.setColor(Color.BLUE);
        mPaint.setXfermode(mXfermode);
        mPath.reset();
        float startY = -mYOffset + getMeasuredHeight() / 2 + mRadio;
        float startX = getMeasuredWidth() / 2 - mRadio;
        mPath.moveTo(startX, startY);
        mPath.rQuadTo(mRadio / 2, -mRadio / 2, mRadio, 0);
        mPath.rQuadTo(mRadio / 2, mRadio / 2, mRadio, 0);
        mPath.rLineTo(0, mRadio * 2.5f);
        mPath.rLineTo(-mRadio * 2, 0);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        Log.e("layer",layer + "-------");
        canvas.restoreToCount(layer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mValueAnimator.start();
        return super.onTouchEvent(event);
    }
}
