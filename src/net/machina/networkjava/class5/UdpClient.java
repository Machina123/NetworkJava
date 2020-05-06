package net.machina.networkjava.class5;

import java.io.IOException;
import java.net.*;

public class UdpClient {

    private String hostname;
    private int port;

    public UdpClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        connect();
    }

    private void connect() {
        try {
            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();

            DatagramPacket packet = new DatagramPacket(new byte[1], 1, address, port);
            socket.send(packet);

            byte[] buffer = new byte[2048];
            DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
            socket.receive(resp);

            String msg = new String(buffer, 0, resp.getLength());
            System.out.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
