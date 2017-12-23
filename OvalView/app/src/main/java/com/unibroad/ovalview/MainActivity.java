package com.unibroad.ovalview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private OvalView ovalView;

    private void assignViews() {
        textView = (TextView) findViewById(R.id.textView);
        ovalView = (OvalView) findViewById(R.id.ovalView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(MainActivity.this).load("http://p3.gexing.com/shaitu/20120817/0142/502d30f79a6c4.jpg")
                        .placeholder(R.drawable.pic_default_album)
                        .error(R.drawable.pic_default_album)
                        .dontAnimate()
                        .into(ovalView);
            }
        }, 500);

        ovalView.start();
        ovalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ovalView.stop();
                ovalView.reset();
            }
        });
    }
}
