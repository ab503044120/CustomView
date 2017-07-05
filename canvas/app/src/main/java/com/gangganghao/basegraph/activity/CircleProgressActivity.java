package com.gangganghao.basegraph.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.gangganghao.basegraph.GradientCircleProgress;
import com.gangganghao.basegraph.R;

/**
 * Created by Administrator on 2017/7/5.
 */

public class CircleProgressActivity extends AppCompatActivity {
    private android.widget.ListView lv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circleprogress);
        this.lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new Adapter());
    }

    public class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GradientCircleProgress circleProgress1 = new GradientCircleProgress(CircleProgressActivity.this);
            circleProgress1.setLayoutParams(new ViewGroup.LayoutParams(500, 500));
            circleProgress1.setMax(3.0f);
//            circleProgress1.startAnimation();
            return circleProgress1;
        }
    }
}
