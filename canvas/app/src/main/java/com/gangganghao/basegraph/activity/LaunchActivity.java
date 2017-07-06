package com.gangganghao.basegraph.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gangganghao.basegraph.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/5.
 */

public class LaunchActivity extends AppCompatActivity {
    private android.widget.ListView lv;
    private ArrayList<Item> mItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        this.lv = (ListView) findViewById(R.id.lv);
        mItems = new ArrayList<>();
        mItems.add(new Item("pie", PieActivity.class));
        mItems.add(new Item("GradientCircleProgress", CircleProgressActivity.class));
        mItems.add(new Item("MainActivity", MainActivity.class));
        mItems.add(new Item("CubeViewActivity", CubeViewActivity.class));
        mItems.add(new Item("RatioActivity", RatioActivity.class));
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mItems.size();
            }

            @Override
            public Object getItem(int position) {
                return mItems.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) LayoutInflater.from(LaunchActivity.this).inflate(R.layout.item, parent, false);
                text.setText(mItems.get(position).text);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LaunchActivity.this, mItems.get(position).mClass));
                    }
                });
                return text;
            }
        });

    }

    public class Item {
        public String text;
        public Class mClass;

        public Item(String text, Class aClass) {
            this.text = text;
            mClass = aClass;
        }
    }
}
