package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class FileServerClient extends Frame {
    public Socket socket = null;
    public BufferedReader in = null;
    public static BufferedReader networkIn = null; // read from server
    public static PrintWriter networkOut = null;   // write to server


    public static String serverAddress = "localhost";
    public static int serverPort = 8080;

    // this constructor doesn't actually need a string, its only there as a parameter to differentiate from the empty constructor (empty constructor required for fxml controller)
    public FileServerClient(String a) throws FileNotFoundException {
        try{
            socket = new Socket(serverAddress, serverPort);
        }
        catch(UnknownHostException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        if (socket == null){
            System.err.println("socket is null");
        }

        // assign readers and writers to communicate with server
        try{
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); // read from server
            networkOut = new PrintWriter(socket.getOutputStream(), true);   // write to server
        }
        catch(IOException e){
            e.printStackTrace();
        }

        String message = null;
        // print statements to verify that the connection has been established
        try{
            message = networkIn.readLine(); // "connected to file server"
            System.out.println(message);
            message = networkIn.readLine(); // "200 ready to share files"
            System.out.println(message);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        // launch the GUI in a new thread
        UIThread UI = new UIThread(socket);
        UI.start();

        boolean running = true;
        while(running){
            running = buttonRequest();
        }

        try{
            socket.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    
    /** 
     * @return boolean
     * @throws FileNotFoundException
     */
    public boolean buttonRequest() throws FileNotFoundException {
        String request = null;
        String fileName = null;

        try{
            request = networkIn.readLine();
            System.out.println(request + "  button selected");

            fileName = networkIn.readLine();
            System.out.println("file selected: " + fileName);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return processClientRequest(request, fileName);
    }

    public static String content;

    
    /** 
     * @param request
     * @param fileName
     * @return boolean
     * @throws FileNotFoundException
     */
    public static boolean processClientRequest(String request, String fileName) throws FileNotFoundException {
        if (request.equalsIgnoreCase("download")){
            try {
                content = networkIn.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            File newFile = new File(localSharedFolderPath + "/" + fileName);
            PrintWriter writeToFile = new PrintWriter(newFile);
            writeToFile.println(content);
            writeToFile.close();
            return true;
        }
        else if (request.equalsIgnoreCase("upload")){
            parseFile(fileName);
            return true;
        }
        else {
            return false;
        }
    }

    
    /** 
     * @param fileName
     * @throws FileNotFoundException
     */
    public static void parseFile(String fileName) throws FileNotFoundException {
        File fileToParse = new File(localSharedFolderPath + "/" + fileName);

        String content = "";

        Scanner scanner = new Scanner(fileToParse);
        while (scanner.hasNextLine()){
            content = content + scanner.nextLine();
        }
        networkOut.println(content);
    }

    // fxml handler functions
    @FXML public ListView listLocalFiles;
    @FXML public ListView listServerFiles;

    public FileServerClient(){} // empty constructor for fxml controller

    
    /** 
     * @throws Exception
     */
    public void initialize() throws Exception{
        listLocalFiles.setItems(FileServerClient.localFolderFiles);
        listServerFiles.setItems(FileServerThread.getServerFiles());
    }

    
    /**
     * when download button is pressed, send selected request and filename to the server
     * @param actionEvent
     * @throws Exception
     */
    public void handleDownload(ActionEvent actionEvent) throws Exception {
        String clientRequest = "download";
        String fileName = listServerFiles.getSelectionModel().getSelectedItem().toString();

        if (fileName != null){
            networkOut.println(clientRequest);
            networkOut.println(fileName);

            localFolderFiles.add(fileName);
            initialize();   // reload the listviews
        }
    }

    
    /**
     * when upload button is pressed, send selected request and filename to the server
     * @param actionEvent
     * @throws Exception
     */
    public void handleUpload(ActionEvent actionEvent) throws Exception {
        String clientRequest = "upload";
        String fileName = listLocalFiles.getSelectionModel().getSelectedItem().toString();

        if (fileName != null){
            networkOut.println(clientRequest);
            networkOut.println(fileName);

            FileServerThread.serverFolderFiles.add(fileName);
            initialize();   // reload the listviews
        }
    }

    
    /**
     * print out all names of all files that are in the shared server folder
     * @param actionEvent
     * @throws Exception
     */
    public void handleDir(ActionEvent actionEvent) throws Exception{
        System.out.println("Files in the shared server folder");
        for (int i = 0; i < FileServerThread.serverFolderFiles.size(); i++){
            System.out.println(FileServerThread.serverFolderFiles.get(i));
        }
    }


    public static ObservableList<String> localFolderFiles = FXCollections.observableArrayList();
    public static String localSharedFolderPath = null;

    /** 
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        String computerName = args[0];
        localSharedFolderPath = args[1];

        File localSharedFolder = new File(localSharedFolderPath);
        File[] listOfLocalFiles = localSharedFolder.listFiles();
        for (File file:listOfLocalFiles){
            localFolderFiles.add(file.getName());
        }

        // empty string to not get error for calling FileServerClient constructor
        String a = "";

        FileServerClient client = new FileServerClient(a);
    }
}
