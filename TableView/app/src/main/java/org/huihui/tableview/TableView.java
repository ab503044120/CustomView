package org.huihui.tableview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */

public class TableView extends View {
    public int mTexsize = 20;
    private List<String> mTab;
    private List<List<String>> mDatas;
    private Paint mPaint;
    private List<Float> mColumeWidths = new ArrayList<>();
    private float columeHeight;
    public RectF mShowRect = new RectF();
    public RectF mScaleRect = new RectF();
    private GestureDetector mGestureDetector;
    private float mTranslateX = 0;
    private float mTranslateY = 0;
    private Scroller scroller;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
        scroller = new Scroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), mListener);

    }

    public void setData(List<String> tab, List<List<String>> datas) {
        mTab = tab;
        mDatas = datas;
        caculate();
        invalidate();
    }

    private void caculate() {
        if (mColumeWidths != null) {
            mColumeWidths.clear();
        }
        mColumeWidths = new ArrayList<>(mTab.size());
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTexsize, getResources().getDisplayMetrics()));
        mScaleRect.left = 0;
        mScaleRect.top = 0;
        for (int i = 0; i < mTab.size(); i++) {
            float width = mPaint.measureText(mTab.get(i));
            for (List<String> data : mDatas) {
                float tmp = mPaint.measureText(data.get(i));
                width = Math.max(width, tmp);
            }
            mScaleRect.right += width;
            mColumeWidths.add(width);
        }
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        columeHeight = fontMetrics.bottom - fontMetrics.top;
        mScaleRect.bottom = columeHeight * mDatas.size();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mShowRect.left = 0;
        mShowRect.right = getMeasuredWidth();
        mShowRect.top = 0;
        mShowRect.bottom = getMeasuredHeight();

    }

    private RectF mTmpRect = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTab == null) {
            return;
        }
        canvas.save();
        canvas.clipRect(mShowRect);
        mTmpRect.left = 0;
        mTmpRect.right = mTranslateX;
        mTmpRect.top = 0;
        mTmpRect.bottom = 0;
        for (int i = 0; i < mTab.size(); i++) {
            mTmpRect.left = mTmpRect.right;
            mTmpRect.top = 0;
            mTmpRect.right = mTmpRect.left + mColumeWidths.get(i);
            mTmpRect.bottom = mTmpRect.top + columeHeight;
            if (inshowRange(mTmpRect)) {
                mPaint.reset();
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.GRAY);
                mPaint.setStrokeWidth(5);
                canvas.drawRect(mTmpRect, mPaint);
                mPaint.reset();
                mPaint.setColor(Color.BLACK);
                mPaint.setTextSize(mTexsize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(mTab.get(i), mTmpRect.centerX(), getFixTextCenter(mTmpRect.centerY(), mPaint), mPaint);
            }
        }
        canvas.restore();
        if (mDatas == null) {
            return;
        }
        canvas.save();
        float top = mTranslateY;
        canvas.clipRect(mShowRect.left, columeHeight, mShowRect.right, mShowRect.bottom);
        for (List<String> data : mDatas) {
            mTmpRect.left = 0;
            mTmpRect.right = mTranslateX;
            mTmpRect.top = top;
            mTmpRect.bottom = 0;
            for (int i = 0; i < data.size(); i++) {
                mTmpRect.left = mTmpRect.right;
                mTmpRect.top = top;
                mTmpRect.right = mTmpRect.left + mColumeWidths.get(i);
                mTmpRect.bottom = mTmpRect.top + columeHeight;
                if (inshowRange(mTmpRect)) {
                    mPaint.reset();
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setColor(Color.GRAY);
                    mPaint.setStrokeWidth(5);
                    canvas.drawRect(mTmpRect, mPaint);
                    mPaint.reset();
                    mPaint.setColor(Color.BLACK);
                    mPaint.setTextSize(mTexsize);
                    mPaint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(data.get(i), mTmpRect.centerX(), getFixTextCenter(mTmpRect.centerY(), mPaint), mPaint);
                }
            }
            top += columeHeight;
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.SimpleOnGestureListener mListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            scroller.abortAnimation();
            return true;
        }

        /**
         *
         * @param e1
         * @param e2
         * @param velocityX 沿着x方向为正
         * @param velocityY 沿着y方向为正
         * @return
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("scroller", "start");
            lastX = 0;
            lastY = 0;
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                scroller.fling(0, 0, (int) velocityX, 0,
                        -50000, 50000, -50000, 50000);
            } else {
                scroller.fling(0, 0, 0, (int) velocityY,
                        -50000, 50000, -50000, 50000);
            }

            return true;
        }

        /**
         *
         * @param e1
         * @param e2
         * @param distanceX 沿着x方向为负
         * @param distanceY 沿着y方向为负
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mTranslateX -= distanceX;
            if (mTranslateX >= 0) {
                mTranslateX = 0;
            }
            if (mScaleRect.right <= mShowRect.right) {
                mTranslateX = 0;
            } else {
                if (mTranslateX <= -(mScaleRect.right - mShowRect.right)) {
                    mTranslateX = -(mScaleRect.right - mShowRect.right);
                }
            }

            mTranslateY -= distanceY;
            if (mTranslateY >= 0) {
                mTranslateY = 0;
            }
            if (mScaleRect.bottom <= mShowRect.bottom) {
                mTranslateY = 0;
            } else {
                if (mTranslateY <= -(mScaleRect.bottom - mShowRect.bottom)) {
                    mTranslateY = -(mScaleRect.bottom - mShowRect.bottom);
                }
            }
            invalidate();
            return true;
        }
    };
    private int lastX;
    private int lastY;

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            mTranslateX += currX - lastX;

            lastX = currX;
            if (mTranslateX >= 0) {
                mTranslateX = 0;
            }
            if (mScaleRect.right <= mShowRect.right) {
                mTranslateX = 0;
            } else {
                if (mTranslateX <= -(mScaleRect.right - mShowRect.right)) {
                    mTranslateX = -(mScaleRect.right - mShowRect.right);
                }
            }
            int currY = scroller.getCurrY();
            mTranslateY += currY - lastX;
            lastY = currY;
            if (mTranslateY >= 0) {
                mTranslateY = 0;
            }
            if (mScaleRect.bottom <= mShowRect.bottom) {
                mTranslateY = 0;
            } else {
                if (mTranslateY <= -(mScaleRect.bottom - mShowRect.bottom)) {
                    mTranslateY = -(mScaleRect.bottom - mShowRect.bottom);
                }
            }
            invalidate();
        }
    }

    public boolean inshowRange(RectF rectf) {
        return RectF.intersects(mShowRect, rectf);
    }

    public float getFixTextCenter(float y, Paint paint) {
        return y - ((paint.descent() + paint.ascent()) / 2);
    }
}
