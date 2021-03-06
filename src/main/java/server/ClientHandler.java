package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    Protocol protocol;
    boolean loggedIn = false;
    Socket socket;
    Server server;
    PrintWriter outputStream;
    Scanner inputStream;
    User user;
    String[] toUsers = {"*"};


    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }


    @Override
    public void run() {
        try {
            handler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handler() throws IOException {
        outputStream = new PrintWriter(socket.getOutputStream(),true);
        inputStream = new Scanner(socket.getInputStream());
        protocol = new Protocol(outputStream,server);


        System.out.println("New client connected. Thread:" + Thread.currentThread().getName() );

        //This line below is to make sure the connect statement is the first executed statement
        String firstInput = inputStream.nextLine();
        loggedIn = protocol.login(firstInput);

        if(loggedIn){
            user = protocol.getUser();
            String userLog = "("+Thread.currentThread().getName()+")"+" User logged in: "+user.getUsername();
            LogFile.writeToLog(userLog, LogFile.Level.INFO);
            System.out.println(userLog);
            server.addToMyClients(user.getUsername(),this);
            //next line is used to send ONLINE# message everytime someone logs in
            server.sendToUsers(toUsers,protocol.showOnlineUsers());
            while (loggedIn) {
                try {
                    String inputMsg = inputStream.nextLine();
                    loggedIn = protocol.handleCommand(inputMsg);
                }catch (Exception e){
                    userLog = "("+Thread.currentThread().getName()+")"+" Client terminated the session outside protocol: "+user.getUsername();
                    System.out.println(userLog);
                    LogFile.writeToLog(userLog, LogFile.Level.ERROR);
                    loggedIn = false;
                }
            }

            user.setToOffline();
            server.removeFromMyClients(user.getUsername());
            userLog = "("+Thread.currentThread().getName()+")"+" User logged off: "+user.getUsername();
            System.out.println(userLog);
            LogFile.writeToLog(userLog, LogFile.Level.INFO);
            //Used to send ONLINE# message everytime someone logs off
            server.sendToUsers(toUsers,protocol.showOnlineUsers());
        }
        socket.close(); //M??ske ikke helt efter protocol
    }
}
