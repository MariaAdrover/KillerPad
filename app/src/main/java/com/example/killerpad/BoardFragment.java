package com.example.killerpad;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BoardFragment extends Fragment{
    private TextView scoreTV;
    private Button exitB;
    private static String TAG = "handler";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_board, container, false);

        this.scoreTV = (TextView) v.findViewById(R.id.scoreTV);
        this.exitB = (Button) v.findViewById((R.id.exitB));

        // Volver al menu
        this.exitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForConfirmation();
            }
        });

        return v;
    }

    private void askForConfirmation() {

        //Método para terminar la partida: Se cierra la actividad, vuelve al menú y se corta la conexión.
        //Se crea un dialog con dos botones: Aceptar y Cancelar.

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_exit);

        FloatingActionButton bAceptar = dialog.findViewById(R.id.byeB);
        FloatingActionButton bCancelar = dialog.findViewById(R.id.cancelByeB);

        //evento al pular boton aceptar: configurar ursName, ip, puerto
        bAceptar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
                ((PadActivity) getActivity()).sayBye();

            }
        });

        //evento al pular boton aceptar: configurar ursName, ip, puerto
        bCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public TextView getScoreTV(){
        return scoreTV;
    }




}

