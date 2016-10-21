package com.gangganghao.basegraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Administraor
 * Date: 2016-10-21 {HOUR}:40
 */
public class PieView extends View {
    private Paint mPaint;
    private List<PieData> data;
    private int persent;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        data = new ArrayList<>();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setData(List<PieData> data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        persent = 0;
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2 - 50, mPaint);

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2 - 50, mPaint);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        RectF rectF = new RectF(getMeasuredWidth() / 2 - getMeasuredWidth() / 2 + 50+2, getHeight() / 2 - getMeasuredWidth() / 2 + 50+2, getMeasuredWidth() / 2 + getMeasuredWidth() / 2 - 50-2, getHeight() / 2 + getMeasuredWidth() / 2 - 50-2);
        for (PieData pieData : data) {
            mPaint.setColor(pieData.color);
            canvas.drawArc(rectF, persent, persent + pieData.percent, true, mPaint);
            persent += pieData.percent;
        }
    }
}