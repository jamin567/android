package edu.umsl.proj.kyu.canonhw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    private Activity activity; // to display Game Over dialog in GUI thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Banana", "MainActivity.onCreate");
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("Banana", "MainActivity.onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("Banana", "MainActivity.onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            showAboutDialog(R.string.title);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showAboutDialog2(final int messageId) {
        Log.e("Banana", "CannonView.showAboutDialog");
        // DialogFragment to display quiz stats and start new quiz
        final DialogFragment gameResult = new DialogFragment() {
            // create an AlertDialog and return it
            @Override
            public Dialog onCreateDialog(Bundle bundle) {
                // create dialog displaying String resource for messageId
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(messageId));

                // display number of shots fired and total time elapsed
                builder.setMessage(getResources().getString(R.string.content));
                builder.setPositiveButton(R.string.resume_game, new DialogInterface.OnClickListener() {
                    // called when "Reset Game" Button is pressed
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                } // end anonymous inner class
                ); // end call to setPositiveButton

                return builder.create(); // return the AlertDialog
            } // end method onCreateDialog

        }; // end DialogFragment anonymous inner class


    } // end method showGameOverDialog


    private void showAboutDialog(final int messageId) {
        Log.e("Banana", "CannonView.showAboutDialog");
        // DialogFragment to display quiz stats and start new quiz
        final DialogFragment gameResult = new DialogFragment() {
            // create an AlertDialog and return it
            @Override
            public Dialog onCreateDialog(Bundle bundle) {
                // create dialog displaying String resource for messageId
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(messageId));

                // display contents
                builder.setMessage(getResources().getString(R.string.content));
                builder.setPositiveButton(R.string.start_game, new DialogInterface.OnClickListener() {
                    // called when "Reset Game" Button is pressed
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialogIsDisplayed = false;
                        //newGame(); // set up and start a new game
                    }
                } // end anonymous inner class
                ); // end call to setPositiveButton

                return builder.create(); // return the AlertDialog
            } // end method onCreateDialog
        }; // end DialogFragment anonymous inner class

        // in GUI thread, use FragmentManager to display the DialogFragment
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        //dialogIsDisplayed = true;
                        gameResult.setCancelable(false); // modal dialog
                        gameResult.show(activity.getFragmentManager(), "results");
                    }
                } // end Runnable
        ); // end call to runOnUiThread
    } // end method showGameOverDialog

}
