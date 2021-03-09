package client;

import java.io.IOException;

public class ChatClientMain {
    public static void main(String[] args) {
        int DEFAULT_port = 8088;
        String DEFAULT_SERVER_IP = "localhost";
        int port = DEFAULT_port;
        String ip = DEFAULT_SERVER_IP;
        try {
            if (args.length == 2) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
            }
            new Client().connect(ip, port);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port or ip , using defaults port :");
        } catch (IOException e) {
            System.out.println("Failed when trying to connect to server");
        }
    }
}
