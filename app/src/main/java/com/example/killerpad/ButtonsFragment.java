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
    private PadActivity activity;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buttons, container, false);

        this.handler = ((PadActivity) this.getActivity()).getHandler();

        this.bSend = v.findViewById(R.id.send);
        bSend.setOnClickListener(this);
        this.activity = (PadActivity)getActivity();

        return v;
    }

    @Override
    public void onClick(View v) {
        this.handler.sendMessage("pad:shoot");

    }
}
