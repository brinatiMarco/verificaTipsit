package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biglietteria extends Thread{
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    static ArrayList<String> utenti = new ArrayList<>();
    static List<Integer> disponibilita = new ArrayList<>();    // [0] = Gold, [1] = Pit, [2] = Parterre;

    public Biglietteria(Socket s) throws IOException{
        this.socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        disponibilita.add(10);   //Gold
        disponibilita.add(30);  //Pit
        disponibilita.add(60); //Parterre
    }

    @Override 
    public void run(){

        out.println("WELCOME");    //inizio del dialogo tra server e client
        String errore = "OK";
        String nomeUtente = "";
        String msg;
        String[] login;
        String[] biglietti;
        int nBiglietti;

        do{
            errore = "OK";
            try{
                msg = in.readLine();
                login = msg.split(" ", 2);
                if(!login[0].equals("LOGIN")){
                    errore = "ERR LOGINREQUIRED";
                }
                for (String string : utenti) {
                    if(string.equals(login[1])){
                        errore = "ERR USERINUSE";
                    }
                }

                out.println(errore); 
                nomeUtente = login[1];

                }catch(IOException e){
        
                }
        }while(!errore.equals("OK"));



        utenti.add(nomeUtente);



        do{
            errore = "OK";
            try{
                msg = in.readLine();
                switch (msg) {
                    case "N":

                        out.println(stampaBigliettiDisponibili());
                        
                        break;
                    
                    case "BUY":
                        
                        try{
                        msg = in.readLine();
                        biglietti = msg.split(" ", 2);
                        
                        try{
                            nBiglietti = Integer.parseInt(biglietti[2]);
                        }catch(NumberFormatException ex){
                            errore = "ERR SYNTAX";
                        }

                        switch (biglietti[0]) {
                            case "Gold":
                                
                                break;
                        
                            default:
                            errore = " ERR UNKNOWNTYPE";
                                break;
                        }
                        out.println(errore);

                        }catch(IOException e){

                        }
                        break;
                    
                    case "QUIT":

                        out.println("BYE");
                        socket.close();

                        break;
                    default:
                        break;
                }
            }catch(IOException e){

            }
            
        }while(true);


    }

    public String stampaBigliettiDisponibili(){
        String msg = "AVAIL ";
        msg += "Gold:" + disponibilita.get(0); 
        msg += " Pit:" + disponibilita.get(1);
        msg += " Parterre:" + disponibilita.get(2);
        return msg;
    }
    
}
