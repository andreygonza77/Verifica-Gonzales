package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 3000; // in caso si voglia cambiare porta basta cambiare questa variabile
        ServerSocket ss = new ServerSocket(port);
        do{
            Socket s = ss.accept();
            MioThread t = new MioThread(s);
            t.start();
        }
        while(true);
    }
}