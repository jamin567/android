package edu.umsl.proj.kyu.volleyproject;

import com.android.volley.Response;

public class SearchRequest extends GsonRequest {
	private final static String REQUEST_URL = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pilimit=50&generator=prefixsearch&gpslimit=50&continue";

	@SuppressWarnings("unchecked")
	public SearchRequest(String query, int imageSize, Response.Listener<Query> listener, Response.ErrorListener errorListener) {
		super(REQUEST_URL + "&pithumbsize=" + imageSize + "&gpssearch=" + query, Query.class, listener, errorListener);
	}
}