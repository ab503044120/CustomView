package com.gangganghao.basegraph.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gangganghao.basegraph.R;
import com.gangganghao.basegraph.ggh.TimePieView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/6.
 */

public class TimePieActivity extends AppCompatActivity {
    private com.gangganghao.basegraph.ggh.TimePieView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_pie);
        this.tv = (TimePieView) findViewById(R.id.tv);
        ArrayList<Long> datas = new ArrayList<>();
        datas.add(1000L);
        datas.add(2000L);
        datas.add(1800L);
        datas.add(100L);
        datas.add(900L);
        datas.add(500L);
        datas.add(100L);
        datas.add(10L);
        datas.add(0L);
        datas.add(1900L);
        datas.add(900L);
        datas.add(11900L);
        tv.setData(datas);
    }
}
