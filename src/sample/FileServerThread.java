package sample;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.io.*;
import java.net.*;
import java.util.*;

public class FileServerThread extends Thread{
    public static Socket socket = null;
    public BufferedReader in = null;
    public static PrintWriter out = null;

    public static File[] listOfServerFiles;

    // constructor
    public FileServerThread(Socket socket){
        super();
        this.socket = socket;

        // assign readers and writers to communicate with client
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));    // read from client
            out = new PrintWriter(socket.getOutputStream(), true);    // write to client
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void run(){
        out.println("connected to file server");
        out.println("200 ready to share files");

        boolean endOfSession = false;

        while(!endOfSession){
            try {
                endOfSession = processRequest();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
    public boolean processRequest() throws FileNotFoundException {
        String request = null;
        String fileName = null;

        try{
            request = in.readLine();
            fileName = in.readLine();
        }
        catch (IOException e){
            e.printStackTrace();
            return true;
        }

        if (request == null || fileName == null){
            return true;
        }

        out.println(request);
        out.println(fileName);

        return processRequest(request, fileName);
    }

    String content = null;

    
    /** 
     * @param request
     * @param fileName
     * @return boolean
     * @throws FileNotFoundException
     */
    public boolean processRequest(String request, String fileName) throws FileNotFoundException {
        if (request.equalsIgnoreCase("download")){
            parseFile(fileName);
            return false;
        }
        else if (request.equalsIgnoreCase("upload")){
            try{
                content = in.readLine();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            File newFile = new File("src/sample/serverFolder/" + fileName);
            PrintWriter writeToFile = new PrintWriter(newFile);
            writeToFile.println(content);
            writeToFile.close();
            return false;
        }
        return true;
    }

    
    /** 
     * @param filename
     * @throws FileNotFoundException
     */
    public static void parseFile(String filename) throws FileNotFoundException {
        File fileToParse = new File("src/sample/serverFolder/" + filename);

        String content = "";

        Scanner scanner = new Scanner(fileToParse);
        while (scanner.hasNextLine()){
            content = content + scanner.nextLine();
        }
        out.println(content);
    }


    @FXML public ListView listLocalFiles;
    @FXML public ListView listServerFiles;
    public String clientRequest;
    public String fileName2;

    public boolean buttonPressed;

    public FileServerThread(){}

    
    /** 
     * @throws Exception
     */
    public void initialize() throws Exception{
        listLocalFiles.setItems(FileServerClient.localFolderFiles);
        listServerFiles.setItems(FileServerThread.getServerFiles());
    }

    
    /** 
     * @throws Exception
     */
    public void handleDownload() throws Exception {
        clientRequest = "download";
        fileName2 = listServerFiles.getSelectionModel().getSelectedItem().toString();

        if (fileName2 != null){

            buttonPressed =true;

            FileServerClient.localFolderFiles.add(fileName2);
            initialize();
        }
    }

    
    /** 
     * @return boolean
     */
    public boolean isButtonPressed(){
        return buttonPressed;
    }

    
    /** 
     * @param r
     */
    public void setRequest(String r){
        this.clientRequest = r;
    }

    
    /** 
     * @param f
     */
    public void setFileName(String f){
        this.fileName2 =f;
    }

    
    /** 
     * @return String
     */
    public String getRequest(){
        return this.clientRequest;
    }

    
    /** 
     * @return String
     */
    public String getFileName(){
        return this.fileName2;
    }

    
    /** 
     * @param actionEvent
     */
    public void handleUpload(ActionEvent actionEvent) {
    }

    public static ObservableList<String> serverFolderFiles = FXCollections.observableArrayList();

    public static void serverFiles(){
        File serverSharedFolder = new File("src/sample/serverFolder");
//        System.out.println(serverSharedFolder.getName());

        listOfServerFiles = serverSharedFolder.listFiles();
        for (File file : listOfServerFiles){
            serverFolderFiles.add(file.getName());
        }

        for (int i = 0; i < serverFolderFiles.size(); i++){
            System.out.println(serverFolderFiles.get(i));
        }
    }

    
    /** 
     * @return ObservableList<String>
     */
    public static ObservableList<String> getServerFiles(){
        return serverFolderFiles;
    }

    
    /** 
     * @return boolean
     */
    public boolean processCommand(){
        String input = null;

        try{
            input = in.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
            return true;
        }

        if (input == null){
            return true;
        }

        return processCommand(input);
    }

    
    /** 
     * @param command
     * @return boolean
     */
    public boolean processCommand(String command){
        if (command.equalsIgnoreCase("DIR")){
            System.out.println("DIR option selected from client");
            out.println("loading files in server shared folder");
//            sendListOfFiles();
            return false;
        }
        else{
            return true;
        }
    }
}
