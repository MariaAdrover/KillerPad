package com.example.killerpad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * guardar el color
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    private Bundle sis;
    private Button goPad;
    private Button buttonInfoDialog;

    //Dialogs
    private Dialog colorDialog;
    private Dialog configurationDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // carga el layout
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(400);

        // para comenzar nueva partida => muestra el dialogo de conifguracion
        this.goPad = (Button) v.findViewById(R.id.go_to_pad);

        // para elegit el color de la nave
        this.buttonInfoDialog = (Button) v.findViewById(R.id.buttonInfo);

        //añade listener en el boton.
        this.goPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //muestra el dialog para introducir la configuracion
                showConfigDialog();
            }
        });

        // futuro color picker
        this.buttonInfoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        return v;
    }

    //************** myMethods ****************

    private void loadConfigurationDialoog() {
        FloatingActionButton bAceptar;
        FloatingActionButton bCancelar;

        EditText etUsername;
        EditText etIp;
        EditText etPort;

        // para que pueda acceder al metodo ik
        bAceptar = this.configurationDialog.findViewById(R.id.fButtonAceptar);
        bCancelar = this.configurationDialog.findViewById(R.id.fButtonCancelar);

        // editText de cada campo
        etUsername = this.configurationDialog.findViewById(R.id.username);
        etIp = this.configurationDialog.findViewById(R.id.ip);
        etPort = this.configurationDialog.findViewById(R.id.puerto);

        // carga las configuraciones con las shared preferences
        loadConfig("user", etUsername);
        loadConfig("ip", etIp);
        loadConfig("port", etPort);

        bAceptar.setOnClickListener(this);
        bCancelar.setOnClickListener(this);

    }

    private void loadConfig(String key, EditText et) {
        et.setText(((MenuActivity) getActivity()).loadPreference(key));
    }

    private void showColorPickerDialog() {

        this.colorDialog = new Dialog(this.getContext());
        this.colorDialog.setContentView(R.layout.dialog_color_picker);
        this.addColorListeners();
        this.colorDialog.show();  //muestra el dialogo

    }

    private void addColorListeners() {

        FloatingActionButton[] colorButton = new FloatingActionButton[10]; //test

        Button colorOrangeButton;
        Button colorGreenButton;
        Button colorPinkButton;
        Button acceptButton;


        colorOrangeButton = this.colorDialog.findViewById(R.id.orangeButton);
        colorGreenButton = this.colorDialog.findViewById(R.id.greenButton);
        colorPinkButton = this.colorDialog.findViewById(R.id.pinkButton);
        acceptButton = this.colorDialog.findViewById(R.id.accept);


        colorOrangeButton.setOnClickListener(this);
        colorGreenButton.setOnClickListener(this);
        colorPinkButton.setOnClickListener(this);
        acceptButton.setOnClickListener(this);

    }

    private void showConfigDialog() {

        this.configurationDialog = new Dialog(this.getContext());
        this.configurationDialog.setContentView(R.layout.dialog_configuration);
        this.loadConfigurationDialoog();
        this.configurationDialog.show();

    }

    private void saveConfig(String key, String value) {
        ((MenuActivity) getActivity()).savePreferences(key, value);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // dialoog de los colores
            case R.id.orangeButton:
                this.saveConfig("color", "#333456");
                ((TextView) this.colorDialog.findViewById(R.id.colorTest)).setText("naranja");
                break;

            case R.id.pinkButton:
                this.saveConfig("color", "ff0000");
                ((TextView) this.colorDialog.findViewById(R.id.colorTest)).setText("rojo");
                break;

            case R.id.greenButton:
                this.saveConfig("color", "#33cc33");
                ((TextView) this.colorDialog.findViewById(R.id.colorTest)).setText("verde");
                break;

            // dialogo para empezar nueva partida
            case R.id.accept:
                this.colorDialog.cancel();
                break;

            case R.id.fButtonAceptar:
                Intent intent;
                String user;
                String ip;
                int port;

                // cierra el dialogo
                this.configurationDialog.cancel();

                // crea un nuevo intent
                intent = new Intent(getActivity(), PadActivity.class);

                //  coge del dialogo el obj del editText e indicamos q es un editText (parse). Luego pillamos el texto de dentro y lo pasamos a string
                user = ((EditText) this.configurationDialog.findViewById(R.id.username)).getText().toString();
                ip = ((EditText) this.configurationDialog.findViewById(R.id.ip)).getText().toString();

                //matadme por favor...
                port = Integer.parseInt(((EditText) this.configurationDialog.findViewById(R.id.puerto)).getText().toString());

                // se las pasa por gracias al put extra
                intent.putExtra("user", user);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);

                // guarda las configuraciones en las shared preferences
                saveConfig("user", user);
                saveConfig("ip", ip);
                saveConfig("port", String.valueOf(port));


                // cierra el dialogo
                this.configurationDialog.cancel();

                // inicia la actividad del pad
                startActivity(intent);

                // acaba la menu activity
                getActivity().finish();
                break;

            case R.id.fButtonCancelar:
                this.colorDialog.cancel();
                break;

        }
    }

}
