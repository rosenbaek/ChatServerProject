package server;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.Socket;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    public static Thread thread;
    Scanner scannerMikkel;
    Scanner scannerChristian;
    Scanner scannerMathias;
    Scanner scannerLars;
    Socket socketMikkel;
    Socket socketChristian;
    Socket socketMathias;
    Socket socketLars;
    PrintWriter mikkelOutputStream;
    PrintWriter christianOutputStream;
    PrintWriter mathiasOutputStream;
    PrintWriter larsOutputStream;

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
        socketMikkel = new Socket("localhost",8088);
        scannerMikkel = new Scanner(socketMikkel.getInputStream());
        mikkelOutputStream = new PrintWriter(socketMikkel.getOutputStream(),true);

        socketChristian = new Socket("localhost", 8088);
        scannerChristian = new Scanner(socketChristian.getInputStream());
        christianOutputStream = new PrintWriter(socketChristian.getOutputStream(),true);

        socketMathias = new Socket("localhost", 8088);
        scannerMathias = new Scanner(socketMathias.getInputStream());
        mathiasOutputStream = new PrintWriter(socketMathias.getOutputStream(),true);

        socketLars = new Socket("localhost", 8088);
        scannerLars = new Scanner(socketLars.getInputStream());
        larsOutputStream = new PrintWriter(socketLars.getOutputStream(),true);
    }


    @AfterEach
    public void tearDown() throws IOException {
        socketMikkel.close();
        socketChristian.close();
        socketMathias.close();
        socketLars.close();
    }


    @Test
    public void testConnectSucceed() throws IOException {
        mikkelOutputStream.println("CONNECT#Mikkel");
        String actual = scannerMikkel.nextLine();
        assertEquals("ONLINE#Mikkel",actual);

        Socket socket2 = new Socket("localhost", 8088);
        Scanner scanner2 = new Scanner(socket2.getInputStream());
        PrintWriter pw2 = new PrintWriter(socket2.getOutputStream(),true);

        pw2.println("CONNECT#Christian");
        assertEquals("ONLINE#Mikkel,Christian", scannerMikkel.nextLine());
        assertNotEquals("ONLINE#Christian", scanner2.nextLine());
    }

    @Test
    public void testConnectFailure() throws IOException {
        mikkelOutputStream.println("CONNECT#NoUser");
        String actual = scannerMikkel.nextLine();
        assertEquals("CLOSE#2",actual);
    }

    @Test
    public void testFirstLoginInputFailure() throws IOException {
        mikkelOutputStream.println("connect#NoUser");
        String actual = scannerMikkel.nextLine();
        assertEquals("CLOSE#1",actual);
    }

    @Test
    public void testSendToALLSucceed() throws IOException {
        mikkelOutputStream.println("CONNECT#Mikkel");
        System.out.println(scannerMikkel.nextLine()); //ONLINE#Mikkel

        christianOutputStream.println("CONNECT#Christian");

        System.out.println(scannerMikkel.nextLine()); //ONLINE#Mikkel,Christian
        christianOutputStream.println("SEND#*#message");
        assertEquals("MESSAGE#Christian#message", scannerMikkel.nextLine());
    }

    @Test
    public void testSendToUserSucceed() throws IOException {
        mikkelOutputStream.println("CONNECT#Mikkel");
        System.out.println(scannerMikkel.nextLine()); //ONLINE#Mikkel - Disregard for Mikkel
        mathiasOutputStream.println("CONNECT#Mathias");
        System.out.println(scannerMikkel.nextLine()); //ONLINE#Mikkel,Mathias - Disregard for Mikkel
        System.out.println(scannerMathias.nextLine()); //ONLINE#Mikkel,Mathias - Disregard for Mathias

        christianOutputStream.println("CONNECT#Christian");
        System.out.println(scannerMikkel.nextLine()); //ONLINE#Mikkel,Mathias,Christian - Disregard for Mikkel
        System.out.println(scannerMathias.nextLine()); //ONLINE#Mikkel,Mathias,Christian - Disregard for Mathias

        christianOutputStream.println("SEND#Mikkel#message");
        assertEquals("MESSAGE#Christian#message", scannerMikkel.nextLine());
        larsOutputStream.println("CONNECT#Lars");
        assertEquals("ONLINE#Mikkel,Mathias,Christian,Lars", scannerMathias.nextLine());
    }

    @Test
    public void testSendToNonExistingUser() throws IOException {
        mikkelOutputStream.println("CONNECT#Mikkel");
        System.out.println(scannerMikkel.nextLine()); //ONLINE#Mikkel - Disregard for Mikkel
        mikkelOutputStream.println("SEND#Peter#hej med dig");
        assertEquals("CLOSE#2", scannerMikkel.nextLine());
    }

    @Test
    public void testIllegalInput() throws IOException {
        mikkelOutputStream.println("CONNECT#Mikkel");
        System.out.println(scannerMikkel.nextLine()); //ONLINE#Mikkel
        mikkelOutputStream.println("fejl");
        assertEquals("CLOSE#1", scannerMikkel.nextLine()); //We have not created a client, so NoSuchElement expected
    }

    @Test
    public void testClose0 (){
        mikkelOutputStream.println("CONNECT#Mikkel");
        System.out.println(scannerMikkel.nextLine()); //Disregard online msg
        mikkelOutputStream.println("CLOSE#");
        assertEquals("CLOSE#0",scannerMikkel.nextLine());
    }

}
