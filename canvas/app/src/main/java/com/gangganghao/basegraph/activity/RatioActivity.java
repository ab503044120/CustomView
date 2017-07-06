package com.gangganghao.basegraph.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gangganghao.basegraph.R;
import com.gangganghao.basegraph.ggh.RatioView;

/**
 * Created by Administrator on 2017/7/6.
 */

public class RatioActivity extends AppCompatActivity {

    private com.gangganghao.basegraph.ggh.RatioView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratio);
        this.rv = (RatioView) findViewById(R.id.rv);
        rv.setRadio(0.8f, "10000元", "20000元");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv.startAnimation();
            }
        }, 3000);
    }
}
