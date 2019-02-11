package com.example.killerpad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PadActivity extends AppCompatActivity implements JoystickView.JoystickListener {
  
    private Handler handler;

    public Handler getHandler() {
        return this.handler;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitAll().build());

        //poner el valor de las variables segun el valor puesto x los putExtra
        Bundle extras = getIntent().getExtras();

        String user = extras.getString("user");
        String ip = extras.getString("ip");
        int port = extras.getInt("port");

        //crear handler
        this.handler = new Handler(user, ip, port);
        this.handler.setConnection();

        //configurar handler
        ////this.handler.setHandler(this.socket, this.ip, this.port);

        Thread t = new Thread(this.handler);
        t.start();


        //crear los fragments
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

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
      //  Log.d("move","X: "+xPercent+" Y: "+yPercent);
    }

    @Override
    public void directionChanged(String direction) {
        Log.d("move",direction);
        handler.sendMessage(direction);
    }
}
