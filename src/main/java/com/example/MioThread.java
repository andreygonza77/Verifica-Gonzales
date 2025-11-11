package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MioThread extends Thread{
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    public static ArrayList<Integer> disponibilita = new ArrayList<>();
    public static ArrayList<String> users = new ArrayList<>();
   
    
    public MioThread(Socket s, ArrayList<Integer> disponibilita, ArrayList<String> users) throws IOException{
        this.s = s;
        in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
        out = new PrintWriter(this.s.getOutputStream(), true);
        this.disponibilita = disponibilita;
        this.users = users;
    }


    @Override
    public void run(){
        try{
            String[] login ={"",""};
            String utente = "";
            out.println("WELCOME");
            while (!login[0].equals("LOGIN")|| login.length!=2 || login[1].isBlank()) {
                try {
                    String linea = in.readLine();           
                    login = linea.split(" ", 2);  
                    utente = login[1];
                } catch (Exception e) {
                    
                }
                if (!login[0].equals("LOGIN")|| login.length!=2 || login[1].isBlank()) {
                out.println("ERR LOGINREQUIRED");
                }
                if(users.size() != 0){
                    boolean check = false;
                    for(int i = 0; i < users.size(); i++){
                        String user = users.get(i);
                        if(utente.equals(user)){
                            check = true;
                        }
                    } 
                    if(check){
                        login[0] = "ERR USERINUSE";
                        login[1] = "";
                        out.println(login[0]);
                        continue;
                    } 
                    else users.add(utente);
                }
                users.add(utente);
            }
            out.println("OK");

            String[] comando = {"", ""};
            while(true){
                String linea = "";
                linea = in.readLine();
                comando = linea.split(",", 2);
                if(comando[0].equals("QUIT")){
                    out.println("BYE");
                    s.close();
                    return;
                }
                
                switch(comando[0]){
                    case "N":
                        out.println("AVAIL Gold:" + disponibilita.get(0) + " Pit:" + disponibilita.get(1) + " Parterre:" + disponibilita.get(2));
                        break;
                    case "BUY":
                        synchronized (disponibilita) {
                            linea = in.readLine();
                            String[] biglietto = linea.split(",", 2);
                            switch (biglietto[0]) {
                                case "Gold":
                                    if(!(Integer.parseInt(biglietto[1]) > disponibilita.get(0))){
                                        int index = 0;
                                        setBiglietti(Integer.parseInt(biglietto[1]), index);
                                        out.println("OK");
                                    }
                                    else out.print("KO");
                                    break;
                                case "Pit":
                                    if(!(Integer.parseInt(biglietto[1]) > disponibilita.get(1))){
                                        int index = 1;
                                        setBiglietti(Integer.parseInt(biglietto[1]), index);
                                        out.println("OK");
                                    }
                                    else out.print("KO");
                                    break;
                                case "Parterre":
                                    if(!(Integer.parseInt(biglietto[1]) > disponibilita.get(2))){
                                        int index = 2;
                                        setBiglietti(Integer.parseInt(biglietto[1]), index);
                                        out.println("OK");
                                    }
                                    else out.print("KO");
                                    break;
                                default:
                                    out.println("ERR UNKNOWNTYPE");
                                    break;
                            }
                        }
                        break;

                    default:
                    out.println("ERR UNKNOWNCMD");
                }
            }
        }
        catch(Exception e){
            System.err.println("Errore");
        }
    }

    private void setBiglietti(int input, int index){
        int n_biglietti = disponibilita.get(index) - input;
        disponibilita.set(index, n_biglietti);
    }
}