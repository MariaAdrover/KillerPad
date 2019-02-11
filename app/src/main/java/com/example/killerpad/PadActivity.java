package com.example.killerpad;

import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class PadActivity extends AppCompatActivity implements JoystickView.JoystickListener {
    private Socket socket = null;
    private String ip = "192.168.1.5";
    private int port = 1500;
    private BufferedReader in;
    private PrintWriter out;
    private Handler handler;

    public Handler getHandler() {
        return this.handler;
    }

    private void requestConnection() {
        try {
            this.socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

       //permitir conexion en el main
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitAll().build());

        //solicitar conexion
        while (this.socket == null) {
            this.requestConnection();
        }

        //crear handler
        this.handler = new Handler();

        //configurar handler
        this.handler.setHandler(this.socket, this.ip, this.port);

        Thread t = new Thread(this.handler);
        t.start();

        //Enviar mensaje de prueba desde handler
        this.handler.sendMessage("handler: holi desde el kk mando");

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
