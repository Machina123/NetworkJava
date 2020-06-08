package net.machina.networkjava.class7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PageConnection {
    public PageConnection() { }

    public StringBuilder getWebpage(String address) {
        try {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Content-Type", "text/html");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
