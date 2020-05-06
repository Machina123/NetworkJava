package net.machina.networkjava.class5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppServer extends Thread {
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private int clientNo;

    public AppServer(Socket socket, int clientNo) {
        clientSocket = socket;
        this.clientNo = clientNo;
        System.out.println("Connection from #" + this.clientNo + " with " + clientSocket.toString());
    }

    @Override
    public void run() {
        try {
            InputStream in = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            OutputStream out = clientSocket.getOutputStream();
            writer = new PrintWriter(out, true);

            writer.println("Opcja? [1] Aktualna data | [2]Losowa wiadomość | [exit] Wyjście");

            String line;
            while((line = reader.readLine()) != null) {
                switch(line) {
                    case "1":
                        sendDateTime();
                        break;
                    case "2":
                        printMessage();
                        break;
                    case "exit":
                        System.out.println("Użytkownik " + clientNo + " kończy połączenie");
                        break;
                    default:
                        writer.println("Nieznane polecenie");
                }
                if(line.equals("exit")) break;
            }
            writer.close();
            out.close();
            reader.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendDateTime() {
        LocalDateTime ldt = LocalDateTime.now();
        writer.println(ldt.toString());
    }

    public void printMessage() {
        writer.println("test xD");
    }
}

