package server;

import java.io.PrintWriter;
import java.util.Scanner;

public class Protocol {
    String command;
    String msg;
    int charAt;
    PrintWriter outputStream;
    boolean loggedIn = false;
    User user;


    public Protocol(PrintWriter outputStream) {
        this.outputStream = outputStream;
    }

    //Function split inputMsg
    //if inputmsg.lengt
    public boolean splitInputMessage(String inputMsg) {
        try {
            charAt = inputMsg.indexOf("#");
            command = inputMsg.substring(0,charAt);
            msg = inputMsg.substring(charAt+1);
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName()+": CLOSE#1");//CLOSE#1 = Illegal input recieved
            outputStream.println("CLOSE#1");
            return false;
        }
        return true;
    }

    public boolean login(String inputMsg){

        if (!splitInputMessage(inputMsg)){
            return false;
        }

        if (!command.equals("CONNECT")){
            System.out.println(Thread.currentThread().getName()+": CLOSE#1");//CLOSE#1 = Illegal input recieved
            outputStream.println("CLOSE#1");
            return false;
        }

        for (User tmp: Server.hardcodedUsers.values()) {
            if (tmp.getUsername().equals(msg)){
                user = tmp;
                user.setToOnline();
                outputStream.println(showOnlineUsers());
                return true;
            }
        }
        System.out.println(Thread.currentThread().getName()+": CLOSE#2");//CLOSE#2 = User not found
        outputStream.println("CLOSE#2");
        return false;
    }

    public String showOnlineUsers(){
        StringBuilder result = new StringBuilder();
        result.append("ONLINE#");
        int counter=0;
        for (User tmp:Server.hardcodedUsers.values()) {
            if (tmp.isOnline()==true){
                if (counter==0) {
                    result.append(tmp.getUsername());
                    counter++;
                } else {
                    result.append("," + tmp.getUsername());
                }
            }
        }
        return result.toString();
    }

    public User getUser() {
        return user;
    }

    public boolean handleCommand(String inputMsg) {
        if (!splitInputMessage(inputMsg)){
            return false;
        }

        switch (command) {
            case "CLOSE":
                System.out.println(Thread.currentThread().getName()+": CLOSE#0");
                outputStream.println("CLOSE#0");//CLOSE#0 = normal close
                return false;
            case "SEND":
                // when original input is SEND#PETER#Message it will result in PETER#Message when command is removed
                //Therefor we use the same procedure again here in the SEND case so we get a user and a message.
                int newCharAt = msg.indexOf("#");
                String user = msg.substring(0,newCharAt);
                String message = msg.substring(newCharAt+1);
                System.out.println("User: "+user+". Message: "+message);
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
