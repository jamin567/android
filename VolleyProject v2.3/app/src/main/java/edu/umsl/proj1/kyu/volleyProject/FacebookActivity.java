package edu.umsl.proj1.kyu.volleyProject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FacebookActivity extends FragmentActivity  {
    //Define a private array of fragments that will be handled by your main activity.
    // Also define the array index constants that will be used to manipulate these fragments later on
    private static final int SPLASH = 0;
    private static final int SELECTION = 1;
    private static final int SETTINGS = 2;
    private static final int FRAGMENT_COUNT = SETTINGS  +1;
    private MenuItem settings; // to trigger the UserSettingsFragment display
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        confirmLogin();

        //Hide the fragments initially in the onCreate() method:
        FragmentManager fm = getSupportFragmentManager();
        fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
        fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
        fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

            FragmentTransaction transaction = fm.beginTransaction();
            for(int i = 0; i < fragments.length; i++) {
                transaction.hide(fragments[i]);
            }
            transaction.commit();

            //instance of the UiLifecycleHelper and pass in the listener:
            uiHelper = new UiLifecycleHelper(this, callback);
            uiHelper.onCreate(savedInstanceState);

        }

        public void confirmLogin() {
            DialogFragment newFragment = new confirmLogin();
            newFragment.show(getSupportFragmentManager(),null);
        }

        private class confirmLogin extends DialogFragment {
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Login Success!")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

    }

    //showing a given fragment and hiding all other fragments
    private void showFragment(int fragmentIndex, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (i == fragmentIndex) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    // Define a private method that will be called due to session state changes.
    // The method shows the relevant fragment based on the person's authenticated state.
    // Only handle UI changes when the activity is visible by making use of the isResumed flag.
    // The fragment back stack is first cleared before the showFragment()
    // method is called with the appropriate fragment info:
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        // Only make changes if the activity is visible
        if (isResumed) {
            FragmentManager manager = getSupportFragmentManager();
            // Get the number of entries in the back stack
            int backStackSize = manager.getBackStackEntryCount();
            // Clear the back stack
            for (int i = 0; i < backStackSize; i++) {
                manager.popBackStack();
            }
            if (state.isOpened()) {
                // If the session state is open:
                // Show the authenticated fragment
                showFragment(SELECTION, false);
            } else if (state.isClosed()) {
                // If the session state is closed:
                // Show the login fragment
                showFragment(SPLASH, false);
            }
        }
    }

    // Handle the case where fragments are newly instantiated and the authenticated versus nonauthenticated UI needs to be properly set
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Session session = Session.getActiveSession();

        if (session != null && session.isOpened()) {
            // if the session is already open,
            // try to show the selection fragment
            showFragment(SELECTION, false);
        } else {
            // otherwise present the splash screen
            // and ask the person to login.
            showFragment(SPLASH, false);
        }
    }

    // to prepare the options menu display
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // only add the menu when the selection fragment is showing
        if (fragments[SELECTION].isVisible()) {
            if (menu.size() == 0) {
                settings = menu.add(R.string.settings);
            }
            return true;
        } else {
            menu.clear();
            settings = null;
        }
        return false;
    }

    //UiLifecycleHelper to track the session and trigger a session state change listener.
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.equals(settings)) {
            showFragment(SETTINGS, true);
            return true;
        }
        return false;
    }

    //clear or set the flag when the activity is paused or resumed
    //call the UiLifecycleHelper activity lifecycle methods that are needed to properly keep track of the session
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        isResumed = true;
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
