package com.example.killerpad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket socket;
    private String user;
    private String ip;
    private int port;
    private BufferedReader in;
    private PrintWriter out;

    public Handler(String user, String ip, int port) {
        this.user = user;
        this.ip = ip;
        this.port = port;
    }

    public void configureConnection() { //cambiar al constructor? separar metodos?
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
        // poner lo que hacemos al recibir menssajes del servidor
    }

    public void listenServer() {
        try {
            String data = this.in.readLine();
            processServerMessage(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        out.println("from:P" + user);
        while(true) {
            out.println("jelou!! sigo aqui :-D");
            //listenServer();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
