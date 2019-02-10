package com.example.killerpad;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MenuFragment extends Fragment {
    private Button goPad;
    private FloatingActionButton buttonInfoDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        //añade listener en el boton.
        this.goPad = (Button) v.findViewById(R.id.go_to_pad);

        //mostrar dialogo
        this.goPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_connect);
                dialog.show();


                FloatingActionButton bGo = dialog.findViewById(R.id.fButtonAceptar);
                bGo.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PadActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        this.buttonInfoDialog = (FloatingActionButton) v.findViewById(R.id.buttonInfo);

        //test
        this.buttonInfoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_connect);
                dialog.show();

                String user = ((EditText) dialog.findViewById(R.id.ip)).getText().toString();

            }
        });

        return v;
    }



}
