package net.machina.networkjava.class6;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private String hostName;
    private int port;
    private String userName;

    public ChatClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public void Execute() {
        try {
            Socket socket = new Socket(hostName, port);
            System.out.println("Connected to chat");

            new WriterThread(socket, this).start();
            new ReadThread(socket, this).start();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String GetUserName() {
        return this.userName;
    }

}
