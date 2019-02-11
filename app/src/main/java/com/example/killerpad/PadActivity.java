package com.example.killerpad;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class PadActivity extends AppCompatActivity {
    private Handler handler;

    public Handler getHandler() {
        return this.handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        /*permitir conexion en el main??*/
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitAll().build());

        //poner el valor de las variables segun el valor puesto x los putExtra
        Bundle extras = getIntent().getExtras();
        String user = extras.getString("user");
        String ip = extras.getString("ip");
        int port = extras.getInt("port");

        //crear handler
        this.handler = new Handler(user, ip, port);

        this.handler.configureConnection();

        Thread t = new Thread(this.handler);
        t.start();

        //crear los fragments del mando: joystick, buttons y panelInfo(board)
        FragmentManager fm = getSupportFragmentManager();

        Fragment boardFragment;

        boardFragment = fm.findFragmentById(R.id.board_container);
        if (boardFragment == null) {
            boardFragment = new BoardFragment();
            fm.beginTransaction()
                    .add(R.id.board_container, boardFragment) .commit();
        }

        Fragment joystickFragment = fm.findFragmentById(R.id.joystick_container);
        if (joystickFragment == null) {
            joystickFragment = new JoystickFragment();
            fm.beginTransaction()
                    .add(R.id.joystick_container, joystickFragment) .commit();
        }

        Fragment buttonsFragment = fm.findFragmentById(R.id.buttons_container);
        if (buttonsFragment == null) {
            buttonsFragment = new ButtonsFragment();
            fm.beginTransaction()
                    .add(R.id.buttons_container, buttonsFragment) .commit();
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
