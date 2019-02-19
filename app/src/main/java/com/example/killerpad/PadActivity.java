package com.example.killerpad;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PadActivity extends AppCompatActivity implements JoystickView.JoystickListener {

    private Handler handler;
    private static final String TAG = "handler";
    private int topScore;
    private int score;

    private Dialog spinner;


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
        score = 0;

        fragmentCreate();


        spinner = new Dialog(this);
        spinner.setContentView(R.layout.dialog_spinner);
        spinner.setCancelable(false);

        FloatingActionButton cancel = spinner.findViewById(R.id.spcancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sayBye();
                    }
                }
        );

        spinner.show();

        //crear handler
        this.handler = new Handler(this, user, ip, port);
        Thread t = new Thread(this.handler);
        t.start();

        //crear los fragments
        fragmentCreate();


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

        Log.d(TAG,"on resume");
    }


    public void fragmentCreate(){

        FragmentManager fm = getSupportFragmentManager();

        Fragment board_fragment = fm.findFragmentById(R.id.board_container);
        if (board_fragment == null) {
            board_fragment = new BoardFragment();
            fm.beginTransaction()
                    .add(R.id.board_container, board_fragment).commit();

        }
        Fragment joystick_fragment = fm.findFragmentById(R.id.joystick_container);
        if (joystick_fragment == null) {
            joystick_fragment = new JoystickFragment();
            fm.beginTransaction()
                    .add(R.id.joystick_container, joystick_fragment).commit();
        }
        Fragment buttons_fragment = fm.findFragmentById(R.id.buttons_container);
        if (buttons_fragment == null) {
            buttons_fragment = new ButtonsFragment();
            fm.beginTransaction()
                    .add(R.id.buttons_container, buttons_fragment).commit();
        }

    }

    public void askForConfirmation() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.show();

        FloatingActionButton bAceptar = dialog.findViewById(R.id.byeB);
        FloatingActionButton bCancelar = dialog.findViewById(R.id.cancelByeB);

        //evento al pular boton aceptar: configurar ursName, ip, puerto
        bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuActivity.class);

                sayBye();
                startActivity(intent);
            }
        });

        //evento al pular boton aceptar: configurar ursName, ip, puerto
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PadActivity.class);
                dialog.hide();
            }
        });
    }

    public void sayBye(){

        if(handler.connected) {
            this.handler.sendMessage("pad:bye");
            this.handler.setAlive(false);
        }
        startActivity(new Intent(this, MenuActivity.class));
        finish();

    }

    public void vibrar(int duration) {
        Vibrator vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(duration);

    }

    public void updateScores(final int points) {
        FragmentManager fm = getSupportFragmentManager();
        BoardFragment bf = (BoardFragment) fm.findFragmentById(R.id.board_container);
        final TextView text = bf.getScoreTV();
        final int score = Integer.parseInt(text.getText().toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(String.valueOf(score + points));
            }
        });

        this.score+= points;

    }

    public void dead() {

    }

    public Handler getHandler() {
        return this.handler;
    }

    public Dialog getSpinner() {
        return spinner;
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        //  Log.d("move","X: "+xPercent+" Y: "+yPercent);
    }

    @Override
    public void directionChanged(String direction) {
        Log.d("move", direction);
        handler.sendMessage("pad:" + direction);
    }
}
