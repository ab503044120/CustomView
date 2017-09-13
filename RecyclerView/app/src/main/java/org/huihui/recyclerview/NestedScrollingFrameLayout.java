package org.huihui.recyclerview;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/9/11.
 */

public class NestedScrollingFrameLayout extends FrameLayout implements NestedScrollingParent {
    private RecyclerView mChild;

    public NestedScrollingFrameLayout(Context context) {
        this(context, null);
    }

    public NestedScrollingFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        mChild = (RecyclerView) target;
        return true;
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {

    }

    public void onStopNestedScroll(View target) {

    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {

    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (mChild.getChildAdapterPosition(mChild.getChildAt(0)) == 0) {
            if (mChild.getChildAt(0).getTop() == 0 && dy < 0) {
                mChild.setTranslationY(mChild.getTranslationY() - dy);
                consumed[1] = dy;
            }
        }
        if (mChild.getChildAdapterPosition(mChild.getChildAt(mChild.getChildCount() - 1)) == mChild.getAdapter().getItemCount() - 1) {
            if (mChild.getChildAt(mChild.getChildCount() - 1).getBottom() + mChild.getLayoutManager().getBottomDecorationHeight(mChild.getChildAt(mChild.getChildCount() - 1))
                    == mChild.getMeasuredHeight() && dy > 0) {
                mChild.setTranslationY(mChild.getTranslationY() - dy);
                consumed[1] = dy;
            }
        }
    }


    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
