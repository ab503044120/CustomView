package org.huihui.recyclerview.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.huihui.recyclerview.ItemDecorationActivity;
import org.huihui.recyclerview.R;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */

public class PinSelectionDecoration extends RecyclerView.ItemDecoration {

    private final List<ItemDecorationActivity.GoodsType> mGoodsTypes;

    public PinSelectionDecoration(Context context, List<ItemDecorationActivity.GoodsType> goodsTypes) {
        mGoodsTypes = goodsTypes;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mt_right_title, parent, false);
        View child1 = parent.getChildAt(0);
        int childAdapterPosition1 = parent.getChildAdapterPosition(child1);
        int itemPosition = findItemPosition(childAdapterPosition1);
        ItemDecorationActivity.GoodsType goodsType = mGoodsTypes.get(itemPosition);
        ((TextView) view.findViewById(R.id.tv_right)).setText(goodsType.name);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        view.measure(View.MeasureSpec.makeMeasureSpec(layoutParams.width, View.MeasureSpec.EXACTLY)
                , View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY));
        int bottom = child1.getBottom();
        if (isGroupLastView(childAdapterPosition1) && (bottom - view.getMeasuredHeight()) < 0) {
            c.translate(0, bottom - view.getMeasuredHeight());
        }
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(c);
        c.restore();

    }

    public int findItemPosition(int position) {
        for (ItemDecorationActivity.GoodsType goodsType : mGoodsTypes) {
            if (goodsType.mGoodses.size() + 1 > position) {
                return mGoodsTypes.indexOf(goodsType);
            }
            position -= goodsType.mGoodses.size() + 1;
        }
        return 0;
    }

    public boolean isGroupLastView(int position) {
        if (position == 0) {
            return false;
        }
        for (ItemDecorationActivity.GoodsType goodsType : mGoodsTypes) {
            if (goodsType.mGoodses.size() == position) {
                return true;
            }
            position -= goodsType.mGoodses.size() + 1;
            if (position <= 0) {
                return false;
            }
        }
        return false;
    }
}
