package org.huihui.tableview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/8/18.
 */

public abstract class BaseTableAdapter {

    public abstract View getView(int row, int column, View convertView, ViewGroup parent);

    public abstract int getWidths(int position);

    public abstract int getHeights(int position);
}
