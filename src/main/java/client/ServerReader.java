package client;

import java.io.InputStream;
import java.util.Scanner;

class ServerReader implements Runnable {

    Scanner scanner;
    ServerReader(InputStream is){
        scanner = new Scanner(is);
    }

    @Override
    public void run() {
        while(Client.keepRunning) {
            try {
                String message = scanner.nextLine();
                System.out.println("message: "+message);
                if (message.equals("CLOSE#1") || message.equals("CLOSE#2")){
                    System.exit(1);
                    //TODO Make it a more clean exit
                }
            }catch (Exception e){
                Client.keepRunning = false;
            }

        }
    }

}
