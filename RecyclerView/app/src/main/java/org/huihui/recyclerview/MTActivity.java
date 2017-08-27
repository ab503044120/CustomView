package org.huihui.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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

public class MTActivity extends AppCompatActivity {
    private android.support.v7.widget.RecyclerView rvleft;
    private android.support.v7.widget.RecyclerView rvrignt;
    private List<GoodsType> mGoodsTypes;
    private List<TypeBean> mTypeBeen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt);
        this.rvrignt = (RecyclerView) findViewById(R.id.rv_rignt);
        this.rvleft = (RecyclerView) findViewById(R.id.rv_left);
        mGoodsTypes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ArrayList<Goods> goodses = new ArrayList<>();
            for (int j = 0; j < Math.random() * 20; j++) {
                goodses.add(new Goods("类型" + i + "的" + j));
            }
            mGoodsTypes.add(new GoodsType("类型" + i, goodses));
        }
        mTypeBeen = new ArrayList<>();
        for (GoodsType goodsType : mGoodsTypes) {
            TypeBean typeBean = new TypeBean();
            typeBean.type = 1;
            typeBean.name = goodsType.name;
            mTypeBeen.add(typeBean);
            for (Goods goodse : goodsType.mGoodses) {
                typeBean = new TypeBean();
                typeBean.type = 0;
                typeBean.mGoods = goodse;
                mTypeBeen.add(typeBean);
            }
        }
        rvleft.setLayoutManager(new LinearLayoutManager(this));
        rvleft.setAdapter(new LAdapter(mGoodsTypes));
        rvleft.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvrignt.setLayoutManager(new LinearLayoutManager(this));
        rvrignt.setAdapter(new RAdapter(mTypeBeen));
        rvrignt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    public class LAdapter extends RecyclerView.Adapter {
        List<GoodsType> mGoodsTypes;

        public LAdapter(List<GoodsType> goodsTypes) {
            mGoodsTypes = goodsTypes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mt_left, parent, false);
            return new RecyclerView.ViewHolder(inflate) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView tv = (TextView) holder.itemView.findViewById(R.id.tv_left);
            tv.setText(mGoodsTypes.get(position).name);

        }

        @Override
        public int getItemCount() {
            return mGoodsTypes.size();
        }
    }

    public class RAdapter extends RecyclerView.Adapter {
        List<TypeBean> mTypeBeen;

        public RAdapter(List<TypeBean> typeBeen) {
            mTypeBeen = typeBeen;
        }

        @Override
        public int getItemViewType(int position) {
            return mTypeBeen.get(position).type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mt_right, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mt_right_title, parent, false);
            }
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView tv = (TextView) holder.itemView.findViewById(R.id.tv_right);
            if (getItemViewType(position) == 0) {
                tv.setText(mTypeBeen.get(position).mGoods.name);
            } else {
                tv.setText(mTypeBeen.get(position).name);
            }
        }

        @Override
        public int getItemCount() {
            return mTypeBeen.size();
        }
    }

    public class TypeBean {
        public int type;
        public String name;
        public Goods mGoods;

    }

    public class GoodsType {
        public boolean isSelect;
        public String name;
        public List<Goods> mGoodses;

        public GoodsType(String name, List<Goods> goodses) {
            this.name = name;
            mGoodses = goodses;
        }
    }

    public class Goods {
        public String name;

        public Goods(String name) {
            this.name = name;
        }
    }
}
