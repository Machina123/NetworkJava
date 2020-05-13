package net.machina.networkjava.class6;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class UserThread extends Thread {

    public static final String C_YELLOW = "\033[33m";
    public static final String C_DEF = "\033[0m";
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server){
        this.server = server;
        this.socket = socket;

    }

    public void run(){
        try {
            InputStream is = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            OutputStream os = socket.getOutputStream();
            writer = new PrintWriter(os, true);

            String username, clientMessage, serverMessage;
            printUsers();

            username = reader.readLine();
            sendMessage("Hello " + username);
            server.addUserName(username);

            serverMessage = C_YELLOW + username + C_DEF + " has joined the chat";
            server.broadcastUser(serverMessage, this);

            do {
                clientMessage = reader.readLine();
                serverMessage = C_YELLOW + "[" + username + "] " + C_DEF + clientMessage;
                server.broadcastUser(serverMessage, this);
            } while(!clientMessage.equalsIgnoreCase("exit"));

            serverMessage = C_YELLOW + username + C_DEF + " has left the chat";
            server.broadcastUser(serverMessage, this);
            server.removeUser(username, this);
            os.close();
            is.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void printUsers(){
        if(server.hasUser()) {
            sendMessage("Online users: " + server.getUserNames());
        } else {
            sendMessage("No users online");
        }
    }

    public void sendMessage(String message){
        writer.println(message);
    }
}
