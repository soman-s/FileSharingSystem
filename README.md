<!-- # Note
Please refer to the message I sent you on Canvas reagrding the readme.md file (subject is "Note about readme file for Assignment 2") -->

# CSCI2020 File Sharing System Project Information
This project allows a user to transfer files from a local folder on their computer to a folder on a server, and vice versa.
The program uses concepts such as servers, sockets, and multithreading.

![screenshot](https://user-images.githubusercontent.com/71238125/114112937-7550ef00-98ab-11eb-9899-80d87fe7e77a.PNG)

# Improvements
Added css styling (padding and colours) to enhance the aesthetic of the client. Added a directory button in the client that prints out all files on the server folder to the terminal.
Added a clientThread class that starts a seperate thread for each GUI client.

# How to Run
This program is configured to run in intellij

Setup javafx:
Go to File, select project Structure, select Libraries, select new Project Library, and add the path to the javafx lib folder.

Setup Configurations and Command Line Arguments:
The FileServerClient.java file requires 2 command line arguments. The first is the name of the computer, and the second is the path to the local folder on your computer
from which you will transfer files. To setup the command line arguments, go to Run, select Edit Configurations, select FileServerClient, then add the command line arguments. 
The path of the server folder is: src/sample/serverFolder

Once all the above is setup, the program can be started. First run the FileServer class to start the server, then run the FileServerClient class to start the GUI client.

Note: The uplaod (transfer file from local to server) button will only work when a local file is selected, and the download (transfer file from server to local) button
will only work when a server folder is selected.

# Other Resources
The chatserver code example shown my Professor Mariana in module 7 was used as a reference to setup the servers and sockets. 




