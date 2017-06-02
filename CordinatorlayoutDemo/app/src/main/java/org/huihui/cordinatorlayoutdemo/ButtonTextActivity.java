package org.huihui.cordinatorlayoutdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ButtonTextActivity extends AppCompatActivity {
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_text);
        ButterKnife.bind(this);
        btn.setFocusable(true);
        btn.setFocusableInTouchMode(true);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_MOVE:
                        v.setX(event.getRawX()-v.getWidth()/2 );
                        //注意y坐标是event.getRawY() 是包括状态栏的距离
                        v.setY(event.getRawY()-v.getHeight()/2);
                        break;
                }
                return false;
            }
        });
    }
}
