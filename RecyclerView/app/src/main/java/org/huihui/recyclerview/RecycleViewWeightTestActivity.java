package org.huihui.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by huihui on 2018/2/28.
 */

public class RecycleViewWeightTestActivity extends AppCompatActivity {
    private RecyclerView rv;

    private void assignViews() {
        rv = (RecyclerView) findViewById(R.id.rv);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_weight);
        assignViews();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new WeightAdapter());
    }

    public class WeightAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(RecycleViewWeightTestActivity.this);
            textView.setText("11111");
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Log.e("RV", "onCreateViewHolder");
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.e("RV", "onBindViewHolder");
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
}
