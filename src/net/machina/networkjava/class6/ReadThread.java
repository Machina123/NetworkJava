package net.machina.networkjava.class6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class ReadThread extends Thread {

    public static final String C_CYAN = "\033[36m";
    public static final String C_DEF = "\033[0m";
    private BufferedReader buffer;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket cSocket, ChatClient cClient){
        this.socket = cSocket;
        this.client = cClient;

        try {
            InputStream is = socket.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(!socket.isClosed()) {
            try {
                String response = buffer.readLine();
                System.out.println(response);
                if(client.GetUserName() != null) {
                    System.out.print(C_CYAN + "[" + client.GetUserName() +  "] :" + C_DEF);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Connection ended");
    }
}
