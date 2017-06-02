package org.huihui.strickview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/6/2.
 */

public class MultiTouchView extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private Paint mPaint;
    private BitmapFactory.Options mOpts;
    private float oldX;
    private float oldY;

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
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
