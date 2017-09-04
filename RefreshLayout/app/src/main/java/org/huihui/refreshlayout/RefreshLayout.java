package org.huihui.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/9/1.
 */

public class RefreshLayout extends FrameLayout {
    private IHead mIHead;
    private IFoot mIFoot;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initHead();
        initFoot();
    }

    private void initFoot() {

    }

    private void initHead() {

    }

    public void setHeader(IHead iHead) {
        mIHead = iHead;
    }

    public void setFooter(IFoot iFoot) {
        mIFoot = iFoot;
    }

    @Override

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }
}
