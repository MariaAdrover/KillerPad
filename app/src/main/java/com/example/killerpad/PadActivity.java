package com.example.killerpad;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PadActivity extends AppCompatActivity implements JoystickView.JoystickListener,  View.OnClickListener {

    private Handler handler;
    private static final String TAG = "hola";
    private int topScore;
    private int score;

    private Dialog spinner;
    private Dialog alert;
    private Dialog exitConfirmation;
    private Dialog restart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);
        Log.d(TAG,"on create");



        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitAll().build());

        //poner el valor de las variables segun el valor puesto x los putExtra
        Bundle extras = getIntent().getExtras();

        String user = extras.getString("user");
        String ip = extras.getString("ip");
        int port = extras.getInt("port");
        score = 0;

        // Cargar TopScore de sharedPreferences
        SharedPreferences prefs = getSharedPreferences("savedPrefs", MODE_PRIVATE);
        topScore = Integer.parseInt(prefs.getString("topScore", "0"));

        Log.d(TAG,"record= " + topScore);

        // Crear los fragments
        createFragments();

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

        // Crear el handler
        this.handler = new Handler(this, user, ip, port);
        Thread t = new Thread(this.handler);
        t.start();

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

        Log.d(TAG,"padActivity:on resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"on pause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"on stop");

    }

    public void createFragments(){

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
        // Método llamado desde el BoardFragment al pulsar el boton EXIT
        exitConfirmation = new Dialog(this);
        exitConfirmation.setContentView(R.layout.dialog_exit);
        exitConfirmation.show();

        FloatingActionButton bAcep = exitConfirmation.findViewById(R.id.byeB);
        FloatingActionButton bCancel = exitConfirmation.findViewById(R.id.cancelByeB);

        // Al pulsar boton aceptar: volvemos al menu
        bAcep.setOnClickListener(this);

        // Al pulsar boton cancelar: se oculta el dialog
        bCancel.setOnClickListener(this);
    }

    public void sayBye(){
        // El handler le envia un mensaje al servidor para notificar la desconexión
        if(handler.connected) {
            this.handler.disconnect();
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

        this.score += points;

        if (this.score > this.topScore) {
            setTopScore("topScore", this.score);

            // pruebaaaaa
            this.handler.sendMessage("from el janler:El topScore es " + this.topScore);// quitar, kk solo test
        }
    }

    // guarda el topScore en las shared preferences
    public void setTopScore(String key, int value){
        this.topScore = value;

        // Guarda el topScore
        SharedPreferences.Editor sp;
        sp = getSharedPreferences("savedPrefs", MODE_PRIVATE).edit();    // pq es "s"
        sp.putString(key, String.valueOf(value));
        sp.commit();
    }

    public Handler getHandler() {
        return this.handler;
    }

    public Dialog getSpinner() {
        return spinner;
    }

    public void dead() {
        Log.d("TAG", "PadActivity0.0: dead ");

    }

    public void alertUser () {
        Log.d("TAG", "PadActivity0: ONcLICK ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "PadActivity1: ONcLICK ");
                if(handler.connected) {
                    handler.disconnect();//creo que da error xk socket=null REVISAR
                }
                Log.d("TAG", "PadActivity2: ONcLICK " + Thread.currentThread().getName());
                alert = new Dialog(PadActivity.this);
                alert.setContentView(R.layout.dialog_alertuser);
                FloatingActionButton bot = alert.findViewById(R.id.botonAlertUser);

                Log.d("TAG", "PadActivity3: ONcLICK " + Thread.currentThread().getName());
                // Al pulsar boton aceptar: volvemos al menu
                bot.setOnClickListener(PadActivity.this);
                //Activity com.example.killerpad.PadActivity has leaked window DecorView@1b910e2[PadActivity] that was originally added here
                alert.show();
            }
        });

    }

    public void mayBeYouWantRestart(){
        Log.d("TAG", "PadActivity: mayBeYouWantRestart " + Thread.currentThread().getName());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                restart = new Dialog(PadActivity.this);
                restart.setContentView(R.layout.dialog_restart);
                restart.setCancelable(false);

                FloatingActionButton bRestart = restart.findViewById(R.id.botonRestart);
                bRestart.setOnClickListener(PadActivity.this);
                bRestart.setClickable(false);

                FloatingActionButton bCancel = restart.findViewById(R.id.botonCancelRestart);
                bCancel.setOnClickListener(PadActivity.this);

                restart.show();
                cuentaAtras();
            }
        });

    }

    public void cuentaAtras() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textCuentaAtras = restart.findViewById(R.id.cuantatras);
                FloatingActionButton bCancel = restart.findViewById(R.id.botonCancelRestart);
                FloatingActionButton bRestart = restart.findViewById(R.id.botonRestart);

                int cuenta = Integer.parseInt(textCuentaAtras.getText().toString()) - 1;
                String text = String.valueOf(cuenta);

                if (cuenta >= 0) {
                    textCuentaAtras.setText(text);
                } else {
                    bRestart.setClickable(true);
                    TextView textRestart = (restart.findViewById(R.id.textoRestart));
                    textRestart.setText("¿Quieres volver a jugar?");
                }

            }
        });

    }

    private void resetBoard() {

        FragmentManager fm = getSupportFragmentManager();
        BoardFragment bf = (BoardFragment) fm.findFragmentById(R.id.board_container);
        TextView text = bf.getScoreTV();
        text.setText("0");
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) {
        //  Log.d("move","X: "+xPercent+" Y: "+yPercent);
    }

    @Override
    public void directionChanged(String direction) {
        Log.d("move", direction);
        //handler.sendMessage("pad:" + direction);
        handler.sendMessage(direction);
    }

    @Override
    public void onClick(View v) {

        /*
        Funciona de las dos maneras

        1)
        Log.d(TAG,"a"+v.getId());
        Log.d(TAG,"b"+R.id.botonAlertUser);

        2)
        Log.d(TAG, "a " + alert.findViewById(R.id.botonAlertUser).getId());
        Log.d(TAG, "b " + v.getId());

        PD: nada nuevo, pero odio android
        */

        int clickedButtonID = v.getId();
            if (clickedButtonID==R.id.botonAlertUser) {
                Log.d("TAG", "PadActivity: ONcLICK listener " + Thread.currentThread().getName());

                startActivity(new Intent(this, MenuActivity.class));
                finish();
            } else if (clickedButtonID == R.id.byeB) {
                sayBye();
            } else if (clickedButtonID == R.id.cancelByeB) {
                exitConfirmation.hide();
            } else if (clickedButtonID == R.id.botonCancelRestart) {
                sayBye();
            } else if (clickedButtonID == R.id.botonRestart) {
                handler.sendMessage("replay");
                resetBoard();
                restart.hide();
            }
    }
}

