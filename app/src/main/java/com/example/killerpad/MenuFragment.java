package com.example.killerpad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.content.ContextCompat.getSystemService;


/**
 * guardar el color
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    private Bundle sis;
    private Button goPad;
    private FloatingActionButton buttonInfoDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);


        // cargar las topscores de shared preferences
        SharedPreferences prefs = getContext().getSharedPreferences ( "savedPrefs", MODE_PRIVATE);
        //recupermos el textview y le ponemos de texto, el valor de las shared preferences
        ((TextView) v.findViewById(R.id.tops)).setText(prefs.getString("topScore","0"));

        this.goPad = (Button) v.findViewById(R.id.go_to_pad);
        this.buttonInfoDialog = (FloatingActionButton) v.findViewById(R.id.buttonInfo);

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
                showInfoDialog();
            }
        });

        return v;
    }

    //************** myMethods ****************

    private void showInfoDialog() {
        FloatingActionButton[] a;


        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_color_picker); //añade ñayout al dialogo
        dialog.show();  //muestra el dialogo


    }

    private void loadConfig(String key, EditText et) {
        et.setText(((MenuActivity) getActivity()).loadPreference(key));
    }

    private void saveConfig(String key, String value) {
        ((MenuActivity) getActivity()).savePreferences(key, value);
    }

    private void showConfigDialog() {

        FloatingActionButton bAceptar;
        FloatingActionButton bCancelar;

        EditText etUsername;
        EditText etIp;
        EditText etPort;

        // para que pueda acceder al metodo ik
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_connect); //añade ñayout al dialogo
        dialog.show();  //muestra el dialogo
        


        bAceptar = dialog.findViewById(R.id.fButtonAceptar);
        bCancelar = dialog.findViewById(R.id.fButtonCancelar);

        // editText de cada campo
        etUsername = dialog.findViewById(R.id.username);
        etIp = dialog.findViewById(R.id.ip);
        etPort = dialog.findViewById(R.id.puerto);

        // carga las configuraciones con las shared preferences
        loadConfig("user", etUsername);
        loadConfig("ip", etIp);
        loadConfig("port", etPort);
        bAceptar.setOnClickListener(this);

        //EVENTO: al pular boton aceptar: configurar ursName, ip, puerto
        bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String user;
                String ip;
                int port;

                intent = new Intent(getActivity(), PadActivity.class);

                //  coje del dialogo el obj del editText e indicamos q es un editText (parse). Luego pillamos el texto de dentro y lo pasamos a string
                user = ((EditText) dialog.findViewById(R.id.username)).getText().toString();
                ip = ((EditText) dialog.findViewById(R.id.ip)).getText().toString();

                //matadme por favor...
                port = Integer.parseInt(((EditText) dialog.findViewById(R.id.puerto)).getText().toString());

                // se las pasa por gracias al put extra
                intent.putExtra("user", user);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);

                // guarda las configuraciones en las shared preferences
                saveConfig("user", user);
                saveConfig("ip", ip);
                saveConfig("port", String.valueOf(port));

                dialog.cancel();
                startActivity(intent);
                getActivity().finish();

            }
        });

        //EVENTO: al pusltar boton canclear: cerrar dialogo
        bCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    @Override
    public void onClick(View v) {
        String s = v.getResources().getResourceEntryName(v.getId());

        switch (s){
            case "rojo":
                break;

            case "azul":
                break;

            case "verde":
                break;

        }
    }
}
