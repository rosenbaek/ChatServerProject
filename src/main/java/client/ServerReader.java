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
        while(true) {
            String message = scanner.nextLine();
            System.out.println(message);
        }
    }
}
