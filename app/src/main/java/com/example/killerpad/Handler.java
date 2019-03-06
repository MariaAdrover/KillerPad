package com.example.killerpad;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static android.content.Context.MODE_PRIVATE;

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

    @Override
    public void run() {

        //Comienza el hilo y entra en setConnection donde intentará establecer conexión
        // y crear los elementos necesarios (socket, printwriter, bufferedreader, etc) para
        // mantener la comunicación entre servidor y cliente.

        setConnection();

        //Cuando se establezca conexión el dialog del Spinner se oculta.

        padA.getSpinner().cancel();

        // Cargar el color de la nave de shared preferences
        // >> Hex color value
        SharedPreferences prefs = this.padA.getSharedPreferences ( "savedPrefs", MODE_PRIVATE);
        String color = prefs.getString("color","ffffff");

        //Envia un mensaje utilizando el protocolo de la aplicación para crear un mando nuevo
        // mandando como parámetros el usuario y el color.

        out.println("fromPnew:" + user + "&" + color);

        //El hilo entra en este bucle de comunicación determinado por la variable "alive"
        while(alive) {
            try {
                //Con el método listenServer escucha e interpreta los mensajes del servidor.
                listenServer();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, "handler run excepcion");
            }
        }

    }

    private void cuentaAtras() {

        //Método que se invoca cuando se recibe el mensaje "ded" para indicar la muerte del usuario.
        //Irá actualizando la cuenta atrás de 10  a 0 a cada segundo pasado utilizando el método cuentaAtras.


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
        // Método para cerrar la conexión entre servidor y cliente. Envia un mensaje avisando al servidor

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

        // Si el mensaje no es null se procesa.

        try {
            String data = this.in.readLine();
            Log.d(TAG, data);
            if (data != null) {
                Log.d(TAG, "datos recibidos = " + data);
                processServerMessage(data);
            }
        } catch (IOException e) {
            //Si salta excepción se notifica al user.
            Log.d(TAG, "handler listenServer: IOexception!!!!");
            this.padA.alertUser();//OK
        }
    }

    private void processServerMessage(String data) {

        //Implementa el protocolo de aplicación, discrimina los mensajes y gestiona el comando.

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

    public void setConnection() {

        // Mientras no hay conexión intentar conectarse...
        while (this.socket == null) {
            try {
                this.socket = new Socket(this.ip, this.port);
                Thread.sleep(200);
            } catch (InterruptedException | IOException e) {
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

        connected = true;
    }

    public void sendMessage(String message) {
        //Envia un mensaje al servidor
        out.println(message);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
