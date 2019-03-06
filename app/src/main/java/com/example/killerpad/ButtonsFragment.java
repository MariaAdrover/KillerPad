package com.example.killerpad;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ButtonsFragment extends Fragment implements View.OnClickListener {

    private Button bSend;
    private Button bSuicide;
    private PadActivity activity;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_buttons, container, false);

        //almacena el handler.
        this.handler = ((PadActivity) this.getActivity()).getHandler();

        //Añade los Listeners a los botones de disparo y kill me
        this.bSend = v.findViewById(R.id.send);
        bSend.setOnClickListener(this);

        this.bSuicide = v.findViewById(R.id.killMe);

        // -- escuchador desactivado --
        //bSuicide.setOnClickListener(this);

        this.activity = (PadActivity)getActivity();

        return v;
    }

    @Override
    public void onClick(View v) {

        int button = v.getId();

        //botón KillMe (desactivado)
        if (button == R.id.killMe) {
            this.handler.sendMessage("replay");

        //botón de disparo
        } else if (button == R.id.send) {
            this.handler.sendMessage("shoot");

        }

    }
}
