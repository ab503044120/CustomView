package org.huihui.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huihui on 2017/9/10.
 */

public class LauncherActivity extends AppCompatActivity {
    List<String> mStrings;
    private android.support.v7.widget.RecyclerView rvcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        this.rvcontent = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_content);
        rvcontent.setLayoutManager(new LinearLayoutManager(this));

        mStrings = new ArrayList<>();
        mStrings.add("底部滑出滑入动画");
        mStrings.add("仿淘宝动画集合");
        mStrings.add("属性动画基础");

        rvcontent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvcontent.setAdapter(new RvAdapter());

    }

    public class RvAdapter extends RecyclerView.Adapter {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RvHolder(new Button(LauncherActivity.this));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            Button itemView = (Button) holder.itemView;
            itemView.setText(mStrings.get(position));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(layoutParams);
            itemView.setGravity(Gravity.CENTER);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(LauncherActivity.this, TaobaoActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(LauncherActivity.this, AnimatorBaseActivity.class));
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mStrings.size();
        }
    }

    public class RvHolder extends RecyclerView.ViewHolder {
        public RvHolder(View itemView) {
            super(itemView);
        }
    }
}
