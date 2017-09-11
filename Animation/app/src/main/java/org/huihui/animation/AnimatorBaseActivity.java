package org.huihui.animation;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by huihui on 2017/9/10.
 */

public class AnimatorBaseActivity extends AppCompatActivity {
    private Button button1;
    private android.widget.ImageView iv;
    //1.插值器估值器区别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator_base);
        this.iv = (ImageView) findViewById(R.id.iv);
        this.button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                xmlAnimation();
//                propertyValuesHolderAnimator();
                keyFrameAnimator();
            }
        });
    }

    public void xmlAnimation() {
        Animation loadAnimation = AnimationUtils.loadAnimation(AnimatorBaseActivity.this, R.anim.abc_fade_in);
        iv.startAnimation(loadAnimation);
    }

    public void propertyValuesHolderAnimator() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(iv, alpha, scaleX);
        objectAnimator.setInterpolator(new OvershootInterpolator(5));
        objectAnimator.start();
    }

    public void keyFrameAnimator() {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        Keyframe keyframe = Keyframe.ofFloat(0f);
        Keyframe keyframe1 = Keyframe.ofFloat(0.5f, 0.5f);
        keyframe1.setInterpolator(new AccelerateDecelerateInterpolator());
        Keyframe keyframe2 = Keyframe.ofFloat(1f, 1f);
        keyframe1.setInterpolator(new DecelerateInterpolator());
        Keyframe keyframe3 = Keyframe.ofFloat(1f, 2f);
        keyframe1.setInterpolator(new BounceInterpolator());
        scaleX.setKeyframes(keyframe, keyframe1, keyframe2, keyframe3);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(iv, scaleX);
        objectAnimator.setInterpolator(new OvershootInterpolator(5));
        objectAnimator.start();
    }

    public void startAnimation(View v) {


        //1.-------------属性动画基础--------------------
//		iv.setTranslationX(100);
//		iv.setScaleX(scaleX);
//		iv.setAlpha(alpha);
//		iv.setRotation(rotation)
//		iv.setBackgroundColor(color);

//		//只要view里面有setXXX()方法就可以通过反射达到变化的目的
//		ObjectAnimator oa = ObjectAnimator.ofFloat(iv, "translationX", 0f,200f);
////		ObjectAnimator oa = ObjectAnimator.ofFloat(iv, "backgroundColor", Color.RED,Color.BLUE);
//		oa.setDuration(500);
//		oa.start();

        //2.-------------多个动画同时执行----------------------
/*		//方法 1) 设置动画监听，同步操作其他的属性
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "hehe", 0f,100f);
//		ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "translationX", 0f,100f);
		animator.setDuration(300);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// 监听动画回调
//				animation.getAnimatedFraction();//动画执行的百分比 0~1 //API 12+
				float value = (float) animation.getAnimatedValue();//得到0f~100f当中的这个时间点对应的值
				iv.setScaleX(0.5f+value/200);
				iv.setScaleY(0.5f+value/200);
//				iv.setTranslationX(value);
			}
		});
		animator.start();
//		animator.setRepeatCount(2);
//		animator.setRepeatCount(ValueAnimator.INFINITE);
//		animator.setRepeatMode(ValueAnimator.RESTART);
//		animator.setRepeatMode(ValueAnimator.REVERSE.)
*/
//		animator.addListener(new AnimatorListener() {
//
//			@Override
//			public void onAnimationStart(Animator animation) {
//			}
//
//			@Override
//			public void onAnimationRepeat(Animator animation) {
//			}
//
//			@Override
//			public void onAnimationEnd(Animator animation) {
////				animator.setRepeatCount(ValueAnimator.RESTART);
//			}
//
//			@Override
//			public void onAnimationCancel(Animator animation) {
//			}
//		});

//		animator.addListener(new AnimatorListenerAdapter() {
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				// TODO Auto-generated method stub
//				super.onAnimationEnd(animation);
//			}
//		});

        //方法 2）---------------ValueAnimator---如果只需要监听值变化就用ValueAnimator---------------
/*		ValueAnimator animator = ValueAnimator.ofFloat(0f, 200f);
        animator.setDuration(200);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float value = (float) animation.getAnimatedValue();//得到0f~100f当中的这个时间点对应的值
				iv.setScaleX(0.5f+value/200);
				iv.setScaleY(0.5f+value/200);
			}
		});
		animator.start();
*/
        //方法 3)
//		PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("alpha", 1f,0.5f);
//		PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleX", 1f,0.5f);
//		PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("scaleY", 1f,0.5f);
//		ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(iv, holder1,holder2,holder3);
//		animator.setDuration(200);
//		animator.start();

        //方法 4）-------------动画集合-----------------
/*	ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv, "translationX", 0f,100f);
//		animator1.setRepeatCount(3);
		ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv, "alpha", 0f,1f);
//		animator2.setStartDelay(startDelay)//设置延迟执行
		ObjectAnimator animator3 = ObjectAnimator.ofFloat(iv, "scaleX", 0f,2f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(500);
//		animatorSet.play(animator3).with(animator2).after(animator1);//animator1在前面
		animatorSet.play(animator3).with(animator2).before(animator1);//animator1在后面
//		animatorSet.playTogether(animator1,animator2,animator3);
		animatorSet.playSequentially(animator1,animator2,animator3);
		animatorSet.start();*/


        //4.------------------案例：实现自由落体抛物线效果---购物车动画、股指数-----------------
        /**
         * x: 匀速
         * y: 加速度 y=vt=1/2*g*t*t
         * 估值器---控制坐标PointF(x,y)
         */
/*		ValueAnimator valueAnimator = new ValueAnimator();
//		valueAnimator.setInterpolator(value)
		valueAnimator.setDuration(2000);
		valueAnimator.setObjectValues(new PointF(0, 0));
//		valueAnimator.setObjectValues(new PointF(0, 0),new PointF(10, 10));
		final PointF pointF = new PointF();
		//颜色估值器
//		setBackgroundColor((Integer) sArgbEvaluator.evaluate(ratio, mDiscrollveFromBgColor, mDiscrollveToBgColor));
		valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {

			@Override
			public PointF evaluate(float fraction, PointF startValue,
					PointF endValue) {
				// 估值计算方法---可以在执行的过程当中干预改变属性的值---做效果：用自己的算法来控制
				//不断地去计算修改坐标
				//x匀速运动 x=v*t 为了看起来效果好我让t变成fraction*5
				pointF.x = 100f*(fraction*5);
				//加速度 y=vt=1/2*g*t*t
//				pointF.y = 0.5f*9.8f*(fraction*5)*(fraction*5);
				pointF.y = 10f*0.5f*9.8f*(fraction*5)*(fraction*5);
				return pointF;
			}
		});
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				PointF f = (PointF) animation.getAnimatedValue();
				iv.setX(f.x);
				iv.setY(f.y);
			}
		});
		valueAnimator.start();
		*/

        //6.---------插值器（加速器）Interpolater-----------
        ObjectAnimator oa = ObjectAnimator.ofFloat(iv, "translationY", 0f, 1000f);
        oa.setDuration(800);
//		TimeInterpolator
//		oa.setInterpolator(new AccelerateInterpolator(1));
//		oa.setInterpolator(new AccelerateDecelerateInterpolator());
//		oa.setInterpolator(new BounceInterpolator());
//		oa.setInterpolator(new AnticipateInterpolator());
        oa.setInterpolator(new CycleInterpolator(5));

        oa.start();

    }

}
