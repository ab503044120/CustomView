package com.gangganghao.basegraph.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gangganghao.basegraph.PieGraph;
import com.gangganghao.basegraph.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class PieActivity extends AppCompatActivity {
    private com.gangganghao.basegraph.PieGraph pg;
    private List<Integer> colors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        this.pg = (PieGraph) findViewById(R.id.pg);
        colors = new ArrayList<>();
        colors.add(0xffffea35);//火焰
        colors.add(0xff06bf6f);//高桥
        colors.add(0xfff96b1e);//株洲
        colors.add(0xffffc435);//簏谷
        colors.add(0xffcd2525);//大桥
        colors.add(0xff2190d0);//星沙
        colors.add(0xff6ae1ff);
        colors.add(0xff37d993);
        colors.add(0xffffa27a);
        colors.add(0xfff9471e);
        ArrayList<PieGraph.PieDataHolder> pieDataList = new ArrayList<>();
        for (Integer color : colors) {
            pieDataList.add(new PieGraph.PieDataHolder((float) (Math.random() * 100), color, "1111", 2));
        }
        for (Integer color : colors) {
            pieDataList.add(new PieGraph.PieDataHolder((float) (Math.random() * 20), color, "1111", 2));
        }

        pg.setPieData(pieDataList);
        pg.startAnimation();
    }
}
