package net.machina.networkjava.class3;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class WebPage {

    public WebPage() {
    }

    public String getDataFromWeb(String pageUrl) {
        try {
            URL url = new URL(pageUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
//            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String tmp;
            while((tmp = buffer.readLine()) != null) {
                builder.append(tmp);
//                System.out.println(tmp);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendDataToWeb(String pageUrl, String data) {
        try {
            URL url = new URL(pageUrl);
            URLConnection conn = url.openConnection();
//            conn.setRequestProperty("Accept", "application/json");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(data);
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            String tmp;
            while((tmp = reader.readLine()) != null) {
                builder.append(tmp);
            }
            System.out.println(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllLinks(String data) {
        Document doc = Jsoup.parse(data);
//        Element head = doc.head();
//        System.out.println(head);
        Element body = doc.body();
//        System.out.println(body);
        Elements links = body.select("a[href^=\"http\"]");
        for(Element e : links) {
            System.out.println(e);
        }
    }
}
