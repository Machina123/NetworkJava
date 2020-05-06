package net.machina.networkjava.class5;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AppClient {

    public AppClient(String host,  int port) {
        try {
            Socket socket = new Socket(host, port);

            OutputStream os = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os, true);

            InputStream is = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            Scanner scanner = new Scanner(System.in);
            String line;

            System.out.println(reader.readLine());
            while((line = scanner.nextLine()) != null) {
                writer.println(line);
                System.out.println(reader.readLine());
                if(line.equals("exit")) break;
            }
            System.out.println("Koniec połączenia");

            reader.close();
            is.close();
            writer.close();
            os.close();
            scanner.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
