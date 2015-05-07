package edu.umsl.proj1.kyu.volleyProject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "edu.umsl.proj1.kyu.volleyProject",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }

        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(onClickListner);
        findViewById(R.id.button2).setOnClickListener(onClickListner);
    }

    View.OnClickListener onClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.button:
                    intent = new Intent(MainActivity.this, FacebookActivity.class);
                    break;
                case R.id.button2:
                    intent = new Intent(MainActivity.this, ListActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };

}
