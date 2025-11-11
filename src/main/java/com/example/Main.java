package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 3000; // in caso si voglia cambiare porta basta cambiare questa variabile
        ServerSocket ss = new ServerSocket(port);
        ArrayList<Integer> disponibilita = new ArrayList<>();
        ArrayList<String> users = new ArrayList<>();
        disponibilita.add(10); // Gold
        disponibilita.add(30); // Pit
        disponibilita.add(60); // Parterre
        do{
            Socket s = ss.accept();
            MioThread t = new MioThread(s, disponibilita, users);
            t.start();
        }
        while(true);
    }
}