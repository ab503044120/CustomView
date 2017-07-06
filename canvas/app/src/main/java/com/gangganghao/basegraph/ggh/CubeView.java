package com.gangganghao.basegraph.ggh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.gangganghao.basegraph.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class CubeView extends View {
    private Paint mPaint;
    public List<Item> mItems;
    private int mitemStroke;
    private int mMaxWidth;
    private Item mMaxItem;
    List<LinearGradient> mLinearGradients;
    private ValueAnimator mValueAnimator;
    private float mAnimatedValue;
    private Paint mTextPaint;
    private float textOffset;
    private Paint mNameTextPaint;
    private int mNameTextColor;
    private int mNameTextSize;
    private int mNumberTextColor;
    private int mNumberTextSize;
    private int mCubeSpace;
    private int mCubeStroke;
    private float nameTextOffset;

    public CubeView(Context context) {
        this(context, null);
    }

    public CubeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CubeView, defStyle, 0);
        mCubeStroke = a.getDimensionPixelSize(R.styleable.CubeView_cube_stroke
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                        getResources().getDisplayMetrics()));
        mCubeSpace = a.getDimensionPixelSize(R.styleable.CubeView_cube_space
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                        getResources().getDisplayMetrics()));
        mNumberTextSize = a.getDimensionPixelSize(R.styleable.CubeView_cube_number_textsize
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18,
                        getResources().getDisplayMetrics()));
        mNumberTextColor = a.getColor(R.styleable.CubeView_cube_number_textcolor
                , Color.WHITE);
        mNameTextSize = a.getDimensionPixelSize(R.styleable.CubeView_cube_name_textsize
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18,
                        getResources().getDisplayMetrics()));
        mNameTextColor = a.getColor(R.styleable.CubeView_cube_name_textcolor
                , 0xff333333);
        a.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mitemStroke = mCubeStroke;
        mPaint.setStrokeWidth(mitemStroke);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mNumberTextSize);
        mTextPaint.setColor(mNumberTextColor);

        mNameTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNameTextPaint.setTextSize(mNameTextSize);
        mNameTextPaint.setColor(mNameTextColor);

        mValueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    public void setItems(List<Item> items) {
        this.mItems = items;
        mMaxItem = null;
        for (Item item : items) {
            if (mMaxItem == null) {
                mMaxItem = item;
            } else {
                mMaxItem = mMaxItem.number < item.number ? item : mMaxItem;
            }
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mItems == null || mItems.isEmpty()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        super.onMeasure(widthMeasureSpec
                , MeasureSpec.makeMeasureSpec(mItems.size() * 2 * mitemStroke, MeasureSpec.EXACTLY));
        mMaxWidth = getMeasuredWidth() * 2 / 3;

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        textOffset = textHeight / 2 - fontMetrics.bottom;

        fontMetrics = mNameTextPaint.getFontMetrics();
        textHeight = fontMetrics.bottom - fontMetrics.top;
        nameTextOffset = textHeight / 2 - fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mItems == null || mItems.isEmpty()) {
            return;
        }
        int startY = mitemStroke / 2;
        for (Item item : mItems) {
            float width = mMaxWidth * (item.number / mMaxItem.number);

            LinearGradient shader = new LinearGradient(0, startY, width * mAnimatedValue + mitemStroke / 2, startY,
                    item.colorStart, item.colorEnd, Shader.TileMode.REPEAT);
            mPaint.setShader(shader);
            canvas.drawLine(0, startY, width * mAnimatedValue, startY, mPaint);
            if (mAnimatedValue == 1) {
                float textWidth = mTextPaint.measureText(item.number + "");
                if (textWidth < width) {
                    //有足够的空间画文字
                    float marginRight = width - textWidth;
                    canvas.drawText(item.number + "", marginRight, startY + textOffset, mTextPaint);
                }
                float marginLeft = width + mitemStroke;
                canvas.drawText(item.name, marginLeft, startY + nameTextOffset, mNameTextPaint);
            }
            startY += mitemStroke * 1.5;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startAnimation();
    }

    public void startAnimation() {
        if (mItems == null || mItems.isEmpty() || mMaxWidth == 0) {
            return;
        }
        mValueAnimator.start();
    }

    public static class Item {
        public String name;
        public float number;
        public int colorStart;
        public int colorEnd;

        public Item(String name, float number) {
            this.name = name;
            this.number = number;
        }

        public Item(String name, float number, int colorStart, int colorEnd) {
            this.name = name;
            this.number = number;
            this.colorStart = colorStart;
            this.colorEnd = colorEnd;
        }
    }
}
