package com.gangganghao.basegraph.ggh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/7/6.
 */

public class TimePieView extends View {
    public TimePieView(Context context) {
        this(context, null);
    }

    public TimePieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
    }
}
