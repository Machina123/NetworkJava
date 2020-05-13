package net.machina.networkjava.class6;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriterThread extends Thread {

    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriterThread(Socket socket, ChatClient client){
        this.socket = socket;
        this.client = client;

        try {
            OutputStream os = socket.getOutputStream();
            writer = new PrintWriter(os, true);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        while(username.isEmpty()) {
            System.out.print("Enter your username: ");
            username = scanner.nextLine();
        }
        client.setUserName(username);
        writer.println(username);

        String text;
        do {
            text = scanner.nextLine();
            writer.println(text);
        } while(!text.equalsIgnoreCase("exit"));

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
