package org.huihui.stagglelayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private StaggleLayout sl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.sl = (StaggleLayout) findViewById(R.id.sl);
        for (int i = 0; i < 100; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
            int v = (int) (Math.random() * 255);
            textView.setBackgroundColor(Color.argb(v, v, v, v));
            textView.setTextSize((float) (Math.random() * 20f + 10f));
            textView.setGravity(Gravity.CENTER);
            textView.setText(i + "");
            final int finali = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sl.setCount(finali / 10 + 1);
                }
            });
            sl.addView(textView);
        }

    }
}
