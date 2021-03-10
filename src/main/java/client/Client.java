package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    PrintWriter outputStream;
    Scanner inputStream;
    static boolean keepRunning = true;


    public void connect(String ip, int port) throws IOException, InterruptedException {
        socket = new Socket(ip,port);
        outputStream = new PrintWriter(socket.getOutputStream(),true);
        inputStream = new Scanner(socket.getInputStream());


        ServerReader sr = new ServerReader(socket.getInputStream());
        Thread t = new Thread(sr);
        t.start();

        Scanner keyboard = new Scanner(System.in);

        while(keepRunning){
            String msgToSend = keyboard.nextLine();
            outputStream.println(msgToSend);
            if (msgToSend.equals("CLOSE#")) {
                keepRunning = false;
            }
        }
        socket.close();

    }
}
