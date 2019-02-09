package com.example.killerpad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket socket;
    private String ip;
    private int port;
    private BufferedReader in;
    private PrintWriter out;

    public void setHandler(Socket socket, String ip, int port) {
        this.socket = socket;
        this.ip = ip;
        this.port = port;

        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void listenServer() {
        try {
            String data = this.in.readLine();
            processServerMessage(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processServerMessage(String data) {
        // poner lo que hacemos al recibir menssajes del servidor
    }

    @Override
    public void run() {
        while(true) {
            out.println("sigo aqui");
            //listenServer();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
