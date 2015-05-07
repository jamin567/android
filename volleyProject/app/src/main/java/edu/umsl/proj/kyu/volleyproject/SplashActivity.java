package edu.umsl.proj.kyu.volleyproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import oak.svg.AnimatedSvgView;


public class SplashActivity extends Activity {
	private Handler mHandler = new Handler();

	@InjectView(R.id.animated_globe) AnimatedSvgView mAnimatedGlobe;
	@InjectView(R.id.animated_word_mark) AnimatedSvgView mAnimatedWordMark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ButterKnife.inject(this);

		mAnimatedGlobe.setGlyphStrings(WikipediaLogo.GLOBE_GLYPHS);
		mAnimatedWordMark.setGlyphStrings(WikipediaLogo.LETTER_GLYPHS);

		//trace and residue colors
		int tc = Color.argb(255, 0, 0, 0);
		int rc = Color.argb(50, 0, 0, 0);
		//colors for each individual path in an array of ints
		int[] fillAlphas = new int[] {255, 255, 255, 255, 255, 255};
		int[] fillReds = new int[] {221, 60, 173, 107, 158};
		int[] fillGreens = new int[] {223, 57, 172, 107, 161};
		int[] fillBlues = new int[] {224, 58, 173, 107, 162};
		int[] traceColors = new int[] {tc, tc, 0, 0, 0, 0};
		int[] residueColors = new int[] {rc, rc, 0, 0, 0, 0};

		mAnimatedGlobe.setFillPaints(fillAlphas, fillReds, fillGreens, fillBlues);
		mAnimatedGlobe.setTraceColors(traceColors);
		mAnimatedGlobe.setTraceResidueColors(residueColors);

		//black fillPaint
		mAnimatedWordMark.setFillPaints(new int[] { 255 }, new int[] { 0 }, new int[] { 0 }, new int[] { 0 });
		mAnimatedWordMark.setTraceColors(traceColors);
		mAnimatedWordMark.setTraceResidueColors(residueColors);

		mAnimatedWordMark.setVisibility(View.INVISIBLE);

		mAnimatedGlobe.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
			@Override
			public void onStateChange(int state) {
				if (state == AnimatedSvgView.STATE_FILL_STARTED) {
					mAnimatedWordMark.setVisibility(View.VISIBLE);
					mAnimatedWordMark.start();
				}
			}
		});

		mAnimatedWordMark.setOnStateChangeListener(new AnimatedSvgView.OnStateChangeListener() {
			@Override
			public void onStateChange(int state) {
				if (state == AnimatedSvgView.STATE_FINISHED) {
					Intent intent = new Intent(SplashActivity.this, SearchActivity.class);
					intent.putExtra(WikipediaLogo.GLOBE, new HeroAnimation(mAnimatedGlobe));
					intent.putExtra(WikipediaLogo.WORD_MARK, new HeroAnimation(mAnimatedWordMark));
					startActivity(intent);
					overridePendingTransition(0, 0); //override default transition
				}
			}
		});
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mAnimatedGlobe.start();
			}
		}, 400);
	}
}
