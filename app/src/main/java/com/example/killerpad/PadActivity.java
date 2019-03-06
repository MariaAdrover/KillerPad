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

        //Recupera los valores de los extras para que el objeto Handler pueda establecer la conexión.
        Bundle extras = getIntent().getExtras();

        String user = extras.getString("user");
        String ip = extras.getString("ip");
        int port = extras.getInt("port");

        // Cargar TopScore de sharedPreferences
        SharedPreferences prefs = getSharedPreferences("savedPrefs", MODE_PRIVATE);
        topScore = Integer.parseInt(prefs.getString("topScore", "0"));
        //atributo de clase que almacenará la puntuación del jugador en tod.o momento
        // (empieza en 0 al ser una nueva partida)
        score = 0;

        // Crear los fragments
        createFragments();
        //crea el dialog Spinner (Loading animation) que se mantendra hasta que se establezca la conexión.
        createSpinner();


        // Crear el handler para establecer la conexión y arranca su hilo
        this.handler = new Handler(this, user, ip, port);
        new Thread(this.handler).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Oculta la barra con el nombre de la aplicación.
        getSupportActionBar().hide();
    }

    public void alertUser () {

        // Crear un dialog informando de un error de conexión.
        // Dispone de un botón para volver al menú.

        //Utilizamos runOnUi para invocar al hilo que ha creado la jerarquía de vistas para evitar errores.
        // Ya que como vamos a modificar vistas que forman parte de la jerarquía de vistas creada por PadActivity
        // Por lo que el único hilo que puede realizar estas modificaciones es el de PadActivity.

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Si el handler está conectado lo desconectamos.
                if(handler.connected) {
                    handler.disconnect();
                }
                //Crea el dialog y recupera su botón de aceptar y le asigna el button listener.
                alert = new Dialog(PadActivity.this);
                alert.setContentView(R.layout.dialog_alertuser);
                FloatingActionButton bot = alert.findViewById(R.id.botonAlertUser);
                bot.setOnClickListener(PadActivity.this);
                //Muestra el dialog
                alert.show();
            }
        });

    }

    public void askForConfirmation() {
        //Crea un dialog que aparece cuando el usuario pulsa el botón EXIT
        // El usuario puede confirmar si realmente desea salir (vuelta al menú) o cancelar.

        // Método llamado desde el BoardFragment

        //Creamos el Dialog y lo mostramos
        exitConfirmation = new Dialog(this);
        exitConfirmation.setContentView(R.layout.dialog_exit);
        exitConfirmation.show();

        //Recuperamos los botones de aceptar y cancelar para asignarles su correspondiente listener.
        FloatingActionButton bAcep = exitConfirmation.findViewById(R.id.byeB);
        FloatingActionButton bCancel = exitConfirmation.findViewById(R.id.cancelByeB);

        //botón aceptar
        bAcep.setOnClickListener(this);
        //botón cancelar
        bCancel.setOnClickListener(this);
    }

    public void createSpinner(){
        //Método que crea el dialog con la animación del spinner loading.

        //Crea el dialog
        spinner = new Dialog(this);
        spinner.setContentView(R.layout.dialog_spinner);
        //se deshabilita la función para cancelarlo
        spinner.setCancelable(false);

        //Recupera el botón cancelar del dialog y le asigna el listener.
        FloatingActionButton cancel = spinner.findViewById(R.id.spcancel);
        cancel.setOnClickListener(this);

        //muestra el dialog
        spinner.show();
    }

    public void createFragments(){

        //Método que hace las transacciones de los fragments

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

    public void cuentaAtras() {

        //Método para ir actualizando la cuenta atrás del dialog Restart.
        //Cuando la cuenta atrás acaba habilita el botón Reaparecer.


        //Utilizamos runOnUi para invocar al hilo que ha creado la jerarquía de vistas para evitar errores.
        // Ya que como vamos a modificar vistas que forman parte de la jerarquía de vistas creada por PadActivity
        // Por lo que el único hilo que puede realizar estas modificaciones es el de PadActivity.

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

    public void mayBeYouWantRestart(){

        //Método que crea un dialog para mostrar una cuenta atrás cuando el jugador muere.
        //El usuario cuenta con dos botones: Reaparecer y Salir.
        // Cuando se crea el dialog el botón Reaparecer está deshabilitado.
        // El botón Salir vuelve al menú.

        //Utilizamos runOnUi para invocar al hilo que ha creado la jerarquía de vistas para evitar errores.
        // Ya que como vamos a modificar vistas que forman parte de la jerarquía de vistas creada por PadActivity
        // Por lo que el único hilo que puede realizar estas modificaciones es el de PadActivity.
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

    private void resetBoard() {

        //Método llamado por el botón "Reparecer" del Dialog "Restart" para poner el
        // marcador de puntos a 0 otra vez (Cada vez que el usuario muere y reaparece).

        FragmentManager fm = getSupportFragmentManager();
        BoardFragment bf = (BoardFragment) fm.findFragmentById(R.id.board_container);
        TextView text = bf.getScoreTV();
        text.setText("0");
    }

    public void sayBye(){

        //Método para cortar la conexión y volver al menú.

        if(handler.connected) {
            this.handler.disconnect();
        }


        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    public void setTopScore(String key, int value){
        //Método llamado por updateScores para almacenar la puntuación si hay record.

        // guarda el topScore en las shared preferences
        this.topScore = value;

        // Guarda el topScore
        SharedPreferences.Editor sp;
        sp = getSharedPreferences("savedPrefs", MODE_PRIVATE).edit();    // pq es "s"
        sp.putString(key, String.valueOf(value));
        sp.commit();
    }

    public void updateScores(final int points) {

        //Recuperamos el boardfragment para poder acceder a su método getScoreTV para obtener la puntuación actual
        // y la parseamos a int.
        FragmentManager fm = getSupportFragmentManager();
        BoardFragment bf = (BoardFragment) fm.findFragmentById(R.id.board_container);

        final TextView text = bf.getScoreTV();

        final int score = Integer.parseInt(text.getText().toString());

        //Utilizamos runOnUi para invocar al hilo que ha creado la jerarquía de vistas para evitar errores.
        // Ya que como vamos a modificar vistas que forman parte de la jerarquía de vistas creada por PadActivity
        // Por lo que el único hilo que puede realizar estas modificaciones es el de PadActivity.

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //se actualiza la puntuación de la vista
                text.setText(String.valueOf(score + points));
            }
        });

        //actualizamos nuestro atributo de puntos para tener constancia de los puntos en to.do momento
        // Después comparamos si la puntuación actual es superior a la TopScore que recuperamos
        // al principio de las shared preferences, en caso afirmativo actualizamos el record con SetScores.

        this.score += points;

        if (this.score > this.topScore) {
            setTopScore("topScore", this.score);
        }
    }

    public void vibrar(int duration) {
        //Método para vibrar la durante el periodo de tiempo en milisegundos pasado por parámetro.
        // (Requiere permisos para vibrar en manifest)
        Vibrator vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(duration);
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
        //Método para futura implementación.
    }

    @Override
    public void directionChanged(String direction) {

        //Método callback para ser notificado de los mensajes de dirección del joystick
        Log.d("move", direction);
        //handler.sendMessage("pad:" + direction);
        handler.sendMessage(direction);
    }

    @Override
    public void onClick(View v) {

        int clickedButtonID = v.getId();
            if (clickedButtonID==R.id.botonAlertUser) {
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
            }else if(clickedButtonID == R.id.spcancel){
                sayBye();
            }
    }
}

