package org.huihui.refreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2017/9/4.
 */

public class GGHFoot implements IFoot {

    private final Context mContext;
    private final View mView;

    public GGHFoot(Context context) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.ggh_refresh_head, null);
    }

    @Override
    public View getView() {
        return mView;
    }
}
