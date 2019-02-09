package com.example.killerpad;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PadActivity extends AppCompatActivity {
    //holiiiiiii

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        FragmentManager fm = getSupportFragmentManager();

        Fragment board_fragment = fm.findFragmentById(R.id.board_container);
        if (board_fragment == null) {
            board_fragment = new BoardFragment();
            fm.beginTransaction()
                    .add(R.id.board_container, board_fragment) .commit();
        }
        Fragment joystick_fragment = fm.findFragmentById(R.id.joystick_container);
        if (joystick_fragment == null) {
            joystick_fragment = new JoystickFragment();
            fm.beginTransaction()
                    .add(R.id.joystick_container, joystick_fragment) .commit();
        }
        Fragment buttons_fragment = fm.findFragmentById(R.id.buttons_container);
        if (buttons_fragment == null) {
            buttons_fragment = new ButtonsFragment();
            fm.beginTransaction()
                    .add(R.id.buttons_container, buttons_fragment) .commit();
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
