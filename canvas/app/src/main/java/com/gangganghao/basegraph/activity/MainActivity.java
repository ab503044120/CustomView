package com.gangganghao.basegraph.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.gangganghao.basegraph.GradientCircleProgress;
import com.gangganghao.basegraph.R;

public class MainActivity extends AppCompatActivity {
    private GradientCircleProgress wv;
    private android.widget.RelativeLayout activitymain;

//    private PieView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activitymain = (RelativeLayout) findViewById(R.id.activity_main);
        this.wv = (GradientCircleProgress) findViewById(R.id.wv);
        wv.setMax(3.0f);
//        wv.startAnimation();
//        this.activitymain = (RelativeLayout) findViewById(R.id.activity_main);
//        this.wv = (WheelView) findViewById(R.id.wv);
//        wv.setWheelViewSelectListener(new WheelView.WheelViewSelectListener() {
//            @Override
//            public void onSelect(int position) {
//                Toast.makeText(getApplication(), position + "", Toast.LENGTH_SHORT).show();
//            }
//        });
//        final List<String> mStrings = new ArrayList<>();
//        mStrings.add("湖南");
//        mStrings.add("北京");
//        mStrings.add("乌鲁木齐");
//        mStrings.add("新疆维吾尔自治区");
//        mStrings.add("西藏");
//        mStrings.add("阿里巴巴");
//        mStrings.add("腾讯");
//        mStrings.add("宁夏回族自治区");
//        mStrings.add("这是一个超长的东西啊啊啊啊啊啊啊");
//
//        mStrings.add("湖南");
//        mStrings.add("北京");
//        mStrings.add("乌鲁木齐");
//        mStrings.add("新疆维吾尔自治区");
//        mStrings.add("西藏");
//        mStrings.add("阿里巴巴");
//        mStrings.add("腾讯");
//        mStrings.add("宁夏回族自治区");
//        mStrings.add("这是一个超长的东西啊啊啊啊啊啊啊");
//
//        mStrings.add("湖南");
//        mStrings.add("北京");
//        mStrings.add("乌鲁木齐");
//        mStrings.add("新疆维吾尔自治区");
//        mStrings.add("西藏");
//        mStrings.add("阿里巴巴");
//        mStrings.add("腾讯");
//        mStrings.add("宁夏回族自治区");
//        mStrings.add("这是一个超长的东西啊啊啊啊啊啊啊");
//        wv.setAdapter(new BaseWheelViewAdapter<String>(mStrings) {
//
//            @Override
//            public int size() {
//                return mDatas.size();
//            }
//
//            @Override
//            public String getItem(int position) {
//                return mDatas.get(position);
//            }
//        });
//
//        wv.setSelectItem(10);

//        final ProgressCircle pv = (ProgressCircle) findViewById(R.id.pv);
//        pv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pv.start();
//            }
//        });
//        ArrayList<PieData> data = new ArrayList<>();
        //注意颜色是32未如果写成24位就是透明...这里掉过坑
//        data.add(new PieData(10, 0xffff0000));
//        data.add(new PieData(20, 0xff00ff00));
//        data.add(new PieData(30, 0xff0000ff));
//        pv.setData(data);
    }
}
