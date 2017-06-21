package com.gangganghao.basegraph;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */

public class WheelView extends View {

    private static final String TAG = "WheelView";
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
    private float moveDistance;
    private int mCenterTextHeight;
    private ValueAnimator mValueAnimator;
    //当前被选中的index
    private int curentIndex;

    private int mVisibleItem;
    private Scroller mScroller;
    private SimpleOnGestureListener mListener;
    private GestureDetector mGestureDetector;
    private float lastFlingDistance;
    private boolean isFling;
    private ValueAnimator adjustValueAnimator;
    private float lastAdjustDistance;
    private boolean isAdjust;

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

        mStrings.add("湖南");
        mStrings.add("北京");
        mStrings.add("乌鲁木齐");
        mStrings.add("新疆维吾尔自治区");
        mStrings.add("西藏");
        mStrings.add("阿里巴巴");
        mStrings.add("腾讯");
        mStrings.add("宁夏回族自治区");
        mStrings.add("这是一个超长的东西啊啊啊啊啊啊啊");

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

        mValueAnimator = new ValueAnimator();
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.setDuration(200);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                float dy = animatedValue - lastFlingDistance;
                lastFlingDistance = animatedValue;
                Log.e("dy", "onAnimationUpdate: " + dy);
                moveDistance -= dy;
                startY = centerPoint.y - moveDistance;
                invalidate();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isFling = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFling = false;
                adjust();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isFling = false;
                adjust();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        adjustValueAnimator = new ValueAnimator();
        adjustValueAnimator.setInterpolator(new DecelerateInterpolator());
        adjustValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                float dy = animatedValue - lastAdjustDistance;
                lastAdjustDistance = animatedValue;
                moveDistance -= dy;
                startY = centerPoint.y - moveDistance;
                invalidate();
            }
        });
        adjustValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAdjust = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAdjust = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mListener = new GestureListener();
        mGestureDetector = new GestureDetector(getContext(), mListener);
        mGestureDetector.setIsLongpressEnabled(false);

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

        if (moveDistance > (mStrings.size() - 1) * (mCenterTextHeight + textPadding)) {
            startY = centerPoint.y - (mStrings.size() - 1) * (mCenterTextHeight + textPadding);
            moveDistance = (mStrings.size() - 1) * (mCenterTextHeight + textPadding);
            Log.e("index", "已经超过");
        }
        if (startY < centerPoint.y - mVisibleItem * (mCenterTextHeight + textPadding)) {
            startY = centerPoint.y - Math.abs(moveDistance % (mCenterTextHeight + textPadding))
                    - mVisibleItem * (mCenterTextHeight + textPadding);
            Log.e("index", "startY:" + startY);
        }
        if (moveDistance <= 0) {
            moveDistance = 0;
            startY = centerPoint.y;
        }
        curentIndex = (int) (moveDistance / (mCenterTextHeight + textPadding));

        int startIndex = 0;
        //如果已经滚过了足够的item
        startIndex = curentIndex - mVisibleItem;
        if (startIndex < 0) {
            startIndex = 0;
        }
        int endIndex = curentIndex + mVisibleItem;
        if (endIndex > mStrings.size() - 1) {
            endIndex = mStrings.size() - 1;
        }

        int index = startIndex;
        Log.e("index", "开始:" + index);
        while (index <= endIndex) {
            canvas.save();
            canvas.translate(0, traslateY);
            if (startY + traslateY > -mCenterTextHeight / 2 - textPadding + centerPoint.y &&
                    startY + traslateY < mCenterTextHeight / 2 + textPadding + centerPoint.y) {
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
        boolean b = mGestureDetector.onTouchEvent(event);
        if (b) {
            return true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (!isFling) {
                    adjust();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void adjust() {
        Log.e("adjust", "adjust: ");
        float decimal = moveDistance % (mCenterTextHeight + textPadding);
        if (decimal != 0) {
            if (decimal < textPadding / 2 + mCenterTextHeight / 2) {
                adjustValueAnimator.setFloatValues(0, decimal);
            } else {
                adjustValueAnimator.setFloatValues(0, -(textPadding + mCenterTextHeight - decimal));
            }
            adjustValueAnimator.start();
        }
//        moveDistance = ((int) (moveDistance / (mCenterTextHeight + textPadding)))
//                * (mCenterTextHeight + textPadding)
//                + (decimal < textPadding / 2 + mCenterTextHeight / 2 ? 0 : 1) * (mCenterTextHeight + textPadding);
//        startY = centerPoint.y - moveDistance;
//        invalidate();
    }


    class GestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            mValueAnimator.cancel();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG, "onScroll: " + distanceY);
            moveDistance += distanceY;
            startY = centerPoint.y - moveDistance;
            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG, "onFling: " + velocityY);
            float abs = Math.abs(velocityY);
            int during;
            float distance;
            if (abs < 2000) {
                adjust();
                return false;
            } else if (abs < 5000) {
                distance = mCenterTextHeight + textPadding;
                during = 200;
            } else {
                distance = (mCenterTextHeight + textPadding) * mVisibleItem * 2;
                during = 500;
            }
            moveDistance = moveDistance / (mCenterTextHeight + textPadding)
                    * (mCenterTextHeight + textPadding);
            startY = centerPoint.y - moveDistance;
            if (velocityY < 0) {
                distance = -distance;
            }
            lastFlingDistance = 0;
            mValueAnimator.setFloatValues(0, distance);
            mValueAnimator.setDuration(during);
            mValueAnimator.start();
            return true;
        }
    }

}
