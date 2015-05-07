package edu.umsl.proj.kyu.volleyproject.RequestClasses;

/**
 * Created by Kyu on 5/1/2015.
 */
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import edu.umsl.proj.kyu.volleyproject.SubClasses.Query;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
	private final Gson gson;
	private final Class<T> clazz;
	private final Response.Listener<T> listener;

	public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
        Log.i("banana", "GsonRequest.GsonRequest");
		this.gson = new GsonBuilder().registerTypeAdapter(Query.class, new Query.GsonDeserializer()).create();
		this.clazz = clazz;
		this.listener = listener;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
        Log.i("banana", "GsonRequest.getHeaders");
		return super.getHeaders();
	}

	@Override
	protected void deliverResponse(T response) {
        Log.i("banana", "GsonRequest.deliverResponse");
		listener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Log.i("banana", "GsonRequest.parseNetworkResponse");
        try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}