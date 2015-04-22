package edu.umsl.proj.kyu.canonhw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;


public class MainActivity extends ActionBarActivity {
    private CannonThread cannonThread; // controls the game loop
    private SurfaceHolder holder; // for manipulating canvas
    private CannonView cannonView; // for manipulating canvas
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Log.e("Banana", "MainActivity.onOptionsItemSelected");
            showAboutDialog(R.string.title);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showAboutDialog(final int messageId) {
        Log.e("Banana", "MainActivity.showAboutDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(messageId));

        // display number of shots fired and total time elapsed
        builder.setMessage(getResources().getString(R.string.content));
        builder.setPositiveButton(R.string.resume_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setCancelable(true);
        builder.show();
    } // end method showGameOverDialog


}
