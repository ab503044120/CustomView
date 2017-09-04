package org.huihui.refreshlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.RecyclerView rv;
    private int[] foodPics = {R.drawable.food1, R.drawable.food2, R.drawable.food3, R.drawable.food4,
            R.drawable.food5};
    private RefreshLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fl = (RefreshLayout) findViewById(R.id.fl);
        this.rv = (RecyclerView) findViewById(R.id.rv);
        fl.setHeader(new GGHHead(this));
        fl.setFooter(new GGHFoot(this));
        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
                return new RecyclerView.ViewHolder(inflate) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ImageView) holder.itemView.findViewById(R.id.iv_food)).setImageResource(foodPics[position % 5]);
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
