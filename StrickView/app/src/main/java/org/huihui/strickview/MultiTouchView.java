package org.huihui.strickview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/6/2.
 */

public class MultiTouchView extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private Matrix mSaveMatrix;
    private Paint mPaint;
    private int pointCount;
    private BitmapFactory.Options mOpts;
    private float oldX = -1;
    private float oldY = -1;
    private float mSpacing = -1;
    private PointF mPoint;

    public MultiTouchView(Context context) {
        this(context, null);
    }

    public MultiTouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTouchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mOpts = new BitmapFactory.Options();
        mOpts.inScaled = false;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xyjy6, mOpts);
        mMatrix = new Matrix();
        mSaveMatrix = new Matrix();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPoint = new PointF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMatrix.setTranslate(getMeasuredWidth() / 2 - mBitmap.getWidth() / 2, getMeasuredHeight() / 2 - mBitmap.getHeight() / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                pointCount = 1;
                mSaveMatrix.set(mMatrix);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                pointCount++;
                mSaveMatrix.set(mMatrix);
                mPoint = new PointF();
                caculateCenter(mPoint, event);
                mSpacing = -1;
                break;

            case MotionEvent.ACTION_UP:
                pointCount = 0;
                mSpacing = -1;
                clearMovePoint();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pointCount--;
                clearMovePoint();
                mSaveMatrix.set(mMatrix);
                break;

            case MotionEvent.ACTION_MOVE:
                if (pointCount >= 2) {
                    float spacing = spacing(event);
                    if (mSpacing != -1) {
                        mMatrix.set(mSaveMatrix);
                        float ratio = spacing / mSpacing;
                        mMatrix.postScale(ratio, ratio, mPoint.x, mPoint.y);
                        Log.e("spacing", "距离: " + spacing + "  倍数:" + ratio);
                        invalidate();
                    } else {
                        mSpacing = spacing;
                    }
                } else {

                    if (oldX == -1 && oldY == -1) {
                        oldX = event.getX();
                        oldY = event.getY();
                    } else {
                        mMatrix.set(mSaveMatrix);
                        mMatrix.postTranslate(event.getX() - oldX, event.getY() - oldY);
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    private void clearMovePoint() {
        oldX = -1;
        oldY = -1;
    }

    private void caculateCenter(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
