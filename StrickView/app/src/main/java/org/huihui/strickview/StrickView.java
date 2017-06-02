package org.huihui.strickview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/6/2.
 */

public class StrickView extends View {
    public StrickView(Context context) {
        this(context, null);
    }

    public StrickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }
}
