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
            System.out.println("User logged in: "+user.getUsername());
            server.addToMyClients(user.getUsername(),this);
            //next line is used to send ONLINE# message everytime someone logs in
            server.sendToUsers(toUsers,protocol.showOnlineUsers());
            while (loggedIn) {
                try {
                    String inputMsg = inputStream.nextLine();
                    loggedIn = protocol.handleCommand(inputMsg);
                }catch (Exception e){
                    System.out.println("Client terminated the session outside protocol: "+Thread.currentThread().getName());
                    loggedIn = false;
                }
            }

            user.setToOffline();
            server.removeFromMyClients(user.getUsername());
            System.out.println("User logged off: "+user.getUsername());
            //Used to send ONLINE# message everytime someone logs off
            server.sendToUsers(toUsers,protocol.showOnlineUsers());
        }
        socket.close(); //Måske ikke helt efter protocol
    }
}
