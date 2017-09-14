package org.huihui.cordinatorlayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.huihui.cordinatorlayoutdemo.bottomsheet.BottomSheetActivity;
import org.huihui.cordinatorlayoutdemo.collapsingtoolbarlayout.CollapsingToolbarLayoutActivity;
import org.huihui.cordinatorlayoutdemo.viewpager_recyclerview.ViewPagerHeaderRecyclerViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    List<String> mStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        mStrings = new ArrayList<>();
        mStrings.add("按钮文字");
        mStrings.add("viewpager_recyclerview_header");
        mStrings.add("CollapsingToolbarLayoutActivity");
        mStrings.add("BottomSheetActivity");

        rvContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvContent.setAdapter(new RvAdapter());

    }

    public class RvAdapter extends RecyclerView.Adapter {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RvHolder(new Button(MainActivity.this));
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
                            startActivity(new Intent(MainActivity.this, ButtonTextActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(MainActivity.this, ViewPagerHeaderRecyclerViewActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(MainActivity.this, CollapsingToolbarLayoutActivity.class));
                            break;
                        case 3:
                            startActivity(new Intent(MainActivity.this, BottomSheetActivity.class));
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
