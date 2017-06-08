package com.gangganghao.basegraph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/6/8.
 */

public class SearchViewV2 extends View {
    private int maxLen;
    private int cirleRadio;
    private int slantLen;
    private Path mPath;
    private RectF mRectF;
    private Paint mPaint;
    private float progress;
    private double mSqrt;
    private ValueAnimator mValueAnimator;

    public SearchViewV2(Context context) {
        this(context, null);
    }

    public SearchViewV2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchViewV2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPath = new Path();
        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffffffff);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mValueAnimator = ValueAnimator.ofFloat(0, 100f);
        mValueAnimator.setDuration(800).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxLen = getMeasuredWidth() * 3 / 4;
        cirleRadio = getMeasuredHeight() / 20;
        slantLen = (int) (cirleRadio * 1.5);
        mRectF.left = getMeasuredWidth() / 2 - cirleRadio;
        mRectF.top = getMeasuredHeight() / 2 - cirleRadio;
        mRectF.right = getMeasuredWidth() / 2 + cirleRadio;
        mRectF.bottom = getMeasuredHeight() / 2 + cirleRadio;
        mPath.addArc(mRectF, 45, -360);
        mPath.moveTo(getMeasuredWidth() / 2 + (float) (cirleRadio * Math.sin(Math.toRadians(45)))
                , getMeasuredHeight() / 2 + (float) (cirleRadio * Math.cos(Math.toRadians(45))));
        mPath.rLineTo((float) (slantLen * Math.sin(Math.toRadians(45)))
                , (float) (slantLen * Math.cos(Math.toRadians(45))));
        mSqrt = Math.sqrt(cirleRadio * cirleRadio * 2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        caculatePath(progress);
        canvas.drawPath(mPath, mPaint);
    }

    public void caculatePath(float progress) {
        mPath.reset();
        float startX = progress / 100.0f * maxLen * 0.5f + getMeasuredWidth() / 2;
        int startY = getMeasuredHeight() / 2;
        mPath.moveTo(startX, startY);
        mPath.rLineTo(-maxLen * progress / 100f, 0);
        mPath.moveTo(startX, startY);
        if (progress < 50) {
            mPath.rLineTo(-(float) (slantLen * Math.sin(Math.toRadians(45)))
                    , -(float) (slantLen * Math.cos(Math.toRadians(45))));
            mRectF.left = (float) (startX - slantLen * Math.sin(Math.toRadians(45))
                    - (mSqrt + cirleRadio) * Math.sin(Math.toRadians(45)));
            mRectF.top = (float) (startY - slantLen * Math.cos(Math.toRadians(45))
                    - (mSqrt + cirleRadio) * Math.cos(Math.toRadians(45)));
            mRectF.right = (float) (startX - slantLen * Math.sin(Math.toRadians(45))
                    + (mSqrt - cirleRadio) * Math.sin(Math.toRadians(45)));
            mRectF.bottom = (float) (startY - slantLen * Math.cos(Math.toRadians(45))
                    + (mSqrt - cirleRadio) * Math.cos(Math.toRadians(45)));
            mPath.addArc(mRectF, 45, -360 * (50 - progress) / 50f);
        } else {
            mPath.rLineTo(-(float) (slantLen * Math.sin(Math.toRadians(45)) * (100 - progress) / 50.0f)
                    , -(float) (slantLen * Math.cos(Math.toRadians(45))) * (100 - progress) / 50.0f);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mValueAnimator.start();
        return super.onTouchEvent(event);
    }
}
