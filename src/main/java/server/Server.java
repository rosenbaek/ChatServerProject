package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private ServerSocket ss;
    public static HashMap<Integer, User> hardcodedUsers = new HashMap<Integer, User>();
    private ConcurrentHashMap<String,ClientHandler> myClients = new ConcurrentHashMap<>();
    ExecutorService es = Executors.newFixedThreadPool(5);

    static {
        hardcodedUsers.put(1, new User("Mikkel"));
        hardcodedUsers.put(2, new User("Mathias"));
        hardcodedUsers.put(3, new User("Christian"));
    }

    public void startServer(int port) throws IOException {
        ss = new ServerSocket(port);
        System.out.println("Server started, listening on port: "+port);
        System.out.println("Waiting for a client");

        while (true) {
            Socket socket = ss.accept(); //Blocking call
            ClientHandler clienthandler = new ClientHandler(socket,this);
            es.execute(clienthandler);
        }
    }
    public void addToMyClients(String username, ClientHandler clientHandler){
        this.myClients.put(username,clientHandler);
    }
    public void removeFromMyClients(String username){
        this.myClients.remove(username);
    }

    public void sendToUsers(String[] toUsers, String msg){
        //Used to send to all users
        if(toUsers[0].equals("*")){
            myClients.values().forEach(ClientHandler -> ClientHandler.protocol.msgFromUser(msg));
            return;
        }

        //Used to send to specific users
        ClientHandler clientHandler;
        for (String tmp:myClients.keySet()) {
            for (int i = 0; i < toUsers.length; i++) {
                if (toUsers[i].equals(tmp)){
                    clientHandler = myClients.get(tmp);
                    clientHandler.protocol.msgFromUser(msg);
                }
            }
        }
    }
}
