package org.huihui.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huihui on 2017/8/26.
 */

public class GridRecyclerViewActivity extends AppCompatActivity {
    private android.support.v7.widget.RecyclerView rvcontent;
    private ArrayList<TypeBean> mTypeBeen;
    private android.widget.TextView toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grv);
        this.toggle = (TextView) findViewById(R.id.toggle);
        this.rvcontent = (RecyclerView) findViewById(R.id.rv_content);
        mTypeBeen = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i == 0) {
                mTypeBeen.add(new TypeBean(1, "标题"));
                continue;
            }
            if (Math.random() * 10 > 1) {
                mTypeBeen.add(new TypeBean(0, "商品"));
            } else {
                mTypeBeen.add(new TypeBean(1, "标题"));
            }
        }
        rvcontent.setAdapter(new RAdapter(mTypeBeen));
        //grid布局
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return mTypeBeen.get(position).type == 0 ? 1 : 3;
//            }
//        });
//        rvcontent.setLayoutManager(gridLayoutManager);
        //列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvcontent.setLayoutManager(linearLayoutManager);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rvcontent.getLayoutManager().getClass() ==  LinearLayoutManager.class) {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(GridRecyclerViewActivity.this, 3);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return mTypeBeen.get(position).type == 0 ? 1 : 3;
                        }
                    });
                    rvcontent.setLayoutManager(gridLayoutManager);
                } else {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GridRecyclerViewActivity.this);
                    rvcontent.setLayoutManager(linearLayoutManager);
                }
            }
        });
    }

    public class RAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<TypeBean> mTypeBeen;

        public RAdapter(List<TypeBean> typeBeen) {
            mTypeBeen = typeBeen;

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            if (mTypeBeen.get(viewType).type == 0) {
                view = layoutInflater.inflate(R.layout.item_good, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.item_titile, parent, false);
            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mTypeBeen.size();
        }

    }

    public class TypeBean {
        public int type;
        public String bean;

        public TypeBean(int type, String bean) {
            this.type = type;
            this.bean = bean;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
