package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class ChatServerMain {


    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) {
        String ip ="localhost";
        int port = 8088;
        String logFile = "log.txt";  //Do we need this

        try {
            if (args.length == 3) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
                logFile = args[2];
                new Server().startServer(port);
            }
            else {
                //Remove before flight
                try {
                    new Server().startServer(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //throw new IllegalArgumentException("Server not provided with the right arguments");
            }
        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server could not start.");
        }

    }
}
