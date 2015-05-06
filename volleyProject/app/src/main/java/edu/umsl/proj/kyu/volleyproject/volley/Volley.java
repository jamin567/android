package edu.umsl.proj.kyu.volleyproject.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import edu.umsl.proj.kyu.volleyproject.App;

public class Volley {
	private static RequestQueue mRequestQueue;

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue == null)
			mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(App.getContext());

		return mRequestQueue;
	}

	public static void cancelAllRequests() {
		getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return true;
			}
		});
	}
}
