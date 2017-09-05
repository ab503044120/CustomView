package org.huihui.recyclerview.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MTLeftDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.top = 0;
            outRect.left = 0;
            outRect.right = 0;
            outRect.bottom = 100;
        }
    }
}
