package org.huihui.refreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2017/9/4.
 */

public class GGHHead implements IHead {

    private final Context mContext;
    private final View mView;

    public GGHHead(Context context) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.ggh_refresh_head,null);
    }

    @Override
    public View getView() {
        return mView;
    }
}
