package com.gangganghao.basegraph.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gangganghao.basegraph.PieGraph;
import com.gangganghao.basegraph.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/5.
 */

public class PieActivity extends AppCompatActivity {
    private com.gangganghao.basegraph.PieGraph pg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);
        this.pg = (PieGraph) findViewById(R.id.pg);
        ArrayList<PieGraph.PieDataHolder> pieDataList = new ArrayList<>();
        pieDataList.add(new PieGraph.PieDataHolder(100, Color.RED, "1111", 2));
        pieDataList.add(new PieGraph.PieDataHolder(200, Color.GRAY, "1111", 2));
        pieDataList.add(new PieGraph.PieDataHolder(300, Color.GREEN, "1111", 2));
        pieDataList.add(new PieGraph.PieDataHolder(400, Color.BLUE, "1111", 2));
        pieDataList.add(new PieGraph.PieDataHolder(500, Color.YELLOW, "1111", 2));
        pg.setPieData(pieDataList);
        pg.startAnimation();
    }
}
