package sample;

import javafx.application.Application;

import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.*;

public class UIThread extends Thread{
    public Socket socket = null;

    public UIThread(Socket socket){
        super();
        this.socket = socket;
    }

    public void run(){
        // launches from Main.java
        Application.launch(Main.class);
    }
}
