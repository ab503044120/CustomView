package org.huihui.recyclerview.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.huihui.recyclerview.R;

/**
 * Created by Administrator on 2017/8/28.
 */

public class PinSelectionDecoration extends RecyclerView.ItemDecoration {

    public PinSelectionDecoration(Context context) {
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mt_right_title, parent, false);
        view.measure(View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.AT_MOST)
                , View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.AT_MOST));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(c);
    }
}
