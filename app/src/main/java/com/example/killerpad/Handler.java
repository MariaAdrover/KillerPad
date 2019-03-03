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

    public void setConnection() {

        // Mientras no hay conexión intentar conectarse...
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
                Log.d(TAG, "recibido = " + data);
                padA.vibrar(1500);
                padA.mayBeYouWantRestart();
                //padA.cuentaAtras();
                cuentaAtras();
                break;
        }
    }

    private void cuentaAtras() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 10; i >= 0; i--) {

            padA.cuentaAtras();
            try {
                Thread.sleep(1100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        this.sendMessage("bye");
        this.alive = false;

        try {
            if(this.socket!=null) {
                this.socket.close();
                this.socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenServer() {
        try {
            String data = this.in.readLine();
            Log.d(TAG, data);
            if (data != null) {
                Log.d(TAG, "datos recibidos = " + data);
                processServerMessage(data);
            }
        } catch (IOException e) {
            Log.d(TAG, "handler listenServer: IOexception!!!!");
            this.padA.alertUser();//OK
        }
    }

    @Override
    public void run() {
        setConnection();
        connected = true;
        padA.getSpinner().cancel();
        //out.println("from:P/user:"+user);
        // Manda color rosa, luego tendra que ser la variable que esta en shared preferences
        out.println("fromPnew:" + user + "&" + "dd4db7"); // >> Hex color value //dd4db7  //4ae2d6   0x hex
        Log.d(TAG, "run handler");
        while(alive) {
            try {
                Log.d(TAG, "handler: try handler");
                listenServer();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, "handler run excepcion");
            }
        }

    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
