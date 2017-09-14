package org.huihui.cordinatorlayoutdemo.bottomsheet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import org.huihui.cordinatorlayoutdemo.R;

/**
 * Created by Administrator on 2017/9/14.
 */

public class BottomSheetActivity extends AppCompatActivity {
    private android.support.v7.widget.RecyclerView rv;
    private android.support.design.widget.CoordinatorLayout cl;
    private BottomSheetBehavior<RecyclerView> bottomSheetBehavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomsheet);
        this.cl = (CoordinatorLayout) findViewById(R.id.cl);
        this.rv = (RecyclerView) findViewById(R.id.rv);
        rv.setAdapter(new TextAdapter());
        rv.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetBehavior = BottomSheetBehavior.from(rv);
        bottomSheetBehavior.setPeekHeight(0);

        cl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
