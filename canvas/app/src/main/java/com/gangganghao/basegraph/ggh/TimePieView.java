package com.gangganghao.basegraph.ggh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class TimePieView extends View {
    private int mMaxRadius;
    private Paint mPaint;
    private Paint mGrayCirclePaint;
    private Paint mArcPaint;
    private PointF mCenterPointF;
    private RectF mRectF;
    private Paint mTextPaint;
    private float textOffset;
    private List<Long> mDatas;
    private long mMax;
    private int[] colors = {0x7fe10e0c, 0x7f1797fb, 0x7f0fecdf, 0x7ffce62a};
    private float mAngleStep;
    private ValueAnimator mValueAnimator;
    private float mAnimatedValue;

    public TimePieView(Context context) {
        this(context, null);
    }

    public TimePieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(0xff2270ac);
        mPaint.setStyle(Paint.Style.STROKE);

        mGrayCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGrayCirclePaint.setStyle(Paint.Style.STROKE);
        mGrayCirclePaint.setColor(0xffc1c1c1);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(36);
        mTextPaint.setColor(0xff333333);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        textOffset = textHeight / 2 - fontMetrics.bottom;
        mDatas = new ArrayList<>();
        mDatas.add(100L);
        mDatas.add(200L);
        mDatas.add(500L);
        mDatas.add(1000L);
        mDatas.add(1000L);
        mDatas.add(900L);
        mDatas.add(100L);
        mDatas.add(800L);
        mDatas.add(1100L);
        mDatas.add(500L);
        mDatas.add(1540L);
        mDatas.add(1540L);
        setData(mDatas);
        mValueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
        mMaxRadius = min * 2 / 3 / 2;
        mCenterPointF = new PointF(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius, mGrayCirclePaint);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius * (1 - 2 / 9f), mGrayCirclePaint);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius * (1 - 4 / 9f), mGrayCirclePaint);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius * (1 - 4 / 9f), mGrayCirclePaint);

        float startAngle = 0;
        float base = mMaxRadius / 3f;

        for (int i = 0; i < mDatas.size(); i++) {
            mArcPaint.setColor(colors[i % 4]);
            mRectF.left = mCenterPointF.x - base - mMaxRadius * 2 / 3 * mDatas.get(i) / mMax * mAnimatedValue;
            mRectF.top = mCenterPointF.y - base - mMaxRadius * 2 / 3 * mDatas.get(i) / mMax * mAnimatedValue;
            mRectF.right = mCenterPointF.x + base + mMaxRadius * 2 / 3 * mDatas.get(i) / mMax * mAnimatedValue;
            mRectF.bottom = mCenterPointF.y + base + mMaxRadius * 2 / 3 * mDatas.get(i) / mMax * mAnimatedValue;
            canvas.drawArc(mRectF, 180 + startAngle * mAnimatedValue, mAngleStep * mAnimatedValue, true, mArcPaint);
            startAngle += mAngleStep;
        }

        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius / 3f, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius / 3f - 5f, mPaint);

        mPaint.setColor(0xff2270ac);
        mPaint.setStrokeWidth(4);
        float startXL = mCenterPointF.x - mMaxRadius / 3f;
        float startXR = mCenterPointF.x + mMaxRadius / 3f;
        canvas.drawLine(startXL, mCenterPointF.y, startXL + 10 + 5, mCenterPointF.y, mPaint);
        canvas.drawLine(startXR, mCenterPointF.y, startXR - 10 - 5, mCenterPointF.y, mPaint);
        canvas.drawText("9", startXL + 10 + 5 + 5, mCenterPointF.y + textOffset, mTextPaint);
        canvas.drawText("3", startXR - 10 - 5 - mTextPaint.measureText("3") - 5, mCenterPointF.y + textOffset, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mValueAnimator.start();
        return super.onTouchEvent(event);
    }

    public void setData(List<Long> datas) {
        mDatas = datas;
        mMax = Collections.max(mDatas);
        mAngleStep = 360 / mDatas.size();
    }

}
