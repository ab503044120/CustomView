package org.huihui.recyclerview.nestedscroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.huihui.recyclerview.NestedScrollingFrameLayout;
import org.huihui.recyclerview.R;
import org.huihui.recyclerview.adapter.TextAdapter;

/**
 * Created by Administrator on 2017/9/12.
 */

public class NestScrollActivity extends AppCompatActivity {
    private android.support.v7.widget.RecyclerView rv;
    private org.huihui.recyclerview.NestedScrollingFrameLayout nf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestscroll);
        this.nf = (NestedScrollingFrameLayout) findViewById(R.id.nf);
        this.rv = (RecyclerView) findViewById(R.id.rv);
        rv.setAdapter(new TextAdapter());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        
    }

}
