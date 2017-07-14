package com.gangganghao.basegraph.ggh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

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
    private float mInnerCircleStroke = 10;

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
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11,
                getResources().getDisplayMetrics()));
        mTextPaint.setColor(0xff333333);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        textOffset = textHeight / 2 - fontMetrics.bottom;

        mValueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mInnerCircleStroke = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
        mMaxRadius = min * 7 / 10 / 2;
        mCenterPointF = new PointF(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius, mGrayCirclePaint);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius * (1 - 2 / 9f), mGrayCirclePaint);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius * (1 - 4 / 9f), mGrayCirclePaint);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius * (1 - 4 / 9f), mGrayCirclePaint);

        if (mDatas != null && !mDatas.isEmpty() && mMax != 0) {
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
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mInnerCircleStroke);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius / 3f, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterPointF.x, mCenterPointF.y, mMaxRadius / 3f - 5f, mPaint);

        mPaint.setColor(0xff2270ac);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mInnerCircleStroke * 0.4f);
        float startXL = mCenterPointF.x - mMaxRadius / 3f;
        float startXR = mCenterPointF.x + mMaxRadius / 3f;
        canvas.drawLine(startXL, mCenterPointF.y, startXL + mInnerCircleStroke + mInnerCircleStroke / 2, mCenterPointF.y, mPaint);
        canvas.drawLine(startXR, mCenterPointF.y, startXR - mInnerCircleStroke - mInnerCircleStroke / 2, mCenterPointF.y, mPaint);

        canvas.drawText("9"
                , startXL + mInnerCircleStroke + mInnerCircleStroke / 2 + mInnerCircleStroke / 2, mCenterPointF.y + textOffset, mTextPaint);
        canvas.drawText("3"
                , startXR - mInnerCircleStroke - mInnerCircleStroke / 2 - mInnerCircleStroke / 2 - mTextPaint.measureText("3"), mCenterPointF.y + textOffset, mTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation();
    }

    public void startAnimation() {
        if (mDatas == null || mDatas.isEmpty() || mMaxRadius == 0) {
            return;
        }
        mValueAnimator.start();
    }

    public void setData(List<Long> datas) {
        mDatas = datas;
        mMax = Collections.max(mDatas);
        mAngleStep = 360 / mDatas.size();
        requestLayout();
    }

}
