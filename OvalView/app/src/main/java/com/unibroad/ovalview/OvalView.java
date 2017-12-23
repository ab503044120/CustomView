package com.unibroad.ovalview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * Created by huihui on 2017/11/23.
 */

public class OvalView extends android.support.v7.widget.AppCompatImageView {

    private ValueAnimator mValueAnimator;
    private float mAnimatedValue;
    private float mBorderWidth;
    private int mBorderColorColor;


    public OvalView(Context context) {
        this(context, null);
    }

    public OvalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OvalView, defStyle, 0);
        mBorderWidth = a.getDimension(R.styleable.OvalView_boderWidth, 10);
        mBorderColorColor = a.getColor(R.styleable.OvalView_boderColor, 0x00000000);
        a.recycle();

        setLayerType(LAYER_TYPE_HARDWARE, null);
        mValueAnimator = ValueAnimator.ofFloat(0.0f, 359.0f);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = ((float) animation.getAnimatedValue());

                Drawable drawable = getDrawable();
                if (drawable != null) {
                    ((OvalDrawable) drawable).updateShaderMatrix();
                }
                invalidate();
            }
        });
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(10000);
    }

    /**
     * 开启动画
     */
    public void start() {
        mValueAnimator.setFloatValues(mAnimatedValue + 0.0f, mAnimatedValue + 359.0f);
        mValueAnimator.start();
    }

    /**
     * 停止动画
     */
    public void stop() {
        mValueAnimator.cancel();
    }

    /**
     * 重置旋转角度
     */
    public void reset() {
        mAnimatedValue = 0;
        if (getDrawable() != null && getDrawable() instanceof OvalDrawable) {
            ((OvalDrawable) getDrawable()).updateShaderMatrix();
        }
        invalidate();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(new OvalDrawable(drawable, mBorderWidth, mBorderColorColor));
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.FIT_XY);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        try {
            int width = Math.max(drawable.getIntrinsicWidth(), 2);
            int height = Math.max(drawable.getIntrinsicHeight(), 2);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public class OvalDrawable extends Drawable {
        public Bitmap mBitmap;
        private ScaleType mScaleType = ScaleType.CENTER_CROP;
        private RectF mBorderRect = new RectF();
        private RectF mBounds = new RectF();
        private float mBorderWidth;
        private Matrix mShaderMatrix = new Matrix();
        private float mBitmapHeight;
        private float mBitmapWidth;
        private RectF mBitmapRect = new RectF();
        private RectF mDrawableRect = new RectF();

        private Paint mPaint;
        private Paint mPaint1;
        private int mBorderColorColor;

        public OvalDrawable(Bitmap bitmap, float borderWidth, int borderColorColor) {
            if (bitmap == null) {
                return;
            }
            mBorderWidth = borderWidth;
            mBorderColorColor = borderColorColor;
            mBitmap = bitmap;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint1.setColor(mBorderColorColor);
            mPaint1.setStyle(Paint.Style.FILL);

            mBitmapHeight = bitmap.getHeight();
            mBitmapWidth = bitmap.getWidth();
        }

        public OvalDrawable(Drawable drawable, float borderWidth, int borderColorColor) {
            this(drawableToBitmap(drawable), borderWidth, borderColorColor);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (mBitmap == null) {
                return;
            }
            mPaint.getShader().setLocalMatrix(mShaderMatrix);
            canvas.drawOval(mBounds, mPaint1);
            canvas.drawOval(mDrawableRect, mPaint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            if (mBitmap == null) {
                return;
            }
            mBounds.set(bounds);
            updateShaderMatrix();
            BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            shader.setLocalMatrix(mShaderMatrix);
            mPaint.setShader(shader);
        }

        private void updateShaderMatrix() {
            if (mBitmap == null) {
                return;
            }
            float scale;
            float dx;
            float dy;

            switch (mScaleType) {
                case CENTER:
                    mBorderRect.set(mBounds);
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);

                    mShaderMatrix.reset();
                    mShaderMatrix.setTranslate((int) ((mBorderRect.width() - mBitmapWidth) * 0.5f + 0.5f),
                            (int) ((mBorderRect.height() - mBitmapHeight) * 0.5f + 0.5f));
                    break;

                case CENTER_CROP:
                    mBorderRect.set(mBounds);
                    if (mBorderRect.width() > mBorderRect.height()) {
                        mBorderRect.inset(mBorderWidth / 2 * mBorderRect.width() / mBorderRect.height(), mBorderWidth / 2);
                    } else {
                        mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2 * mBorderRect.height() / mBorderRect.width());
                    }

                    mShaderMatrix.reset();

                    dx = 0;
                    dy = 0;

                    if (mBitmapWidth * mBorderRect.height() > mBorderRect.width() * mBitmapHeight) {
                        scale = mBorderRect.height() / mBitmapHeight;
                        dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
                    } else {
                        scale = mBorderRect.width() / mBitmapWidth;
                        dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
                    }
                    mShaderMatrix.setScale(scale, scale);

                    mShaderMatrix.postTranslate(dx + mBorderWidth / 2,
                            dy + mBorderWidth / 2);
                    mShaderMatrix.postRotate(mAnimatedValue, mBounds.width() / 2, mBounds.height() / 2);
                    if (mBorderRect.width() > mBorderRect.height()) {
                        mShaderMatrix.postScale(mBorderRect.width() / mBorderRect.height(), 1, mBounds.width() / 2, mBounds.height() / 2);
                    } else {
                        mShaderMatrix.postScale(1, mBorderRect.height() / mBorderRect.width(), mBounds.width() / 2, mBounds.height() / 2);
                    }
                    break;

                case CENTER_INSIDE:
                    mShaderMatrix.reset();

                    if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {
                        scale = 1.0f;
                    } else {
                        scale = Math.min(mBounds.width() / (float) mBitmapWidth,
                                mBounds.height() / (float) mBitmapHeight);
                    }

                    dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
                    dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f + 0.5f);

                    mShaderMatrix.setScale(scale, scale);
                    mShaderMatrix.postTranslate(dx, dy);

                    mBorderRect.set(mBitmapRect);
                    mShaderMatrix.mapRect(mBorderRect);
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                    mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                    break;

                default:
                case FIT_CENTER:
                    mBorderRect.set(mBitmapRect);
                    mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.CENTER);
                    mShaderMatrix.mapRect(mBorderRect);
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                    mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                    break;

                case FIT_END:
                    mBorderRect.set(mBitmapRect);
                    mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.END);
                    mShaderMatrix.mapRect(mBorderRect);
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                    mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                    break;

                case FIT_START:
                    mBorderRect.set(mBitmapRect);
                    mShaderMatrix.setRectToRect(mBitmapRect, mBounds, Matrix.ScaleToFit.START);
                    mShaderMatrix.mapRect(mBorderRect);
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                    mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                    break;

                case FIT_XY:
                    mBorderRect.set(mBounds);
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
                    mShaderMatrix.reset();
                    mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect, Matrix.ScaleToFit.FILL);
                    break;
            }
            mDrawableRect.set(mBorderRect);
        }
    }


}
