package sample;

import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread extends Thread{
    public Socket socket = null;


    public ClientThread(){
        super();
//        this.socket = socket;
    }

    String[] argsForFileServerClient = {"myComputer", "C:/Users/Soman/Desktop/sharedFolder"};

    public void run(){
        try {
            FileServerClient.main(argsForFileServerClient);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
