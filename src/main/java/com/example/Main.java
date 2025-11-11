package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        
        ServerSocket server = new ServerSocket(3000);
        
        do{
            Socket s = server.accept();
            Biglietteria b = new Biglietteria(s);
            b.start();
        }while(true);

    }
}