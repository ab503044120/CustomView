package org.huihui.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.huihui.recyclerview.itemdecoration.PinSelectionDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ItemDecorationActivity extends AppCompatActivity {
    private android.support.v7.widget.RecyclerView rv;
    private ArrayList<GoodsType> mGoodsTypes;
    private ArrayList<TypeBean> mTypeBeen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_decoration);
        this.rv = (RecyclerView) findViewById(R.id.rv);
        mGoodsTypes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ArrayList<Goods> goodses = new ArrayList<>();
            for (int j = 0; j < Math.random() * 20; j++) {
                goodses.add(new Goods("类型" + i + "的" + j));
            }
            GoodsType goodType = new GoodsType("类型" + i, goodses);
            mGoodsTypes.add(goodType);
            if (i == 0) {
                goodType.isSelect = true;
            }
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
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RAdapter(mTypeBeen));
//        rv.addItemDecoration(new SpaceDecoration(20));
        rv.addItemDecoration(new PinSelectionDecoration(this, mGoodsTypes));
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
