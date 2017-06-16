package com.gangganghao.basegraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/6/16.
 */

public class WheelView extends View {

    private int textSize = 30;
    private int centerTextColor = 0xff333333;
    private int otherTextColor = 0xff999999;
    private Paint mCenterPaint;
    private Paint mOtherPaint;
    private float textPadding;

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

        mOtherPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOtherPaint.setTextSize(textSize * 0.7f);
        mOtherPaint.setColor(otherTextColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
