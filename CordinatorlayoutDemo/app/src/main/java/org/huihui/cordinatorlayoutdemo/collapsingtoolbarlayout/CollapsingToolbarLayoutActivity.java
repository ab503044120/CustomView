package org.huihui.cordinatorlayoutdemo.collapsingtoolbarlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.huihui.cordinatorlayoutdemo.R;
import org.huihui.cordinatorlayoutdemo.fragment.Rfragment;

/**
 * Created by Administrator on 2017/8/25.
 */

public class CollapsingToolbarLayoutActivity extends AppCompatActivity {
    private android.support.design.widget.TabLayout tl;
    private android.support.v4.view.ViewPager vp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collpasing);
        this.vp = (ViewPager) findViewById(R.id.vp);
        this.tl = (TabLayout) findViewById(R.id.tl);
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new Rfragment();
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "114";
            }
        });
        tl.setupWithViewPager(vp);
    }
}
