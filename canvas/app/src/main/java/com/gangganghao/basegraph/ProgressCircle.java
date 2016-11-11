package com.gangganghao.basegraph;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * User: Administrator
 * Date: 2016-11-10 {HOUR}:08
 */
public class ProgressCircle extends View {
    private static final int PREPARE = 0;

    private static final int DOWNING = 1;

    private static final int COMPLETE = 2;

    private int currentStatus = 0;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;


    private float mStroke = 30;
    private float mRadius = 400;
    private float mArrowStroke = mStroke * 0.7f;
    private float arrowLength = mRadius * 1.3f;
    private float arrowLeftRightLenght = arrowLength * 2.0f / 5.0f;


    private int mBackground = 0xff0082D7;
    private final int mOutlineColor = 0xaaffffff;
    private final int mProgressColor = 0xffffffff;

    private int progress = 0;


    private float downRange = arrowLength * 0.2f;
    private PathMeasure mProgressPathMeasure;
    private RectF mRectF;
    private Path mMoveArrowPath;
    private Path mArrowPath;
    private Path mArrowLeftPath;
    private Path mArrowRightPath;

    private Path mCircleOutLinePath;
    private Path mProgressPath;

    private ValueAnimator mPrepareAnimator;
    private ValueAnimator mDowningAnimator;

    private Path mErrorLeftPath;
    private Path mErrorRightPath;
    private Path mErrorMovePath;
    private ValueAnimator mErrorLeftAnimator;
    private ValueAnimator mErrorRightAnimator;
    private AnimatorSet mErrorAnimationSet;


    public ProgressCircle(Context context) {
        this(context, null);
    }

