package edu.umsl.proj.kyu.volleyproject;

import android.app.Application;
import android.content.Context;

public class App extends Application {
	private static Context mApplicationContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplicationContext = getApplicationContext();
	}

	public static Context getContext() {
		return mApplicationContext;
	}
}
