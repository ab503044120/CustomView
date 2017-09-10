package com.dongnaoedu.taobao.animator;

import android.support.v7.app.ActionBarActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	private View first_view;
	private View second_view;
	private Button bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		first_view = findViewById(R.id.first);
		second_view = findViewById(R.id.second);
		
		bt = (Button)findViewById(R.id.bt);
		
	}
	
	public void startFirstAnim(View v){
		//��ʾfirst_view��1.͸���ȶ�����2.���Ŷ�����3.��ת����
		//͸���ȶ���
		ObjectAnimator firstAlphaAnim = ObjectAnimator.ofFloat(first_view, "alpha", 1.0f, 0.7f);
		firstAlphaAnim.setDuration(300);
		//��ת����1
		ObjectAnimator firstRotationXanim = ObjectAnimator.ofFloat(first_view, "rotationX", 0f,20f);
		firstRotationXanim.setDuration(300);
		//����ת����
		ObjectAnimator firstResumeRotationXanim = ObjectAnimator.ofFloat(first_view, "rotationX", 20f, 0f);
		firstResumeRotationXanim.setDuration(300);
		firstResumeRotationXanim.setStartDelay(300);//�ӳٵ�һ����ת������ʱ�䣬����֮����ִ��
		
		//���Ŷ���
		ObjectAnimator firstScaleXAnim = ObjectAnimator.ofFloat(first_view, "ScaleX", 1.0f, 0.8f);
		firstScaleXAnim.setDuration(300);
		ObjectAnimator firstScaleYAnim = ObjectAnimator.ofFloat(first_view, "ScaleY", 1.0f, 0.8f);
		firstScaleYAnim.setDuration(300);
		
		//������������붥����һ�����룬��Ҫƽ��
		ObjectAnimator firstTranslationYAnim = ObjectAnimator.ofFloat(first_view, "translationY", 0f, -0.1f*first_view.getHeight());
		firstTranslationYAnim.setDuration(300);
		
		//�ڶ���view�͵�һ��view����ͬʱ��ʼִ��
		ObjectAnimator secondTranslationYAnim = ObjectAnimator.ofFloat(second_view, "translationY", second_view.getHeight(), 0f);
		secondTranslationYAnim.setDuration(300);
//		secondTranslationYAnim.setStartDelay(200);
		secondTranslationYAnim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				second_view.setVisibility(View.VISIBLE);
				bt.setClickable(false);
			}
		});
		
		
		AnimatorSet set = new AnimatorSet();
		set.playTogether(firstScaleXAnim,firstScaleYAnim,firstAlphaAnim,firstRotationXanim,firstResumeRotationXanim,firstTranslationYAnim,secondTranslationYAnim);
		set.start();
		
	}

}
