package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ProtocolTest {


    Protocol protocol;
    PrintWriter outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new PrintWriter(System.out);
        protocol = new Protocol(outputStream);
    }

    @Test
    void splitInputMessage() {
        String wrongInputTest = "illegalArgument";
        String connectTest = "CONNECT#Mikkel";
        String closeTest = "CLOSE#";
        String sendMultipleTest = "SEND#Peter,Hans#Hello";
        String sendAllTest = "SEND#*#Hello";
        String sendOneTest = "SEND#Peter#Hello";
        //Wrong input test
        assertEquals(false,protocol.splitInputMessage(wrongInputTest));

        //Connect Test
        assertEquals(true,protocol.splitInputMessage(connectTest));
        assertEquals(7,protocol.charAt);
        assertEquals("CONNECT",protocol.command);
        assertEquals("Mikkel",protocol.msg);

        //Close test
        assertEquals(true,protocol.splitInputMessage(closeTest));
        assertEquals("CLOSE",protocol.command);
        assertEquals("",protocol.msg);

        //Send Multiple Test
        assertEquals(true,protocol.splitInputMessage(sendMultipleTest));
        assertEquals(4,protocol.charAt);
        assertEquals("SEND",protocol.command);
        assertEquals("Peter,Hans#Hello",protocol.msg);

        //Send All test
        assertEquals(true,protocol.splitInputMessage(sendAllTest));
        assertEquals(4,protocol.charAt);
        assertEquals("SEND",protocol.command);
        assertEquals("*#Hello",protocol.msg);

        //SendOneTest
        assertEquals(true,protocol.splitInputMessage(sendOneTest));
        assertEquals(4,protocol.charAt);
        assertEquals("SEND",protocol.command);
        assertEquals("Peter#Hello",protocol.msg);

    }

    @Test
    void login() {
        //Wrong inputMessage test
        String wrongInputMessageTest = "lalalal";
        assertEquals(false,protocol.login(wrongInputMessageTest));

        //Wrong command test
        String wrongCommandTest = "connect#Mikkel";
        assertEquals(false,protocol.login(wrongCommandTest));

        //User that exists test
        String correctConnectUser = "CONNECT#Mikkel";
        assertEquals(true,protocol.login(correctConnectUser));

        //User that doesn't exist test
        String wrongConnectUser = "CONNECT#Bob";
        assertEquals(false,protocol.login(wrongConnectUser));
    }

    @Test
    void showOnlineUsers() {
        protocol.login("CONNECT#Mikkel");
        protocol.login("CONNECT#Mathias");
        assertEquals("ONLINE#Mikkel,Mathias",protocol.showOnlineUsers());
        protocol.login("CONNECT#Christian");
        assertEquals("ONLINE#Mikkel,Mathias,Christian",protocol.showOnlineUsers());
        assertNotEquals("DETTE ER EN FEJL",protocol.showOnlineUsers());
    }

    @Test
    void getUser() {
        protocol.login("CONNECT#Mikkel");
        assertEquals("Mikkel",protocol.getUser().getUsername());
    }

    @Test
    void handleCommand() {
        //TODO: Create tests when function works!
    }
}