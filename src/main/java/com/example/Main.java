package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        
        ServerSocket server = new ServerSocket(3000);
        List<Integer> disponibilita = new ArrayList<>();    // [0] = Gold, [1] = Pit, [2] = Parterre;
        disponibilita.add(10);   //Gold
        disponibilita.add(30);  //Pit
        disponibilita.add(60); //Parterre
        
        do{
            Socket s = server.accept();
            Biglietteria b = new Biglietteria(s, disponibilita);
            b.start();
        }while(true);

    }
}