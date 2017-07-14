package com.gangganghao.basegraph.ggh;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/7/6.
 */

public class RatioView extends View {
    private int mRadius;
    private PointF mCenterPointF;
    private Paint mPaint;
    private ArgbEvaluator mArgbEvaluator;
    private float mStrokeWidth;
    private float mRadio = -1;
    private ValueAnimator mValueAnimator;
    private float mAnimatedValue;
    private int mMidColor;
    private Paint mTextPaint;
    private RectF mRectF;
    private String mLeft;
    private String mRight;
    private float textOffset;
    private float mTextHeight;
    private int mRightColor = 0xffffc742;
    private int mLeftColor = 0xffcd2525;

    public RatioView(Context context) {
        this(context, null);
    }

    public RatioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mCenterPointF = new PointF(0, 0);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                getResources().getDisplayMetrics());
        mPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                getResources().getDisplayMetrics()));
        mTextPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f,
                getResources().getDisplayMetrics()));
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        mArgbEvaluator = new ArgbEvaluator();
        mValueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mMidColor = (int) mArgbEvaluator.evaluate(0.5f, mLeftColor, mRightColor);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom - fontMetrics.top;
        textOffset = mTextHeight / 2 - fontMetrics.bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterPointF.set(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
        mRadius = min / 3;
        mRectF = new RectF(mCenterPointF.x - mRadius, mCenterPointF.y - mRadius, mCenterPointF.x + mRadius, mCenterPointF.y + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRadio != 0) {
            mPaint.setColor(mRightColor);
            canvas.drawArc(mRectF, 0, mRadio / 2 * 360 * mAnimatedValue, false, mPaint);
            canvas.drawArc(mRectF, 1, -mRadio / 2 * 360 * mAnimatedValue - 2, false, mPaint);
        }
        if (1 - mRadio != 0) {
            mPaint.setColor(mLeftColor);
            canvas.drawArc(mRectF, 180, (1 - mRadio) / 2 * 360 * mAnimatedValue, false, mPaint);
            canvas.drawArc(mRectF, 180 + 1, -(1 - mRadio) / 2 * 360 * mAnimatedValue - 2, false, mPaint);
        }

        if (mAnimatedValue >= 1) {
            mTextPaint.setColor(mRightColor);
            float distance = mRadius + mStrokeWidth / 2;
            double radians = Math.toRadians(((1 - mRadio) > 0.5f ? mRadio : (1 - mRadio)) / 4 * 360);
            float x = (float) (mCenterPointF.x + Math.cos(radians) * distance);
            float y = (float) (mCenterPointF.y - Math.sin(radians) * distance);
            float endX = (float) (x + Math.cos(radians) * distance * 0.2f);
            float endY = (float) (y - Math.sin(radians) * distance * 0.2f);
            canvas.drawLine(x, y, endX, endY, mTextPaint);
            float dx = mTextPaint.measureText(mRight);
            float dx1 = mTextPaint.measureText((int)(mRadio * 100+0.5) + "%");
            dx = Math.max(dx, dx1) * 1.4f;
            canvas.drawLine(endX, endY, endX + dx, endY, mTextPaint);
            canvas.drawText((int)(mRadio * 100+0.5) + "%", endX + dx / 2, endY - textOffset, mTextPaint);
            canvas.drawText(mRight, endX + dx / 2, endY + mTextHeight - textOffset, mTextPaint);

            mTextPaint.setColor(mLeftColor);
            x = (float) (mCenterPointF.x - Math.cos(radians) * distance);
            y = (float) (mCenterPointF.y + Math.sin(radians) * distance);
            endX = (float) (x - Math.cos(radians) * distance * 0.2f);
            endY = (float) (y + Math.sin(radians) * distance * 0.2f);
            canvas.drawLine(x, y, endX, endY, mTextPaint);
            dx = mTextPaint.measureText(mLeft);
            dx1 = mTextPaint.measureText((int) ((sub(1d, Double.valueOf(mRadio + ""))) * 100 + 0.5)  + "%");
            dx = Math.max(dx, dx1) * 1.4f;
            canvas.drawLine(endX, endY, endX - dx, endY, mTextPaint);
            canvas.drawText((int) ((sub(1d, Double.valueOf(mRadio + ""))) * 100 + 0.5) + "%", endX - dx / 2, endY - textOffset, mTextPaint);
            canvas.drawText(mLeft, endX - dx / 2, endY + mTextHeight - textOffset, mTextPaint);
        }
    }

    public static double sub(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation();
    }

    public void startAnimation() {
        if (mRadio == -1 || mRadius == 0) {
            return;
        }
        mValueAnimator.start();
    }

    public void setRadio(float radio, String left, String right) {
        mRadio = radio;
        mLeft = left;
        mRight = right;
        requestLayout();
    }
}
