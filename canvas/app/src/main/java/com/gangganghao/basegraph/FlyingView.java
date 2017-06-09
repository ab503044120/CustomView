package com.gangganghao.basegraph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Administrator on 2017/6/9.
 */

public class FlyingView extends View {
    private int mRadio;
    private ValueAnimator mValueAnimator;
    private Path mPath;
    private RectF mRectF;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private Bitmap mBitmap;
    private float[] mPos;
    private float[] mTan;
    private Matrix mMatrix;

    public FlyingView(Context context) {
        this(context, null);
    }

    public FlyingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlyingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mValueAnimator = ValueAnimator.ofFloat(0f, 1.0f);
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mPos = new float[2];
                mTan = new float[2];
                mPathMeasure.getPosTan(animatedValue * mPathMeasure.getLength(), mPos, mTan);
                Log.e("animatedValue", animatedValue + "");
                invalidate();
            }
        });
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        mPathMeasure = new PathMeasure();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 8;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow, opts);
        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRadio = getMeasuredWidth() / 4;
        mPath.reset();
        mPath.addCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadio, Path.Direction.CW);
        mPathMeasure.setPath(mPath, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        if (mPos != null) {
            mMatrix.reset();
//            mMatrix.postRotate(90, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
            mMatrix.postRotate((float) Math.toDegrees(Math.atan2(mTan[1], mTan[0])), mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
            mMatrix.postTranslate(mPos[0] - mBitmap.getWidth() / 2, mPos[1] - mBitmap.getHeight() / 2);
            canvas.drawBitmap(mBitmap, mMatrix, mPaint);
            Log.e("mPos", mPos[0] + "    " + mPos[1]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mValueAnimator.start();
        return super.onTouchEvent(event);
    }
}
