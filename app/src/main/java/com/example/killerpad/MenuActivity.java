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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

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

        getSupportActionBar().hide();
    }


    // carga las preferencias de las shared preferences
    public String loadPreferences(String key){
        SharedPreferences prefs = getSharedPreferences("savedPrefs", MODE_PRIVATE);
        String restoredText = prefs.getString(key, "");
        return restoredText;
    }

    // guarda las configuraciones de las shared preferences
    public void savePreferences(String key, String value){
        SharedPreferences.Editor sp;
        sp = getSharedPreferences("savedPrefs", MODE_PRIVATE).edit();    // pq es "s"
        sp.putString(key, value);
        sp.commit();
    }



}
