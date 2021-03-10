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
    Server server;
    String userLog;


    public Protocol(PrintWriter outputStream,Server server) {
        this.outputStream = outputStream;
        this.server = server;
    }

    //Function split inputMsg
    //if inputmsg.lengt
    public boolean splitInputMessage(String inputMsg) {
        try {
            charAt = inputMsg.indexOf("#");
            command = inputMsg.substring(0,charAt);
            msg = inputMsg.substring(charAt+1);
        } catch (Exception e) {
            userLog = "("+Thread.currentThread().getName()+")"+" CLOSE#1 "+user.getUsername();
            System.out.println(userLog);//CLOSE#1 = Illegal input recieved
            outputStream.println("CLOSE#1");
            LogFile.writeToLog(userLog, LogFile.Level.ERROR);
            return false;
        }
        return true;
    }

    public boolean login(String inputMsg){

        //Used to split the input msg, and returns true if success.
        if (!splitInputMessage(inputMsg)){
            return false;
        }

        //Check if protocol is followed
        if (!command.equals("CONNECT")){
            userLog = "("+Thread.currentThread().getName()+")"+" CLOSE#1 ";
            System.out.println(userLog);//CLOSE#1 = Illegal input recieved
            outputStream.println("CLOSE#1");
            LogFile.writeToLog(userLog, LogFile.Level.ERROR);
            return false;
        }

        for (User tmp: Server.hardcodedUsers.values()) {
            if (tmp.getUsername().equals(msg) && !tmp.isOnline()){
                user = tmp;
                user.setToOnline();
                return true;
            }
        }
        userLog = "("+Thread.currentThread().getName()+")"+" CLOSE#2 attempted username: "+ msg;
        System.out.println(userLog);//CLOSE#2 = User not found
        outputStream.println("CLOSE#2");
        LogFile.writeToLog(userLog, LogFile.Level.ERROR);
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
                String[] toUsers;
                int newCharAt = msg.indexOf("#");
                String users = msg.substring(0,newCharAt);
                String message = msg.substring(newCharAt+1);

                if(users.equals("*")){

                }
                toUsers = users.split(",");
                String finalMessage = "MESSAGE#"+user.getUsername()+"#"+message;
                server.sendToUsers(toUsers,finalMessage);
                //CLOSE#2 = User not found
                break;
            default:
                userLog = "("+Thread.currentThread().getName()+")"+" CLOSE#1 "+user.getUsername();
                System.out.println(userLog);//CLOSE#1 = Illegal input recieved
                outputStream.println("CLOSE#1");
                LogFile.writeToLog(userLog, LogFile.Level.ERROR);
                return false;
        }
        return true;
    }

    public void msgFromUser(String msg){
        outputStream.println(msg);
    }

}
