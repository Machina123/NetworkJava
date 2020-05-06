package net.machina.networkjava.class5;

public class ClientMain {
    public static void main(String[] args) {
//        new AppClient(args[0], 5052);
        new UdpClient(args[0], 5056);
    }
}
