package com.example.killerpad;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler implements Runnable {
    private PadActivity padA;

    private Socket socket;
    private String user;
    private String ip;
    private int port;

    private BufferedReader in;
    private PrintWriter out;

    private boolean alive;
    public boolean connected;

    private static String TAG = "handler";

    public Handler(PadActivity activity, String user, String ip, int port) {
        this.padA = activity;
        this.user = user;
        this.ip = ip;
        this.port = port;

        this.alive = true;
        this.connected = false;
    }


    //nombre cambiado (original: configureConnection)
    public void setConnection() { //cambiar al constructor? separar metodos?

        //si nulo vuelve a intentar conectarse

        while (this.socket == null) {
            try {
                this.socket = new Socket(this.ip, this.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        } catch (IOException e) {
            this.socket = null;
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void processServerMessage(String data) {
        String header = data.substring(0, 3);

        switch (header.trim()) {
            case "vib": // vibrar
                padA.vibrar(300);
                break;
            case "pnt": // puntos
                int p = Integer.parseInt(data.substring(3));
                padA.updateScores(p);
                break;
            case "ded": // morir
                padA.vibrar(1500);
                break;
        }
    }

    public void listenServer() {
        try {
            String data = this.in.readLine();
//            Log.i(TAG, data);
            if (data != null) {
                Log.i(TAG, "datos recibidos = " + data);
                processServerMessage(data);
            } else {
                Log.i(TAG, "datos recibidos = " + data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        setConnection();
        connected = true;
        padA.getSpinner().cancel();
        out.println("from:P/user:"+user);
        Log.i(TAG, "run handler");
        while(alive) {
            try {
                Log.i(TAG, "try handler");
                listenServer();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
