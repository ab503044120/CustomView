package org.huihui.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by huihui on 2017/9/10.
 */

public class TaobaoActivity extends AppCompatActivity {
    private android.view.View vtop;
    private android.view.View vbottom;
    private ObjectAnimator mTopScaleX;
    private ObjectAnimator mTopAlpha;
    private AnimatorSet mShowAnimatorSet;
    private ObjectAnimator mBottomTranslationX;
    private ObjectAnimator mTopRotationX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao);
        this.vbottom = (View) findViewById(R.id.v_bottom);
        this.vtop = (View) findViewById(R.id.v_top);
        mShowAnimatorSet = new AnimatorSet();
        mTopScaleX = ObjectAnimator.ofFloat(vtop, "scaleX", 1.0f, 0.8f);
        mTopAlpha = ObjectAnimator.ofFloat(vtop, "Alpha", 1.0f, 0.5f);
        mTopRotationX = ObjectAnimator.ofFloat(vtop, "RotationX", 0f, 15f, 0f);
        mBottomTranslationX = ObjectAnimator.ofFloat(vbottom, "TranslationY", 500f, 0.0f);
//        mShowAnimatorSet.playTogether(mTopAlpha, mTopScaleX,mBottomTranslationX);
        mShowAnimatorSet.play(mTopAlpha).with(mTopScaleX).with(mBottomTranslationX).with(mTopRotationX);
        vtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vbottom.setVisibility(View.VISIBLE);
                mShowAnimatorSet.start();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }
}
