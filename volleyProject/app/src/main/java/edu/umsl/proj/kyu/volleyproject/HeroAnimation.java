package edu.umsl.proj.kyu.volleyproject;

import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.io.Serializable;

public class HeroAnimation implements Serializable {
	private int mLeft;
	private int mTop;
	private int mWidth;
	private int mHeight;
	private float mAlpha;

	public HeroAnimation(View view) {
		final int[] location = new int[2];
		view.getLocationOnScreen(location);

		mLeft = location[0];
		mTop = location[1];
		mWidth = view.getWidth();
		mHeight = view.getHeight();
		mAlpha = ViewHelper.getAlpha(view);
	}

	public void play(View view, int duration) {
		int[] screenLocation = new int[2];
		view.getLocationOnScreen(screenLocation);
		int leftDelta = mLeft - screenLocation[0];
		int topDelta = mTop - screenLocation[1];

		float widthScale = (float)mWidth / view.getWidth();
		float heightScale = (float)mHeight / view.getHeight();

		ViewHelper.setPivotX(view, 0);
		ViewHelper.setPivotY(view, 0);

		AnimatorSet animatorSet = new AnimatorSet();

		animatorSet.playTogether(
				ObjectAnimator.ofFloat(view, "translationX", leftDelta, 0),
				ObjectAnimator.ofFloat(view, "translationY", topDelta, 0),
				ObjectAnimator.ofFloat(view, "scaleX", widthScale, 1),
				ObjectAnimator.ofFloat(view, "scaleY", heightScale, 1),
				ObjectAnimator.ofFloat(view, "alpha", mAlpha, ViewHelper.getAlpha(view))
		);

		animatorSet.setDuration(duration);
		animatorSet.start();
	}
}
