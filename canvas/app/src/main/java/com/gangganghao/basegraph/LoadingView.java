package com.gangganghao.basegraph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: Administrator
 * Date: 2016-11-02 {HOUR}:32
 */
public class LoadingView extends View {

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }
}  