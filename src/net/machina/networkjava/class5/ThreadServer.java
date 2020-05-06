package net.machina.networkjava.class5;

import java.io.IOException;
import java.net.ServerSocket;

public class ThreadServer {
    private static final boolean workServer = true;

    public ThreadServer() {
        System.out.println("Server online");
        start();
    }

    public void start() {
        try{
            int clientNo = 0;
            ServerSocket serverSocket = new ServerSocket(5052);
            while(true) {
                new AppServer(serverSocket.accept(), ++clientNo).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
