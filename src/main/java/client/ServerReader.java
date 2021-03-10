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
                System.out.println(message);
            }catch (Exception e){
                Client.keepRunning = false;
            }

        }
    }
}
