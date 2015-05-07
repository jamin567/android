package edu.umsl.proj.kyu.volleyproject.RequestClasses;

/**
 * Created by Kyu on 5/1/2015.
 */
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import edu.umsl.proj.kyu.volleyproject.SubClasses.App;

public class VolleyRequest {
	private static RequestQueue mRequestQueue;

	public static RequestQueue getRequestQueue() {
        Log.i("banana", "VolleyRequest.RequestQueue");
		if (mRequestQueue == null)
			mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(App.getContext());
		return mRequestQueue;
	}

	public static void cancelAllRequests() { // to cancel previous request
        Log.i("banana", "VolleyRequest.cancelAllRequests");
		getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return true;
			}
		});
	}
}
