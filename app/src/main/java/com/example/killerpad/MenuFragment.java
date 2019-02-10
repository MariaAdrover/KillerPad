package com.example.killerpad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {
    Button goPad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        this.goPad = (Button) v.findViewById(R.id.go_to_pad);
        this.goPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PadActivity.class);
                String user = "mia";
                String ip = "192.168.1.46";
                int port = 8000;

                intent.putExtra("user", user);
                intent.putExtra("ip", ip);
                intent.putExtra("port", port);

                startActivity(intent);
            }
        });

        return v;
    }

}
