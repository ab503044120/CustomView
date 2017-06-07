package com.gangganghao.basegraph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/6/7.
 */

public class RevealView extends View {
    private final Bitmap bubbleFrameActive;
    private final Bitmap bubbleFrame;
    private Bitmap avftActive;
    private Bitmap avft;
    private Paint mPaint;
    private Bitmap boxStack;
    private Bitmap boxStackActive;
    private int offset;
    private Region mRegion;

    public RevealView(Context context) {
        this(context, null);
    }

    public RevealView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RevealView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        avft = BitmapFactory.decodeResource(getResources(), R.drawable.avft);
        avftActive = BitmapFactory.decodeResource(getResources(), R.drawable.avft_active);

        boxStack = BitmapFactory.decodeResource(getResources(), R.drawable.box_stack);
        boxStackActive = BitmapFactory.decodeResource(getResources(), R.drawable.box_stack_active);

        bubbleFrame = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_frame);
        bubbleFrameActive = BitmapFactory.decodeResource(getResources(), R.drawable.bubble_frame_active);

//        boxStack = BitmapFactory.decodeResource(getResources(), R.drawable.box_stack);
//        boxStackActive = BitmapFactory.decodeResource(getResources(), R.drawable.box_stack_active);
        mPaint = new Paint();
        mRegion = new Region(0, 0, avft.getWidth() * 3, avft.getHeight());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(avft, 0, 0, mPaint);
        canvas.drawBitmap(boxStack, avft.getWidth(), 0, mPaint);
        canvas.drawBitmap(bubbleFrame, avft.getWidth() + boxStack.getWidth(), 0, mPaint);
        canvas.clipRect(offset, 0, offset + avft.getWidth(), avft.getHeight());
        canvas.drawBitmap(avftActive, 0, 0, mPaint);
        canvas.drawBitmap(boxStackActive, avft.getWidth(), 0, mPaint);
        canvas.drawBitmap(bubbleFrameActive, avft.getWidth() + boxStack.getWidth(), 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float x = event.getX();
                if (mRegion.contains((int) x, (int) y)) {
                    offset = (int) x - avft.getWidth() / 2;
                    invalidate();
                }
                break;
        }


        return true;
    }
}
