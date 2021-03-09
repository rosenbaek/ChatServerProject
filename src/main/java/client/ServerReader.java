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
        boolean run = true;
        while(run) {
            try {
                String message = scanner.nextLine();
                System.out.println(message);
            }catch (Exception e){
                run = false;
            }
        }
    }
}
