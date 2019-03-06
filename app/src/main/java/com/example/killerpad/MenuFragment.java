package com.example.killerpad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static android.content.Context.MODE_PRIVATE;

import java.util.ArrayList;


/**
 * guardar el color
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    private Bundle sis;
    private Button goToPad;
    private Button bColorPicker;

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

        // almacenamos en una variable las shared preferences
        // y recuperamos el textview y establecemos el valor recuperado de las topscores de sharedpreferences.
        SharedPreferences prefs = getContext().getSharedPreferences ( "savedPrefs", MODE_PRIVATE);
        ((TextView) v.findViewById(R.id.tops)).setText(prefs.getString("topScore","0"));
      
        // almacenamos el boton que inicia nueva partida y muestra el dialogo de configuración
        this.goToPad = (Button) v.findViewById(R.id.go_to_pad);

        //añade listener en el botón.
        this.goToPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //muestra el dialog para introducir la configuracion
                showConfigDialog();
            }
        });

        // almacenamos el boton para elegir el color de la nave
        this.bColorPicker = (Button) v.findViewById(R.id.buttonInfo);

        // añade el listener en el botón.
        this.bColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        return v;
    }

    //************** myMethods ****************

    public void acceptColor(){
        //almacena el surfaceview (ShipView) y utilizando su método para actualizar el color de la nave
        // pasando por parámetro el color recuperado de shared preferences
        ShipView shipView = ((ShipView) ((MenuActivity)getActivity()).findViewById(R.id.shipView));
        shipView.updateColor((String) getContext().getSharedPreferences("savedPrefs",Context.MODE_PRIVATE).getString("color","ffffff"));
        //finalmente cancela el dialog.
        this.colorDialog.cancel();
    }

    private void addColorListeners() {

        // almacenamos en un arraylist todos los botones de color que se encuentran en el dialog del color picker

        ArrayList<Button> arrButtons = new ArrayList<>();


        arrButtons.add((Button) this.colorDialog.findViewById(R.id.orangeButton));
        arrButtons.add((Button) this.colorDialog.findViewById(R.id.limaButton));
        arrButtons.add((Button) this.colorDialog.findViewById(R.id.yellowButton));

        arrButtons.add((Button) this.colorDialog.findViewById(R.id.fucsiaButton));
        arrButtons.add((Button) this.colorDialog.findViewById(R.id.whiteButton));
        arrButtons.add((Button) this.colorDialog.findViewById(R.id.purpleButton));

        arrButtons.add((Button) this.colorDialog.findViewById(R.id.blueButton));
        arrButtons.add((Button) this.colorDialog.findViewById(R.id.redButton));
        arrButtons.add((Button) this.colorDialog.findViewById(R.id.aquaMarineButton));


        //recorremos el arraylist entero, añadiendo el listener a cada elemento botón.
        for (int button = 0; button < arrButtons.size(); button++) {
            arrButtons.get(button).setOnClickListener(this);
        }


    }

    public void connectPadActivity(View v){

        //método para el listener del botón aceptar del dialog de configuración de conexión.
        //recibimos como parámetro el botón.

        Intent intent;
        String user;
        String ip;
        int port;

        // cierra el dialogo
        this.configurationDialog.cancel();

        // crea un nuevo intent
        intent = new Intent(getActivity(), PadActivity.class);

        //  recupera los valores de los editText(Parseados) del dialog
        user = ((EditText) this.configurationDialog.findViewById(R.id.username)).getText().toString();
        ip = ((EditText) this.configurationDialog.findViewById(R.id.ip)).getText().toString();
        port = Integer.parseInt(((EditText) this.configurationDialog.findViewById(R.id.puerto)).getText().toString());

        // pasa la información (user, ip, port) utilizando putExtra para el intent
        intent.putExtra("user", user);
        intent.putExtra("ip", ip);
        intent.putExtra("port", port);

        // guarda las configuraciones en las shared preferences
        saveConfig("user", user);
        saveConfig("ip", ip);
        saveConfig("port", String.valueOf(port));

        // inicia la actividad del pad
        startActivity(intent);

        // acaba la menu activity
        getActivity().finish();
    }

    private void loadConfigurationDialog() {

        //invocado por showConfigDialog() para rellenar los campos (user,ip,puerto)
        // con la última configuración (sharedpreferences).

        FloatingActionButton bAceptar;
        FloatingActionButton bCancelar;

        EditText etUsername;
        EditText etIp;
        EditText etPort;

        // recueramos los botones del dialog de configuración
        bAceptar = this.configurationDialog.findViewById(R.id.fButtonAceptar);
        bCancelar = this.configurationDialog.findViewById(R.id.fButtonCancelar);

        // editText de cada campo.
        etUsername = this.configurationDialog.findViewById(R.id.username);
        etIp = this.configurationDialog.findViewById(R.id.ip);
        etPort = this.configurationDialog.findViewById(R.id.puerto);

        // carga las configuraciones con las shared preferences
        loadConfig("user", etUsername);
        loadConfig("ip", etIp);
        loadConfig("port", etPort);

        //añade los listener para los botones aceptar y cancelar.
        bAceptar.setOnClickListener(this);
        bCancelar.setOnClickListener(this);

    }

    private void loadConfig(String key, EditText et) {
        // pasado una clave y un editText, utilizando loadPreferences carga el valor de la clave
        // y lo carga en el editText.
        et.setText(((MenuActivity) getActivity()).loadPreferences(key));
    }

    private void showColorPickerDialog() {
        //invocado por bColorPicker
        //(ventana para elegir color.)
        //Inicializa el atributo de clase colorDialog, le establece el layout
        // y llama a addColorListeners() para establecer los listeners a cada uno de sus botones.

        this.colorDialog = new Dialog(this.getContext());
        this.colorDialog.setContentView(R.layout.dialog_color_picker);
        this.addColorListeners();

        //muestra el dialogo
        this.colorDialog.show();

    }

    private void showConfigDialog() {

        //invocado por goToPad button.
        //(ventana para establecer la configuración de conexión y conectarse).

        this.configurationDialog = new Dialog(this.getContext());
        this.configurationDialog.setContentView(R.layout.dialog_connect);
        this.loadConfigurationDialog();
        this.configurationDialog.show();

    }

    private void saveConfig(String key, String value) {
        // Pasado una clave y un valor, utilizando savePreferences() almacena
        // el valor con la clave dada en SharedPreferences.
        ((MenuActivity) getActivity()).savePreferences(key, value);
    }


    @Override
    public void onClick(View v) {

    //discrima el evento a asignar por la id de la view pasada por parametro.

        switch (v.getId()) {

            // dialog de los botones de colores.
            case R.id.orangeButton:
                saveConfig("color", "ff9800");
                acceptColor();
                break;

            case R.id.blueButton:
                saveConfig("color", "257EFF");
                acceptColor();
                break;

            case R.id.limaButton:
                saveConfig("color", "A7FF18");
                acceptColor();
                break;

            case R.id.redButton:
                saveConfig("color", "FF0000");
                acceptColor();
                break;

            case R.id.aquaMarineButton:
                saveConfig("color", "11bfb9");
                acceptColor();
                break;

            case R.id.fucsiaButton:
                saveConfig("color", "f24694");
                acceptColor();
                break;

            case R.id.whiteButton:
                saveConfig("color", "ffffff");
                acceptColor();
                break;

            case R.id.yellowButton:
                saveConfig("color", "F0EB3B");
                acceptColor();
                break;

            case R.id.purpleButton:
                saveConfig("color", "6E28E0");
                acceptColor();
                break;


            // dialogo de configuración de conexión.

            //botón cancelar
            case R.id.fButtonCancelar:
                this.configurationDialog.cancel();
                break;

            //botón aceptar
            case R.id.fButtonAceptar:
                this.connectPadActivity(v);
                break;

        }
    }


}
