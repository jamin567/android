package edu.umsl.proj.kyu.volleyproject.RequestClasses;

/**
 * Created by Kyu on 5/1/2015.
 */
import android.util.Log;
import com.android.volley.Response;
import edu.umsl.proj.kyu.volleyproject.SubClasses.Query;

public class SearchRequest extends GsonRequest {
	private final static String url = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pilimit=50&generator=prefixsearch&gpslimit=50&continue";

	@SuppressWarnings("unchecked")
	public SearchRequest(String query, int imageSize, Response.Listener<Query> listener, Response.ErrorListener errorListener) {
		super(url + "&pithumbsize=" + imageSize + "&gpssearch=" + query, Query.class, listener, errorListener);
        Log.i("banana", "SearchRequest.SearchRequest");
	}
}