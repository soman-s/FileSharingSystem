package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.net.*;
import java.util.*;

public class FileServer {
    public static Socket clientSocket = null;
    public ServerSocket serverSocket = null;
    public FileServerThread[] threads = null;
    public int numClients = 0;

    public  static int serverPort = 8080;
    public static int maxClients = 25;

    // constructor
    public FileServer(){
        try{
            // assign socket
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Listening to port: " + serverPort);
            threads = new FileServerThread[maxClients];

            while(true){
                // connect client socket to server socket
                clientSocket = serverSocket.accept();
                System.out.println("File Sharing Client #" + (numClients + 1) + " connected");

                // create new thread for each client
                threads[numClients] = new FileServerThread(clientSocket);
                threads[numClients].start();
                numClients++;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args){
        FileServer fileServer = new FileServer();
    }
}
