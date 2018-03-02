package org.huihui.tableview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TableView tv;

    private void assignViews() {
        tv = (TableView) findViewById(R.id.tv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        ArrayList<String> tab = new ArrayList<>();
        tab.add("111");
        tab.add("1111");
        tab.add("11111");
        tab.add("111111");
        tab.add("1111111");
        tab.add("11111111");
        tab.add("111111111");
        tab.add("1111111111");
        tab.add("11111111111");
        tab.add("111111111111");
        tab.add("1111111111111");
        tab.add("11111111111111");
        ArrayList<List<String>> datas = new ArrayList<>();
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        datas.add(tab);
        tv.setData(tab, datas);
    }
}