    public ProgressCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        initPath();
    }

    private void initPath() {
        mCircleOutLinePath = new Path();
        mCircleOutLinePath.moveTo(mWidth / 2, mHeight / 2 - mRadius);
        mRectF = new RectF(mWidth / 2 - mRadius, mHeight / 2 - mRadius, mWidth / 2 + mRadius, mHeight / 2 + mRadius);
        mCircleOutLinePath.addArc(mRectF, -90, 359);
        mCircleOutLinePath.close();
        mProgressPathMeasure = new PathMeasure(mCircleOutLinePath, false);
        mProgressPath = new Path();

        //箭头
        mArrowPath = new Path();
        mArrowPath.moveTo(mWidth / 2, mHeight / 2 - arrowLength / 2);
        mArrowPath.lineTo(mWidth / 2, mHeight / 2 + arrowLength / 2);

        mArrowRightPath = new Path();
        mArrowRightPath.moveTo(mWidth / 2, mHeight / 2 + arrowLength / 2);
        mArrowRightPath.lineTo((float) (mWidth / 2 + Math.sin(45 * Math.PI / 180) * arrowLeftRightLenght), (float) (mHeight / 2 + arrowLength / 2 - Math.cos(45 * Math.PI / 180) * arrowLeftRightLenght));

        mArrowLeftPath = new Path();
        mArrowLeftPath.moveTo(mWidth / 2, mHeight / 2 + arrowLength / 2);
        mArrowLeftPath.lineTo((float) (mWidth / 2 - Math.sin(45 * Math.PI / 180) * arrowLeftRightLenght), (float) (mHeight / 2 + arrowLength / 2 - Math.cos(45 * Math.PI / 180) * arrowLeftRightLenght));

        mMoveArrowPath = new Path();
        mMoveArrowPath.addPath(mArrowPath);
        mMoveArrowPath.addPath(mArrowRightPath);
        mMoveArrowPath.addPath(mArrowLeftPath);

        //×
        mErrorLeftPath = new Path();
        mErrorLeftPath.moveTo((float) (mWidth / 2f - Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f), (float) (mHeight / 2f - Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f));
        mErrorLeftPath.lineTo((float) (mWidth / 2f + Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f), (float) (mHeight / 2f + Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f));
        mErrorRightPath = new Path();
        mErrorRightPath.moveTo((float) (mWidth / 2f + Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f), (float) (mHeight / 2f - Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f));
        mErrorRightPath.lineTo((float) (mWidth / 2f - Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f), (float) (mHeight / 2f + Math.sin(45 * Math.PI / 180) * arrowLength * 0.8f * 0.5f));
        mErrorMovePath = new Path();
//        mErrorMovePath.addPath(mErrorLeftPath);
//        mErrorMovePath.addPath(mErrorRightPath);

        initAnimationPrepare();
        initAnimationDowning();
        initAnimationError();
    }

    private void initAnimationError() {
        mErrorAnimationSet = new AnimatorSet();
        mErrorAnimationSet.setDuration(400);
        mErrorLeftAnimator = ValueAnimator.ofFloat(0, 1f);
        mErrorLeftAnimator.setDuration(200);
        mErrorLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PathMeasure pathMeasure = new PathMeasure();
                pathMeasure.setPath(mErrorLeftPath, false);
                Path dst = new Path();
                Float animatedValue = (Float) animation.getAnimatedValue();
                pathMeasure.getSegment(0, pathMeasure.getLength() * 0.8f * animatedValue, dst, true);
                mErrorMovePath.reset();
                mErrorMovePath.addPath(dst);
                invalidate();
            }
        });
        mErrorAnimationSet.play(mErrorLeftAnimator);

        mErrorRightAnimator = ValueAnimator.ofFloat(0, 1f);
        mErrorRightAnimator.setDuration(200);
        mErrorRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PathMeasure pathMeasure = new PathMeasure();
                pathMeasure.setPath(mErrorRightPath, false);
                Path dst = new Path();
                Float animatedValue = (Float) animation.getAnimatedValue();
                pathMeasure.getSegment(0, pathMeasure.getLength() * 0.8f * animatedValue, dst, true);
                mErrorMovePath.reset();
                mErrorMovePath.addPath(mErrorLeftPath);
                mErrorMovePath.addPath(dst);
                invalidate();
            }
        });
        mErrorAnimationSet.play(mErrorRightAnimator).after(mErrorLeftAnimator);
    }

    public void initAnimationPrepare() {
        mPrepareAnimator = ValueAnimator.ofFloat(0, 1f, -1f);
        mPrepareAnimator.setDuration(2000);
        mPrepareAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mPrepareAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMoveArrowPath.reset();
                float animatedValue = (float) animation.getAnimatedValue();
                if (animatedValue > 0) {
                    mMoveArrowPath.addPath(mArrowPath);
                    mMoveArrowPath.addPath(mArrowRightPath);
                    mMoveArrowPath.addPath(mArrowLeftPath);
                    mMoveArrowPath.offset(0, downRange * (float) animation.getAnimatedValue());
                } else {
                    float value = Math.abs(animatedValue);
                    PathMeasure pathMeasure = new PathMeasure(mArrowPath, false);
                    Path dstMid = new Path();
                    pathMeasure.getSegment(0, pathMeasure.getLength() - (arrowLength) * value, dstMid, true);

                    pathMeasure.setPath(mArrowLeftPath, false);
                    Path dstLeft = new Path();
                    pathMeasure.getSegment((arrowLength) * value, pathMeasure.getLength(), dstLeft, true);

                    pathMeasure.setPath(mArrowRightPath, false);
                    Path dstRigjt = new Path();
                    pathMeasure.getSegment((arrowLength) * value, pathMeasure.getLength(), dstRigjt, true);

                    mMoveArrowPath.addPath(dstMid);
                    mMoveArrowPath.addPath(dstLeft);
                    mMoveArrowPath.addPath(dstRigjt);
                }
                invalidate();
            }
        });
        mPrepareAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentStatus = DOWNING;
                mDowningAnimator.start();
            }
        });
    }

    private void initAnimationDowning() {
        mDowningAnimator = ValueAnimator.ofFloat(0, 1f);
        mDowningAnimator.setDuration(5000);
        mDowningAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) ((float) animation.getAnimatedValue() * 100.0f);
                invalidate();
            }
        });
        mDowningAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentStatus = COMPLETE;
                mErrorAnimationSet.start();
            }

        });


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        mPaint = new Paint();

        setBackgroundColor(mBackground);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutline(canvas);
        switch (currentStatus) {
            case PREPARE:
                drawPrepare(canvas);
                break;
            case DOWNING:
                drawDowning(canvas);
                break;
            case COMPLETE:
                drawProgress(canvas);
                drawComplete(canvas);
                break;
        }
    }

    private void drawComplete(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mStroke);
        Log.e("draw",new PathMeasure(mErrorMovePath,false).getLength() + "....");
        canvas.drawPath(mErrorMovePath, mPaint);

    }

    private void drawPrepare(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mArrowStroke);

        canvas.drawPath(mMoveArrowPath, mPaint);


    }

    private void drawDowning(Canvas canvas) {
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(200);
        mPaint.setTextAlign(Paint.Align.CENTER);
        String text = progress + "%";
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
//        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, mPaint);
        canvas.drawText(text, mWidth / 2, mHeight / 2 - (bounds.top + bounds.bottom) / 2, mPaint);
        mProgressPath.reset();
    }

    private void drawProgress(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mStroke);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mProgressPath.reset();
        mProgressPathMeasure.getSegment(0, progress / 100.0f * mProgressPathMeasure.getLength(), mProgressPath, true);
        canvas.drawPath(mProgressPath, mPaint);
    }

    private void drawOutline(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mOutlineColor);
        mPaint.setStrokeWidth(mStroke);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mCircleOutLinePath, mPaint);
    }

    public void start() {
        currentStatus = PREPARE;
        mPrepareAnimator.start();
    }
//    new Interpolator() {
//        @Override
//        public float getInterpolation(float input) {
//            float result;
//            if (input < 0.66666666f) {
//                result = (float) (Math.sin(Math.PI * input*(0.5f/0.66666666f)))/ 2;
//                Log.e("value","Value : "+ result);
//                return result;
//            } else {
//                result = (float) (2 - Math.sin(Math.PI * (0.5f+(input-0.66666666f)*(0.5/(1-0.66666666f))))) / 2;
//                Log.e("value","Value : "+ result);
//
//                return result;
//            }
//        }
//    }
}