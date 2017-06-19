package com.gangganghao.basegraph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */

public class WheelView extends View {

    private int textSize = 50;
    private int centerTextColor = 0xff333333;
    private int otherTextColor = 0xff999999;
    private Paint mCenterPaint;
    private Paint mOtherPaint;
    private float textPadding;
    private PointF centerPoint;
    private List<String> mStrings;
    //最初的空白高度
    private int spaceHeight;
    private StaticLayout mStaticLayout;
    private float mCenterTextOffset;
    private float mOtherTextOffset;
    //文字开始画的y值
    private float startY;
    private int mCenterTextHeight;
    private ValueAnimator mValueAnimator;
    //当前被选中的index
    private int curentIndex;

    private int mVisibleItem;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        mStrings = new ArrayList<>();
        mStrings.add("湖南");
        mStrings.add("北京");
        mStrings.add("乌鲁木齐");
        mStrings.add("新疆维吾尔自治区");
        mStrings.add("西藏");
        mStrings.add("阿里巴巴");
        mStrings.add("腾讯");
        mStrings.add("宁夏回族自治区");
        mStrings.add("这是一个超长的东西啊啊啊啊啊啊啊");
    }

    private void init() {
        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setTextSize(textSize);
        mCenterPaint.setColor(centerTextColor);
        mCenterPaint.setTextAlign(Paint.Align.CENTER);

        mOtherPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOtherPaint.setTextSize(textSize * 0.9f);
        mOtherPaint.setColor(otherTextColor);
        mOtherPaint.setTextAlign(Paint.Align.CENTER);

        mValueAnimator = ValueAnimator.ofFloat(0, 1800f);
        mValueAnimator.setDuration(20000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                startY = getMeasuredHeight() / 2 - animatedValue;
                invalidate();
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerPoint = new PointF();
        centerPoint.x = w / 2;
        centerPoint.y = h / 2;
        spaceHeight = w / 2;
        Paint.FontMetrics centerFontMetrics = mCenterPaint.getFontMetrics();
        Paint.FontMetrics otherFontMetrics = mOtherPaint.getFontMetrics();
        mCenterTextHeight = (int) (-centerFontMetrics.top + centerFontMetrics.bottom);
        mCenterTextOffset = mCenterTextHeight / 2 - centerFontMetrics.bottom;

        int otherHeight = (int) (-otherFontMetrics.top + otherFontMetrics.bottom);
        mOtherTextOffset = otherHeight / 2 - otherFontMetrics.bottom;

        textPadding = mCenterTextHeight * 0.5f;

        startY = centerPoint.y;

        mVisibleItem = (int) (h / 2 / (mCenterTextHeight + textPadding)) + 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, centerPoint.y, getMeasuredWidth(), centerPoint.y, mOtherPaint);
        float traslateY = 0;
//            float abs = Math.abs(startY);
        //滑动的距离
        float dy = centerPoint.y - startY;
        curentIndex = (int) ((dy - textPadding) / (mCenterTextHeight + textPadding)) + 1;

        int startIndex = 0;
        int endIndex = mStrings.size() - 1;
        //如果已经滚过了足够的item
        if (curentIndex >= mVisibleItem) {
            startIndex = curentIndex = mVisibleItem;
        }
        if (mStrings.size() - curentIndex > mVisibleItem) {
            endIndex = curentIndex + mVisibleItem;
        }
        int index = startIndex;
        Log.e("index", "开始:" + index);
        while (index <= endIndex) {
            canvas.save();
            canvas.translate(0, traslateY);
            if (startY + traslateY > -mCenterTextHeight / 2 + centerPoint.y &&
                    startY + traslateY < mCenterTextHeight / 2 + centerPoint.y) {
                //中心地带
                canvas.drawText(mStrings.get(index), centerPoint.x, startY + mCenterTextOffset, mCenterPaint);
            } else {
                canvas.drawText(mStrings.get(index), centerPoint.x, startY + mCenterTextOffset, mOtherPaint);
            }
            canvas.restore();
            traslateY += mCenterTextHeight + textPadding;
            index++;
        }
        Log.e("index", "结束:" + index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mValueAnimator.start();
//        startY = getMeasuredHeight() / 2 - 1800;
//        invalidate();
        return super.onTouchEvent(event);
    }
}
