package edu.umsl.proj.kyu.volleyproject.SubClasses;

/**
 * Created by Kyu on 4/28/2015.
 */
import android.app.Application;
import android.content.Context;
import android.util.Log;

// application level
public class App extends Application {
	private static Context mApplicationContext;

	@Override
	public void onCreate() {
        super.onCreate();
        Log.i("banana", "App.onCreate");
        mApplicationContext = getApplicationContext();
    }

	public static Context getContext() {
        Log.i("banana", "App.getContext");
		return mApplicationContext;
	}
}
