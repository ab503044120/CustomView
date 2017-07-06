package com.gangganghao.basegraph.ggh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Administrator on 2017/7/4.
 */

public class GradientCircleProgress extends View {
    private ValueAnimator mValueAnimator;
    private Point mCenterPoint;
    private Paint mPaint;
    private int radius;
    private float mAnimatedValue;
    private Paint mTextPaint;
    private Paint.FontMetrics mFontMetrics;
    private float centerPadding;
    private float mTextHeight;
    private float max = 0;
    private SweepGradient mSweepGradient0_1;
    private RectF mOvalRectF;
    private LinearGradient mLinearGradient0_1;
    private SweepGradient mSweepGradient1_2;
    private LinearGradient mLinearGradient1_2;

    public GradientCircleProgress(Context context) {
        this(context, null);
    }

    public GradientCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientCircleProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mCenterPoint = new Point(0, 0);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setStrokeCap(Paint.Cap.BUTT);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setFakeBoldText(true);
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(5000);
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = ((float) animation.getAnimatedValue());

                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterPoint.set(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        radius = getMeasuredWidth() / 3;
        mTextPaint.setTextSize(radius / 2);
        mFontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = mFontMetrics.bottom - mFontMetrics.top;
        centerPadding = mTextHeight / 2 - mFontMetrics.bottom;
        mSweepGradient0_1 = new SweepGradient(mCenterPoint.x, mCenterPoint.y, 0xfffea546, 0xfffb3131);
        mLinearGradient0_1 = new LinearGradient(0, mCenterPoint.y - mTextHeight / 2, 0, mCenterPoint.y + mTextHeight / 2
                , 0xffffe492, 0xfffb3131, Shader.TileMode.CLAMP);

        mSweepGradient1_2 = new SweepGradient(mCenterPoint.x, mCenterPoint.y, 0xfffb3131, 0xffc60b0b);
        mLinearGradient1_2 = new LinearGradient(0, mCenterPoint.y - mTextHeight / 2, 0, mCenterPoint.y + mTextHeight / 2
                , 0xfffb3131, 0xffc60b0b, Shader.TileMode.CLAMP);

        mOvalRectF = new RectF(mCenterPoint.x - radius, mCenterPoint.y - radius, mCenterPoint.x + radius, mCenterPoint.y + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimatedValue < 1.0f) {
            mPaint.setColor(0xfffea546);
            mPaint.setShader(mSweepGradient0_1);
            canvas.drawArc(mOvalRectF, 0, mAnimatedValue * 360, false, mPaint);

            mTextPaint.setShader(mLinearGradient0_1);
            canvas.drawText((int) (mAnimatedValue * 100) + "%", mCenterPoint.x, mCenterPoint.y + centerPadding, mTextPaint);
        } else if (mAnimatedValue < 2.0f) {
            mPaint.setColor(0xfffea546);
            mPaint.setShader(mSweepGradient0_1);
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaint);

            mPaint.setColor(0xfffb3131);
            mPaint.setShader(mSweepGradient1_2);
            canvas.drawArc(mOvalRectF, 0, (mAnimatedValue - 1.0f) * 360, false, mPaint);
            mTextPaint.setShader(mLinearGradient1_2);

            canvas.drawText((int) (mAnimatedValue * 100) + "%", mCenterPoint.x, mCenterPoint.y + centerPadding, mTextPaint);
        } else if (mAnimatedValue < 3.0f) {
            mPaint.setColor(0xfffb3131);
            mPaint.setShader(mSweepGradient1_2);
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaint);

            mPaint.setColor(0xffc60b0b);
            mPaint.setShader(null);
            canvas.drawArc(mOvalRectF, 0, (mAnimatedValue - 2.0f) * 360, false, mPaint);

            mTextPaint.setShader(null);
            mTextPaint.setColor(0xffc60b0b);
            canvas.drawText((int) (mAnimatedValue * 100) + "%", mCenterPoint.x, mCenterPoint.y + centerPadding, mTextPaint);
        } else {
            mPaint.setColor(0xffc60b0b);
            mTextPaint.setColor(0xffc60b0b);
            mTextPaint.setShader(null);
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, radius, mPaint);
            canvas.drawText((int) (mAnimatedValue * 100) + "%", mCenterPoint.x, mCenterPoint.y + centerPadding, mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation();
    }

    public void startAnimation() {
        if (radius != 0 && max != 0) {
            mValueAnimator.setFloatValues(0, max);
            mValueAnimator.start();
        }
    }
    public void setMax(float max) {
        this.max = max;
        startAnimation();

    }

}
