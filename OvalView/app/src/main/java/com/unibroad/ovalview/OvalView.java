package com.unibroad.ovalview;

import android.animation.ValueAnimator;
import android.content.Context;
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


    public OvalView(Context context) {
        this(context, null);
    }

    public OvalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(LAYER_TYPE_HARDWARE, null);

        mValueAnimator = ValueAnimator.ofFloat(0.0f, 359.0f);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = ((float) animation.getAnimatedValue());
                ((OvalDrawable) getDrawable()).updateShaderMatrix();
                invalidate();
            }
        });
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(10000);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(new OvalDrawable(drawable));
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
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
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
        mValueAnimator.start();
    }

    public class OvalDrawable extends Drawable {
        public Bitmap mBitmap;
        private ScaleType mScaleType = ScaleType.CENTER_CROP;
        private RectF mBorderRect = new RectF();
        private RectF mBounds = new RectF();
        private int mBorderWidth;
        private Matrix mShaderMatrix = new Matrix();
        private float mBitmapHeight;
        private float mBitmapWidth;
        private RectF mBitmapRect = new RectF();
        private RectF mDrawableRect = new RectF();

        private Paint mPaint;

        public OvalDrawable(Bitmap bitmap) {
            mBitmap = bitmap;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mBitmapHeight = bitmap.getHeight();
            mBitmapWidth = bitmap.getWidth();
        }

        public OvalDrawable(Drawable drawable) {
            this(drawableToBitmap(drawable));
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
             mPaint.getShader().setLocalMatrix(mShaderMatrix);
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
            mBounds.set(bounds);
            updateShaderMatrix();
            BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            shader.setLocalMatrix(mShaderMatrix);
            mPaint.setShader(shader);
        }

        private void updateShaderMatrix() {
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
                    mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);

                    mShaderMatrix.reset();

                    dx = 0;
                    dy = 0;

                    if (mBitmapWidth * mBorderRect.height() > mBorderRect.width() * mBitmapHeight) {
                        scale = mBorderRect.height() / (float) mBitmapHeight;
                        dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
                    } else {
                        scale = mBorderRect.width() / (float) mBitmapWidth;
                        dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
                    }
                    mShaderMatrix.setRotate(mAnimatedValue, mBitmapWidth / 2, mBitmapHeight / 2);
                    mShaderMatrix.postScale(scale, scale);

                    mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth / 2,
                            (int) (dy + 0.5f) + mBorderWidth / 2);
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
