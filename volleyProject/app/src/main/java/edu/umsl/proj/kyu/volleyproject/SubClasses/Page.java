package edu.umsl.proj.kyu.volleyproject.SubClasses;

/**
 * Created by Kyu on 4/28/2015.
 */
import android.util.Log;

// class to hold each title and thumbnail and return the value
public class Page {
	private String title;
	private Thumbnail thumbnail;

    public Thumbnail getThumbnail() {
        Log.i("banana", "Page.Thumbnail");
        return thumbnail;
    }

    public String getTitle() {
        Log.i("banana", "Page.getTitle");
        return title;
    }
}
