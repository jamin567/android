package edu.umsl.proj1.kyu.volleyProject;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;


public class ListActivity extends android.app.ListActivity {

    // name of SharedPreferences XML file that stores the saved searches
    private static final String SEARCHES = "searches";

    private EditText interestEditText; // EditText where user enters a interest
    private EditText tagEditText; // EditText where user tags a interest
    private SharedPreferences savedSearches; // user's favorite searches
    private ArrayList<String> tags; // list of tags for saved searches
    private ArrayAdapter adapter; // binds tags to ListView
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
    private TextView percentCustomTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        percentCustomTextView = (TextView) findViewById(R.id.textView);
        setBrightness();

        // set customTipSeekBar's OnSeekBarChangeListener
        SeekBar customTipSeekBar = (SeekBar) findViewById(R.id.customTipSeekBar);
        customTipSeekBar.setOnSeekBarChangeListener(customSeekBarListener);

        // get references to the EditTexts //TODO
        interestEditText = (EditText) findViewById(R.id.interesEditTextt);
        tagEditText = (EditText) findViewById(R.id.tagEditText);

        // get the SharedPreferences containing the user's saved searches //TODO
        savedSearches = getSharedPreferences(SEARCHES, MODE_PRIVATE);

        // store the saved tags in an ArrayList then sort them //TODO
        tags = new ArrayList<String>(savedSearches.getAll().keySet());
        Collections.sort(tags, String.CASE_INSENSITIVE_ORDER);

        // create ArrayAdapter and use it to bind tags to the ListView //TODO
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, tags);
        setListAdapter(adapter);

        // register listener to save a new or edited search //TODO
        ImageButton saveButton = (ImageButton) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

        // register listener that searches Twitter when user touches a tag //TODO
        getListView().setOnItemClickListener(itemClickListener);

        // set listener that allows user to delete or edit a search //TODO
        getListView().setOnItemLongClickListener(itemLongClickListener);

    } // end method onCreate

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
            addTaggedSearch(
                    interestEditText.getText().toString(),
                    tagEditText.getText().toString()
            );
        }
    };//TODO

    // add new search to the shared preferences, then refresh all Buttons
    private void addTaggedSearch(String interest, String tag) //TODO
    {
        if (tag.length() == 0 || interest.length() == 0) {
            Toast.makeText(ListActivity.this, "Invalid input or tag", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor = savedSearches.edit();
        editor.putString(tag, interest);
        editor.apply();
        if (!tags.contains(tag)) {
            tags.add(tag);
            adapter.notifyDataSetChanged();
        }
    }

    // itemClickListener launches a web browser to display search results
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tagView = (TextView) view;
            String tag = tagView.getText().toString();
            String interest = savedSearches.getString(tag, "");
            String url = "https://www.google.com/search?q=" + interest + "&biw=1920&bih=985&source=lnms&tbm=isch&sa=X&ei=FF0OVdKkLMrFggTy_4LgCg&ved=0CAYQ_AUoAQ&dpr=1";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
    };//TODO

    // itemLongClickListener displays a dialog allowing the user to delete
    // or edit a saved search
    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tagView = (TextView) view;
            final String tag = tagView.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
            builder.setTitle("Please choose your action for " + tag);
            String items[] = new String[]{"Edit", "Share", "Delete"};
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    switch (index) {
                        case 0: //Edit
                            tagEditText.setText(tag);
                            interestEditText.setText(savedSearches.getString(tag, ""));
                            break;
                        case 1: //Share
                            shareSearch(tag);
                            break;
                        case 2: //Delete
                            deleteSearch(tag);
                            break;

                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.create().show();
            return true;
        }
    };//TODO

    // allows user to choose an app for sharing a saved search's URL
    private void shareSearch(String tag) {
        // create the URL representing the search
        String interest = savedSearches.getString(tag, "");
        String urlString = "https://www.google.com/search?q=" + interest + "&biw=1920&bih=985&source=lnms&tbm=isch&sa=X&ei=FF0OVdKkLMrFggTy_4LgCg&ved=0CAYQ_AUoAQ&dpr=1";
        Uri uri = Uri.parse(urlString);
        // create Intent to share urlString //TODO
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share the tag " + tag);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out the input" + interest);
        shareIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(shareIntent, "Message");
        startActivity(chooser);

        // display apps that can share text //TODO
    }

    // deletes a search after the user confirms the delete operation
    private void deleteSearch(final String tag) {
        // create a new AlertDialog
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);//TODO
        confirmBuilder.setMessage("Are you sure?");
        // set the AlertDialog's message //TODO

        // set the AlertDialog's negative Button //TODO
        confirmBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // set the AlertDialog's positive Button //TODO
        confirmBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savedSearches.edit().remove(tag).apply();
                tags.remove(tag);
                adapter.notifyDataSetChanged();
            }
        });
        // display AlertDialog //TODO
        confirmBuilder.create().show();
    } // end method deleteSearch
} // end class MainActivity
