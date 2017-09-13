package org.huihui.scrollerdrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ScrollerActivity extends AppCompatActivity {
    private android.widget.TextView tv;
    private ScrollerFramlayout fl;
    private Scroller mScroller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        this.fl = (ScrollerFramlayout) findViewById(R.id.fl);
        this.tv = (TextView) findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl.startScroller();
            }
        });
//        fl.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction() & MotionEvent.ACTION_MASK) {
//
//                }
//                return false;
//            }
//        });
    }
}
