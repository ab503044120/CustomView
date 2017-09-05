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

import org.huihui.recyclerview.itemdecoration.MTLeftDecoration;
import org.huihui.recyclerview.itemdecoration.PinSelectionDecoration1;

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
    private int mSelectedItem = 0;
    private TextView tvcart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt);
        this.tvcart = (TextView) findViewById(R.id.tv_cart);
        this.rvrignt = (RecyclerView) findViewById(R.id.rv_rignt);
        this.rvleft = (RecyclerView) findViewById(R.id.rv_left);
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
        rvleft.setLayoutManager(new LinearLayoutManager(this));
        rvleft.setAdapter(new LAdapter(mGoodsTypes));
        rvleft.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvleft.addItemDecoration(new MTLeftDecoration());
        rvrignt.setLayoutManager(new LinearLayoutManager(this));
        rvrignt.setAdapter(new RAdapter(mTypeBeen));
        rvrignt.addItemDecoration(new PinSelectionDecoration1(this, mGoodsTypes));
        rvrignt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int selectItem = findSelectItem(firstVisibleItemPosition);
                    changeSelectItem(selectItem, true);
                    rvleft.smoothScrollToPosition(selectItem);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int selectItem = findSelectItem(firstVisibleItemPosition);
                changeSelectItem(selectItem, false);
                if (selectItem != -1) {
                    rvleft.smoothScrollToPosition(selectItem);
                }
            }
        });
        tvcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    /**
     * 通过第一个view的位置确定左边列表应该滑到的位置
     *
     * @param position
     * @return
     */
    public int findSelectItem(int position) {
        for (GoodsType goodsType : mGoodsTypes) {
            if (goodsType.mGoodses.size() + 1 > position) {
                return mGoodsTypes.indexOf(goodsType);
            } else {
                position -= goodsType.mGoodses.size() + 1;
            }
        }
        return -1;
    }

    /**
     * 通过应该滑动的位置确定应该滑到的位置
     *
     * @param position
     * @param forceMove 强制滑动
     * @return
     */
    public int changeSelectItem(int position, boolean forceMove) {
        GoodsType goodsType = mGoodsTypes.get(position);
        //如果位置没有改变那就不滑动
        if (!forceMove && goodsType.isSelect) {
            return -1;
        }
        mGoodsTypes.get(mSelectedItem).isSelect = false;
        rvleft.getAdapter().notifyItemChanged(mSelectedItem);
        mSelectedItem = position;
        goodsType.isSelect = true;
        rvleft.getAdapter().notifyItemChanged(mSelectedItem);
        return position;
    }

    public int moveToPosition(int position) {
        int movePosition = 0;
        for (int i = 0; i < position; i++) {
            movePosition += 1 + mGoodsTypes.get(i).mGoodses.size();
        }
        return movePosition;
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            TextView tv = (TextView) holder.itemView.findViewById(R.id.tv_left);
            View ll = holder.itemView.findViewById(R.id.ll_left);
            if (mGoodsTypes.get(position).isSelect) {
                ll.setBackgroundColor(0xfff0f0f0);
            } else {
                ll.setBackgroundColor(0xffffffff);
            }
            tv.setText(mGoodsTypes.get(position).name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsType goodsType = mGoodsTypes.get(mSelectedItem);
                    goodsType.isSelect = false;
                    rvleft.getAdapter().notifyItemChanged(mSelectedItem);
                    mSelectedItem = position;
                    mGoodsTypes.get(mSelectedItem).isSelect = true;
                    rvleft.getAdapter().notifyItemChanged(mSelectedItem);
                    rvrignt.scrollToPosition(moveToPosition(mSelectedItem));
                }
            });

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
