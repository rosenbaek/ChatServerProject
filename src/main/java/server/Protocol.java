package server;

import java.io.PrintWriter;
import java.util.Scanner;

public class Protocol {
    PrintWriter outputStream;


    public Protocol(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }

    public boolean handleCommand(String inputMsg) {
        String command;
        String msg;
        int charAt;

        try {
            charAt = inputMsg.indexOf("#");
            command = inputMsg.substring(0,charAt);
            msg = inputMsg.substring(charAt+1);
        } catch (IllegalArgumentException e) {
            System.out.println(Thread.currentThread().getName()+": CLOSE#1");//CLOSE#1 = Illegal input recieved
            outputStream.println("CLOSE#1");
            return false;
        }

        switch (command) {
            case "CLOSE":
                System.out.println(Thread.currentThread().getName()+": CLOSE#0");
                outputStream.println("CLOSE#0");//CLOSE#0 = normal close
                return false;
            case "CONNECT":
                System.out.println("Connect command");
                break;
            case "SEND":
                System.out.println("Send Command");
                //CLOSE#2 = User not found
                break;
            default:
                System.out.println(Thread.currentThread().getName()+": CLOSE#1");
                outputStream.println("CLOSE#1"); //CLOSE#1 = Illegal input recieved
                return false;
        }
        return true;
    }
}
