package com.fic.android.flagquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends ActionBarActivity {
    // keys for reading data from SharedPreferences
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    public static final String QUESTIONS = "pref_numberOfQuestion";
    public static final String RESET = "pref_reset";
    private boolean phoneDevice = true; // used to force portrait mode
    private boolean preferencesChanged = true; // did preferences change?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Banana", "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

/*        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                Log.e("Banana", "ORIENTATION_PORTRAIT1");
                setContentView(R.layout.activity_main_port);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                Log.e("Banana", "ORIENTATION_LANDSCAPE1");
                setContentView(R.layout.activity_main_land);
                break;
        }*/


        Log.e("Banana", "onCreate2");
        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // register listener for SharedPreferences changes
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // determine screen size
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        // if device is a tablet, set phoneDevice to false
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false; // not a phone-sized device

        // if running on phone-sized device, allow only portrait orientation
        if (phoneDevice)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    } // end method onCreate

    // called after onCreate completes execution
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Banana", "onStart");
        if (preferencesChanged) {
            Log.e("Banana", "preferencesChanged");
            // now that the default preferences have been set,
            // initialize QuizFragment and start the quiz
            QuizFragment quizFragment = (QuizFragment) getFragmentManager().findFragmentById(R.id.quizFragment);
            quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.updateNumberQuestions(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.resetQuiz();
            preferencesChanged = false;
        }
    } // end method onStart

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("Banana", "ORIENTATION_LANDSCAPE2");
            //setContentView(R.layout.activity_main_land);
            invalidateOptionsMenu();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.e("Banana", "ORIENTATION_PORTRAIT2");
            //onCreate(new Bundle());
            //setContentView(R.layout.activity_main_port);
            invalidateOptionsMenu();
        }
    }
    // show menu if app is running on a phone or a portrait-oriented tablet
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get the default Display object representing the screen
        Log.e("Banana", "onCreateOptionsMenu");
        boolean ret = false;
        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                Log.e("Banana", "onCreateOptionsMenu1");
                getMenuInflater().inflate(R.menu.menu_main, menu); // inflate the menu
                ret = true;
                break;
        }
        return ret;
    } // end method onCreateOptionsMenu

    // displays SettingsActivity when running on a phone
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    // listener for changes to the app's SharedPreferences
    private OnSharedPreferenceChangeListener preferenceChangeListener =
            new OnSharedPreferenceChangeListener() {
                // called when the user changes the app's preferences
                @Override

                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                    Log.e("Banana" ,"preferenceChangeListener");
                    preferencesChanged = true; // user changed app settings
                    QuizFragment quizFragment = (QuizFragment) getFragmentManager().findFragmentById(R.id.quizFragment);

                    if (key.equals(CHOICES)) // # of choices to display changed
                    {
                        quizFragment.updateGuessRows(sharedPreferences);
                        quizFragment.resetQuiz();
                        Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
                    } else if (key.equals(REGIONS)) // regions to include changed
                    {
                        Set<String> regions = sharedPreferences.getStringSet(REGIONS, null);

                        if (regions != null && regions.size() > 0) {
                            quizFragment.updateRegions(sharedPreferences);
                            quizFragment.resetQuiz();
                            Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
                        } else // must select one region--set North America as default
                        {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            regions.add(getResources().getString(R.string.default_region));
                            editor.putStringSet(REGIONS, regions);
                            editor.commit();
                            Toast.makeText(MainActivity.this, R.string.default_region_message, Toast.LENGTH_SHORT).show();
                        }
                    } else if (key.equals(QUESTIONS)) {
                        quizFragment.updateNumberQuestions(sharedPreferences);
                        quizFragment.resetQuiz();
                    } else if (key.equals(RESET)) {
                        quizFragment.reset();
                        quizFragment.resetQuiz();
                        Log.e("Banana", "reset called");
                    }


                } // end method onSharedPreferenceChanged
            }; // end anonymous inner class

} // end class MainActivity
