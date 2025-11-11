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
     ArrayList<Integer> disponibilita = new ArrayList<>();    // [0] = Gold, [1] = Pit, [2] = Parterre;

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
        int nBiglietti = 0;

        do{
            errore = "OK";
            try{
                msg = in.readLine();
                login = msg.split(" ", 2);
                if(!login[0].equals("LOGIN")){
                    errore = "ERR LOGINREQUIRED";
                }
                if(login[1].isBlank()){
                    errore = "ERR LOGINREQUIRED";
                }else{
                    nomeUtente = login[1];
                }
                for (String string : utenti) {
                    if(string.equals(login[1])){
                        errore = "ERR USERINUSE";
                    }
                }

                out.println(errore); 
                

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

                            if(biglietti[1].isBlank()){
                                errore = "ERR SYNTAX";
                                break;
                            }
                            
                            try{
                                nBiglietti = Integer.parseInt(biglietti[1]);
                            }catch(NumberFormatException ex){
                                errore = "ERR SYNTAX";
                                break;
                            }

                            if(biglietti[0].equals("Gold") || biglietti[0].equals("Pit") || biglietti[0].equals("Parterre")){

                                if(errore.equals("OK")){

                                    if(!vendiBiglietti(biglietti[0], nBiglietti)){
                                        errore = "KO";
                                    }
                                    
                                }
                            }else{
                                errore = "ERR UNKNOWNTYPE";
                            }
                        }catch(IOException e){

                        }
                        out.println(errore);
                        break;
                    
                    case "QUIT":

                        out.println("BYE");
                        socket.close();

                        break;
                    default:

                        errore = "ERR UNKNOWCMD";
                        out.println(errore);

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

    public boolean vendiBiglietti(String tipo, int numero){
        switch (tipo) {
            case "Gold":
                if(disponibilita.get(0) < numero){
                    //out.println(numero);
                    return false;
                }
                disponibilita.set(0, disponibilita.get(0) - numero);
                //out.println(disponibilita.get(0));
                break;
            
            case "Pit":
                if(disponibilita.get(1) < numero){
                    return false;
                }
                disponibilita.set(1, disponibilita.get(1) - numero);
                break;

            case "Parterre":
                if(disponibilita.get(2) < numero){
                    return false;
                }
                disponibilita.set(2, disponibilita.get(2) - numero);
                break;
    
            default:
                return false;
        }
        return true;
    }
    
}
