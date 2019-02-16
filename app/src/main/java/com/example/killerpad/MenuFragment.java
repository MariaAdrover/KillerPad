package com.example.killerpad;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MenuFragment extends Fragment {

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

        //test
        this.buttonInfoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //poner dialogo con informacion (?)
            }
        });

        return v;
    }


    //************** myMethods ****************



    private void showConfigDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_connect); //añade ñayout al dialogo
        dialog.show();  //muestra el dialogo

        FloatingActionButton bAceptar = dialog.findViewById(R.id.fButtonAceptar);
        FloatingActionButton bCancelar = dialog.findViewById(R.id.fButtonCancelar);

        // editText de cada campo
        EditText etUsername = dialog.findViewById(R.id.username);
        EditText etIp = dialog.findViewById(R.id.ip);
        EditText etPort = dialog.findViewById(R.id.puerto);

        // carga las configuraciones con las shared preferences
        etUsername.setText(((MenuActivity)getActivity()).loadPreference("user"));
        etIp.setText(((MenuActivity)getActivity()).loadPreference("ip"));
        etPort.setText(((MenuActivity)getActivity()).loadPreference("port"));

        //EVENTO: al pular boton aceptar: configurar ursName, ip, puerto
        bAceptar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PadActivity.class);

                //  coje del dialogo el obj del editText e indicamos q es un editText (parse). Luego pillamos el texto de dentro y lo pasamos a string
                String user = ((EditText) dialog.findViewById(R.id.username)).getText().toString();
                String ip = ((EditText) dialog.findViewById(R.id.ip)).getText().toString();

                //matadme por favor...
                int port = Integer.parseInt(((EditText) dialog.findViewById(R.id.puerto)).getText().toString());

                // se las pasa por gracias al put extra
                intent.putExtra("user", user);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);

                // guarda las configuraciones en las shared preferences
                ((MenuActivity)getActivity()).savePreferences("user",user);
                ((MenuActivity)getActivity()).savePreferences("ip",ip);
                ((MenuActivity)getActivity()).savePreferences("port",Integer.toString(port));

                startActivity(intent);
            }
        });

        //EVENTO: al pusltar boton canclear: cerrar dialogo
        bCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }









}
