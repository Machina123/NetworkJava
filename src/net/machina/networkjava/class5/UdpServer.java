package net.machina.networkjava.class5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class UdpServer {
    public UdpServer() {
        try {
            DatagramSocket socket = new DatagramSocket(5056);
            Random random = new Random();
            int count = 0;
            System.out.println("Server online");
            while(count < 10) {
                DatagramPacket req = new DatagramPacket(new byte[1], 1);
                socket.receive(req);
                String message = String.valueOf(random.nextInt());
                byte[] buffer = message.getBytes();
                InetAddress clientAddr = req.getAddress();
                int clientPort = req.getPort();
                System.out.println("Sending data to " + clientAddr + ":" + clientPort);
                DatagramPacket resp = new DatagramPacket(buffer, buffer.length, clientAddr, clientPort);
                socket.send(resp);
                ++count;
            }
            socket.close();
            System.out.println("Server gracefully closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
