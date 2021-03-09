package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    Protocol protocol;
    boolean loggedIn = false;
    Socket socket;
    Server server;
    PrintWriter outputStream;
    Scanner inputStream;
    User user;


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
            server.addToMyClients(user.getUsername(),this);
            while (loggedIn) {
                String inputMsg = inputStream.nextLine();
                loggedIn = protocol.handleCommand(inputMsg);
            }
            user.setToOffline();
            server.removeFromMyClients(user.getUsername());
        }
        socket.close(); //Måske ikke helt efter protocol
    }
}
