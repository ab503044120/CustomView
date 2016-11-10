package com.gangganghao.basegraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: Administrator
 * Date: 2016-11-10 {HOUR}:08
 */
public class ProgressCircle extends View {
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private Path mCircleOutLinePath;
    private Path mProgressPath;

    private int mStroke = 30;
    private int mRadius = 400;
    private int mBackground = 0xff0082D7;
    private final int mOutlineColor = 0xaaffffff;
    private final int mProgressColor = 0xffffffff;


    private int progress = 20;
    private PathMeasure mProgressPathMeasure;
    private RectF mRectF;

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
        mProgressPathMeasure = new PathMeasure(mCircleOutLinePath, false);
        mProgressPath = new Path();
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
        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, mPaint);
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