package edu.umsl.proj.kyu.volleyproject;

/**
 * Created by Kyu on 4/27/2015.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.nineoldandroids.view.ViewHelper;
import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.umsl.proj.kyu.volleyproject.RequestClasses.SearchRequest;
import edu.umsl.proj.kyu.volleyproject.RequestClasses.VolleyRequest;
import edu.umsl.proj.kyu.volleyproject.SubClasses.Page;
import edu.umsl.proj.kyu.volleyproject.SubClasses.Query;

public class MainActivity extends ActionBarActivity {
	@InjectView(R.id.recycler_view) RecyclerView RecyclerView;
	@InjectView(R.id.globe_image)
    ImageView GlobeImageView;
	@InjectView(R.id.word_image)
    ImageView WordImageView;

    public static final String DEFAULT = "";
	private String SearchQuery = "";
	private List<Page> Pages = new ArrayList<>();
	private static final String SAVED_STATE_SEARCH_QUERY = "SEARCH_QUERY";
    private SharedPreferences sharedPreferences; // save previous search
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("banana", "MainActivity.onCreate");
        setContentView(R.layout.activity_search);

		ButterKnife.inject(this);
        ViewHelper.setAlpha(GlobeImageView, 0.1f);
        ViewHelper.setAlpha(WordImageView, 0.1f);
        RecyclerView.setHasFixedSize(true);

        //List adapter for thumnail and title
        RecyclerView.setAdapter(new Adapter(RecyclerView, Pages));

        //Data management
        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SearchQuery = sharedPreferences.getString("search", DEFAULT);
        search();

        //Seek bar
        percentCustomTextView = (TextView) findViewById(R.id.textView);
        setBrightness();

        // set customTipSeekBar's OnSeekBarChangeListener
        SeekBar customTipSeekBar = (SeekBar) findViewById(R.id.seekBar);
        customTipSeekBar.setOnSeekBarChangeListener(customSeekBarListener);

        // register listener to save a new or edited search //TODO
        ImageButton saveButton = (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(SAVED_STATE_SEARCH_QUERY, SearchQuery);
		super.onSaveInstanceState(outState);
        Log.i("banana", "MainActivity.onSaveInstanceState");
    }

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
        Log.i("banana", "MainActivity.onRestoreInstanceState");
		SearchQuery = savedInstanceState.getString(SAVED_STATE_SEARCH_QUERY);
		search();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("banana", "MainActivity.onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.menu_main, menu);

        // modify menu bar into search bar.
		MenuItem searchMenuItem = menu.findItem(R.id.search);
		MenuItemCompat.setActionView(searchMenuItem, R.layout.search_bar);
		EditText searchTextView = (EditText) MenuItemCompat.getActionView(searchMenuItem);
		MenuItemCompat.expandActionView(searchMenuItem);

        //use this text field to check
		if (SearchQuery.length() > 0) searchTextView.setText(SearchQuery);

        // consistantly watching text change
		searchTextView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				SearchQuery = s.toString();
                // search everytime when new value is added in text field
				search();
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		return true;
	}

	private void search() {
        Log.i("banana", "MainActivity.search");
        //uplate the data
        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("search", SearchQuery);
        editor.commit();

        if (SearchQuery.length() == 0) // empty string, nothing happen
			return;

        VolleyRequest.cancelAllRequests();	//remove previous request
        int imageSize = (int)getResources().getDimension(R.dimen.column_width);
        VolleyRequest.getRequestQueue().add(new SearchRequest(SearchQuery, imageSize, new com.android.volley.Response.Listener<Query>() {
			@Override
			public void onResponse(Query response) {
				Pages.clear();
				Pages.addAll(response.getPages());
				((Adapter) RecyclerView.getAdapter()).refresh();
			}
		}, new com.android.volley.Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (error instanceof TimeoutError) {
					toast("Timeout");
				} else {
					toast("0 result");
				}
			}
		}));
	}

    // convinent toast function
	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

    // seek bar function
    private double customPercent = 50;
    private SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            customPercent = progress;
            setBrightness();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private TextView percentCustomTextView;
    private void setBrightness(){
        percentCustomTextView.setText(percentFormat.format(customPercent/100));
        // Brightness setting
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = (float)customPercent/100;
        getWindow().setAttributes(params);
    }

    // saveButtonListener saves a tag-interest pair into SharedPreferences
    public View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showWebPage();
        }
    };

    public void showWebPage (){
        String url = "https://en.wikipedia.org/wiki/" + SearchQuery;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
}
