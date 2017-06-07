package com.gangganghao.basegraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private PieView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
