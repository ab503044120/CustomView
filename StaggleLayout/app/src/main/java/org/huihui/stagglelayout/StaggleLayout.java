package org.huihui.stagglelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huihui on 2017/5/7.
 */

public class StaggleLayout extends ViewGroup {
    private int mCount = 5;
    List<List<View>> viewLists;

    public StaggleLayout(Context context) {
        this(context, null);
    }

    public StaggleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StaggleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setCount(int count) {
        mCount = count;
       requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int perWidth = getMeasuredWidth() / mCount;
        int heightCount = 0;
        int left = 0;
        int right = perWidth;
        for (List<View> viewList : viewLists) {
            for (View view : viewList) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                int top = heightCount + layoutParams.topMargin;
                int bottom = top + view.getMeasuredHeight();
                view.layout(left, top, right, bottom);
                heightCount += view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }
            left += perWidth;
            right += perWidth;
            heightCount = 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewLists = new ArrayList<>();
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int perWidth = sizeWidth / mCount;
        int perWidthMeasureSpec = MeasureSpec.makeMeasureSpec(perWidth, MeasureSpec.EXACTLY);
        View child;
        List<View> viewList = null;
        ArrayList<Integer> heightCounts = new ArrayList<>();
        if (getChildCount() <= 0) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            measureChild(child, perWidthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int heightCount = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            int column = i % mCount;
            int raw = i / mCount;
            if (raw == 0) {
                heightCounts.add(heightCount);
                viewList = new ArrayList<>();
                viewList.add(child);
                viewLists.add(viewList);
            } else {
                viewLists.get(column).add(child);
                Integer integer = heightCounts.remove(column);
                heightCounts.add(column, integer + heightCount);


            }
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(Collections.max(heightCounts ), MeasureSpec.EXACTLY));
    }
}
