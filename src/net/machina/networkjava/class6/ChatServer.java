package net.machina.networkjava.class6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(){

    }

    public void execute(){
        int port = 5050;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server online on port " + port);

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.toString());

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void broadcastUser(String message, UserThread executedUser){
        for(UserThread user : userThreads) {
            if(user != executedUser) {
                user.sendMessage(message);
            }
        }
    }

    public void addUserName(String userName){

    }

    public void removeUser(String userN, UserThread userT){
        boolean removed = userNames.remove(userN);
        if(removed) {
            userThreads.remove(userT);
            System.out.println("Connection ended: " + userN);
        }
    }

    Set<String> getUserNames(){
        return this.userNames;
    }

    boolean hasUser(){
        return !this.userNames.isEmpty();
    }

}
