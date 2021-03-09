package server;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    public static Thread thread;
    Scanner scanner;
    Socket socket;
    PrintWriter pw;

    @BeforeAll
    public static void startServer(){
        thread = new Thread(() -> {
            String[] parameters = {"8088"};
            ChatServerMain.main(parameters);
        });
        thread.start();
    }


    @AfterAll
    public static void stopServer(){
        thread.stop();
    }


    @BeforeEach
    public void setUp() throws IOException {
        socket = new Socket("localhost",8088);
        scanner = new Scanner(socket.getInputStream());
        pw = new PrintWriter(socket.getOutputStream(),true);
    }


    @AfterEach
    public void tearDown() throws IOException {
        socket.close();
    }


    @Test
    public void test() throws IOException {
        pw.println("CONNECT#Mikkel");
        String actual = scanner.nextLine();
        assertEquals("ONLINE#Mikkel",actual);
    }

}
