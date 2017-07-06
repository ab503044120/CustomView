package com.gangganghao.basegraph.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gangganghao.basegraph.ggh.CubeView;
import com.gangganghao.basegraph.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/6.
 */

public class CubeViewActivity extends AppCompatActivity {
    private CubeView cv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cubeview);
        this.cv = (CubeView) findViewById(R.id.cv);
        ArrayList<CubeView.Item> items = new ArrayList<>();

        items.add(new CubeView.Item("中港", 8000, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("梨江", 500, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("火焰", 1000, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("南", 2000, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("南", 3000, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("南", 5000, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("南", 10000, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("南", 1900, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("南", 12000, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        items.add(new CubeView.Item("南", 200, Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))
                , Color.argb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        cv.setItems(items);
    }

}
