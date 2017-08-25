package org.huihui.tableview;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */

public class Recycler {
    private List<View> mScapViews;

    public Recycler() {
        mScapViews = new ArrayList<>();
    }

    public void RecyclerView(View view) {
        mScapViews.add(view);
    }

    public View obtainView() {
        return obtainView();
    }
}
