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

import com.gangganghao.basegraph.adapter.BaseWheelViewAdapter;

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
    private ValueAnimator mFlingValueAnimator;
    //当前被选中的index
    private int curentIndex;

    private int mVisibleItem;
    private Scroller mScroller;
    private SimpleOnGestureListener mListener;
    private GestureDetector mGestureDetector;
    private float lastFlingDistance;
    private ValueAnimator adjustValueAnimator;
    private float lastAdjustDistance;
    private WheelViewSelectListener mWheelViewSelectListener;

    private int mSelectItem = 0;
    private boolean isFirstLoad = true;
    private BaseWheelViewAdapter<?> mBaseWheelViewAdapter;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
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

        mFlingValueAnimator = new ValueAnimator();
        mFlingValueAnimator.setInterpolator(new DecelerateInterpolator());
        mFlingValueAnimator.setDuration(200);
        mFlingValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.isRunning()) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    float dy = animatedValue - lastFlingDistance;
                    lastFlingDistance = animatedValue;
                    Log.e("dy", "onAnimationUpdate: " + dy);
                    moveDistance -= dy;
                    startY = centerPoint.y - moveDistance;
                    invalidate();
                }
            }
        });
        mFlingValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int integer = (int) (moveDistance / getItemHeight());
                if (mSelectItem != integer) {
                    if (mWheelViewSelectListener != null) {
                        mWheelViewSelectListener.onSelect(integer);
                    }
                    mSelectItem = integer;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

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
                if (animation.isRunning()) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    float dy = animatedValue - lastAdjustDistance;
                    lastAdjustDistance = animatedValue;
                    moveDistance -= dy;
                    startY = centerPoint.y - moveDistance;
                    invalidate();
                }
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

        mVisibleItem = (int) (h / 2 / getItemHeight()) + 1;
    }

    private float getItemHeight() {
        return mCenterTextHeight + textPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFirstLoad) {
            moveDistance = getItemHeight() * mSelectItem;
            startY = centerPoint.y - moveDistance;
            isFirstLoad = false;
        }
        if (mBaseWheelViewAdapter == null) {
            return;
        }
        super.onDraw(canvas);
        canvas.drawLine(0, centerPoint.y, getMeasuredWidth(), centerPoint.y, mOtherPaint);
        float traslateY = 0;

        if (moveDistance > (mBaseWheelViewAdapter.size() - 1) * (getItemHeight())) {
            startY = centerPoint.y - (mBaseWheelViewAdapter.size() - 1) * (getItemHeight());
            moveDistance = (mBaseWheelViewAdapter.size() - 1) * (getItemHeight());
            Log.e("index", "已经超过");
        }
        if (startY < centerPoint.y - mVisibleItem * (getItemHeight())) {
            startY = centerPoint.y - Math.abs(moveDistance % (getItemHeight()))
                    - mVisibleItem * (getItemHeight());
            Log.e("index", "startY:" + startY);
        }
        if (moveDistance <= 0) {
            moveDistance = 0;
            startY = centerPoint.y;
        }
        curentIndex = (int) (moveDistance / (getItemHeight()));

        int startIndex = 0;
        //如果已经滚过了足够的item
        startIndex = curentIndex - mVisibleItem;
        if (startIndex < 0) {
            startIndex = 0;
        }
        int endIndex = curentIndex + mVisibleItem;
        if (endIndex > mBaseWheelViewAdapter.size() - 1) {
            endIndex = mBaseWheelViewAdapter.size() - 1;
        }

        int index = startIndex;
        Log.e("index", "开始:" + index);
        while (index <= endIndex) {
            canvas.save();
            canvas.translate(0, traslateY);
            if (startY + traslateY > -mCenterTextHeight / 2 - textPadding + centerPoint.y &&
                    startY + traslateY < mCenterTextHeight / 2 + textPadding + centerPoint.y) {
                //中心地带
                canvas.drawText(mBaseWheelViewAdapter.getItem(index), centerPoint.x, startY + mCenterTextOffset, mCenterPaint);
            } else {
                canvas.drawText(mBaseWheelViewAdapter.getItem(index), centerPoint.x, startY + mCenterTextOffset, mOtherPaint);
            }
            canvas.restore();
            traslateY += getItemHeight();
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
                adjust();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void adjust() {
        Log.e(TAG, "adjust: ");
        lastAdjustDistance = 0;
        float decimal = moveDistance % (getItemHeight());
        int integer = (int) (moveDistance / (getItemHeight()));
        if (decimal != 0) {
            if (decimal < textPadding / 2 + mCenterTextHeight / 2) {
                adjustValueAnimator.setFloatValues(0, decimal);
                if (mSelectItem != integer) {
                    if (mWheelViewSelectListener != null) {
                        mWheelViewSelectListener.onSelect(integer);
                    }
                    mSelectItem = integer;
                }
            } else {
                adjustValueAnimator.setFloatValues(0, -(textPadding + mCenterTextHeight - decimal));
                if (mSelectItem != integer + 1) {
                    if (mWheelViewSelectListener != null) {
                        mWheelViewSelectListener.onSelect(integer + 1);
                    }
                    mSelectItem = integer + 1;
                }
            }
            adjustValueAnimator.start();

        }
    }

    public void setSelectItem(int position) {
        if (position > mBaseWheelViewAdapter.size()) {
            return;
        }
        mSelectItem = position;
        adjustValueAnimator.cancel();
        mFlingValueAnimator.cancel();
        if (!isFirstLoad) {
            moveDistance = getItemHeight() * position;
            startY = centerPoint.y - moveDistance;
            invalidate();
        }
    }

    public void setData(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return;
        }
        mStrings = strings;
        if (isFirstLoad) {
            invalidate();
        }
    }

    public void setAdapter(BaseWheelViewAdapter<?> baseWheelViewAdapter) {
        if (baseWheelViewAdapter == null) {
            return;
        }
        mBaseWheelViewAdapter = baseWheelViewAdapter;
        if (isFirstLoad) {
            invalidate();
        }
    }

    class GestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            mFlingValueAnimator.cancel();
            adjustValueAnimator.cancel();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG, "onScroll: " + distanceY);
            moveDistance += distanceY;
            startY = centerPoint.y - moveDistance;
            invalidate();
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG, "onFling: " + velocityY);
            float abs = Math.abs(velocityY);
            int during;
            float distance;
            if (abs < 2000) {
                return false;
            } else if (abs < 5000) {
                distance = getItemHeight();
                during = 200;
            } else {
                distance = (getItemHeight()) * mVisibleItem * 2;
                during = 500;
            }
            float offset = moveDistance % (getItemHeight());
            Log.e(TAG, "onFling: " + velocityY + "moveDistance:" + moveDistance + " distance:" + distance
                    + " offset:" + offset);
            if (velocityY < 0) {
                //往上滑动:需要减去多余的距离
                distance -= offset;
                distance = -distance;
            } else {
                //往下滑:
                distance += offset;
            }

            lastFlingDistance = 0;
            mFlingValueAnimator.setFloatValues(0, distance);
            mFlingValueAnimator.setDuration(during);
            mFlingValueAnimator.start();
            return true;
        }
    }

    public void setWheelViewSelectListener(WheelViewSelectListener wheelViewSelectListener) {
        mWheelViewSelectListener = wheelViewSelectListener;
    }

    public interface WheelViewSelectListener {
        public void onSelect(int position);
    }
}
