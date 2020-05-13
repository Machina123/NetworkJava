package net.machina.networkjava.class6;

public class ClientMain {
    public static void main(String[] args) {
        new ChatClient(args[0], Integer.parseInt(args[1])).Execute();
    }
}
