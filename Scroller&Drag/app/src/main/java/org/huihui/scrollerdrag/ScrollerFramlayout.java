package org.huihui.scrollerdrag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ScrollerFramlayout extends FrameLayout {
    private Scroller mScroller;

    public ScrollerFramlayout(Context context) {
        this(context, null);
    }

    public ScrollerFramlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerFramlayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context,new OvershootInterpolator());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void startScroller() {
        mScroller.startScroll(0,0,0,500,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
