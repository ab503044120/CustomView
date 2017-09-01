package org.huihui.animation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private android.widget.TextView tv;
    private TextView btn;
    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btn = (TextView) findViewById(R.id.btn);
        this.tv = (TextView) findViewById(R.id.tv);
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f);
        mHiddenAction.setDuration(500);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv.getVisibility() == View.VISIBLE) {
                    tv.startAnimation(mHiddenAction);
                    tv.setVisibility(View.GONE);
                } else {
                    tv.startAnimation(mShowAction);
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
