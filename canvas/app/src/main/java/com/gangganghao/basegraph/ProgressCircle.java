package com.gangganghao.basegraph;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

/**
 * User: Administrator
 * Date: 2016-11-10 {HOUR}:08
 */
public class ProgressCircle extends View {
    private static final int PREPARE = 0;

    private static final int PREPARE2 = 1;

    private static final int DOWNING = 2;

    private int currentStatus = 0;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private Path mCircleOutLinePath;
    private Path mProgressPath;

    private float mStroke = 30;
    private float mRadius = 400;
    private float mArrowStroke = mStroke * 0.7f;
    private float arrowLength = mRadius * 1.3f;
    private float arrowLeftRightLenght = arrowLength * 2.0f / 5.0f;

    private float downRange = arrowLength * 0.2f;

    private int mBackground = 0xff0082D7;
    private final int mOutlineColor = 0xaaffffff;
    private final int mProgressColor = 0xffffffff;


    private int progress = 0;
    private PathMeasure mProgressPathMeasure;
    private RectF mRectF;
    private Path mArrowPath;
    private Path mMoveArrowPath;


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

        mArrowPath = new Path();
        mArrowPath.moveTo(mWidth / 2, mHeight / 2 - arrowLength / 2);
        mArrowPath.lineTo(mWidth / 2, mHeight / 2 + arrowLength / 2);
        mArrowPath.moveTo(mWidth / 2, mHeight / 2 + arrowLength / 2);
        mArrowPath.lineTo((float) (mWidth / 2 + Math.sin(45 * Math.PI / 180) * arrowLeftRightLenght), (float) (mHeight / 2 + arrowLength / 2 - Math.cos(45 * Math.PI / 180) * arrowLeftRightLenght));
        mArrowPath.moveTo(mWidth / 2, mHeight / 2 + arrowLength / 2);
        mArrowPath.lineTo((float) (mWidth / 2 - Math.sin(45 * Math.PI / 180) * arrowLeftRightLenght), (float) (mHeight / 2 + arrowLength / 2 - Math.cos(45 * Math.PI / 180) * arrowLeftRightLenght));

        startPrepare();
//        startDowning();
    }

    private void startPrepare() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f, 0f);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMoveArrowPath = new Path(mArrowPath);
                mMoveArrowPath.offset(0, downRange * (float) animation.getAnimatedValue());
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();
    }

    private void startDowning() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) ((float) animation.getAnimatedValue() * 100.0f);
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();


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
            case PREPARE2:
                drawPrepare2(canvas);
                break;
            case DOWNING:
                drawDowning(canvas);
                break;
        }
    }

    private void drawPrepare2(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mArrowStroke);

        canvas.drawPath(mArrowPath, mPaint);
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
}