package edu.umsl.proj.kyu.volleyproject.SubClasses;
/**
 * Created by Kyu on 4/28/2015.
 */
import android.util.Log;

// class to hold each thumbnail resource and return the value
public class Thumbnail {
	private String source;

	public String getSource() {
        Log.i("banana", "Thumbnail.getSource");
		return source;
	}
}
