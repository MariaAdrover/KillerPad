package com.example.killerpad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.content.ContextCompat.getSystemService;
import java.util.ArrayList;


/**
 * guardar el color
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    private Bundle sis;
    private Button goToPad;
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

        // cargar las topscores de shared preferences
        SharedPreferences prefs = getContext().getSharedPreferences ( "savedPrefs", MODE_PRIVATE);
      
        //recupermos el textview y le ponemos de texto, el valor de las shared preferences
        ((TextView) v.findViewById(R.id.tops)).setText(prefs.getString("topScore","0"));
      
        // para comenzar nueva partida => muestra el dialogo de conifguracion
        this.goToPad = (Button) v.findViewById(R.id.go_to_pad);

        // para elegit el color de la nave
        this.buttonInfoDialog = (Button) v.findViewById(R.id.buttonInfo);

        //añade listener en el boton.
        this.goToPad.setOnClickListener(new View.OnClickListener() {
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



        // guarda este color para tener uno por defecto
        // saveConfig("color", "123456");

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

        Button[] colorButton = new Button[9];
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

        for (int button = 0; button < arrButtons.size(); button++) {
            arrButtons.get(button).setOnClickListener(this);
        }


    }

    private void showConfigDialog() {

        this.configurationDialog = new Dialog(this.getContext());
        this.configurationDialog.setContentView(R.layout.dialog_connect);
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



            // dialogo para empezar nueva partida


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
                this.configurationDialog.cancel();
                break;

        }
    }

    public void acceptColor(){
        ShipView tpm = ((ShipView) ((MenuActivity)getActivity()).findViewById(R.id.shipView));

        tpm.updateColor((String) getContext().getSharedPreferences("savedPrefs",Context.MODE_PRIVATE).getString("color","ffffff"));

        this.colorDialog.cancel();
    }

}
