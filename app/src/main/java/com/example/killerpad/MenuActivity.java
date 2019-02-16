package com.example.killerpad;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;



public class MenuActivity extends AppCompatActivity {

    private static String TAG = "TAG";
    SharedPreferences.Editor sp;    // pq Editor?

    public void savePreferences(String key, String value){
        sp = getSharedPreferences("savedPrefs", MODE_PRIVATE).edit();    // pq es "s"
        sp.putString(key, value);
        sp.commit();
    }

    public String loadPreference(String key){
        SharedPreferences prefs = getSharedPreferences("savedPrefs", MODE_PRIVATE);
        String restoredText = prefs.getString(key, "tonto");
        return restoredText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        Log.d(TAG, "holaaaaa");
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.menu_container);
        if (fragment == null) {
            fragment = new MenuFragment(); fm.beginTransaction()
                    .add(R.id.menu_container, fragment) .commit();
        }




    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        // Hide action bar
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        getSupportActionBar().hide();
    }


}
