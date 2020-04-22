package net.machina.networkjava.class3;

import net.machina.networkjava.class2.PlikXml;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.beans.PropertyEditorSupport;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
            while ((tmp = buffer.readLine()) != null) {
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
            while ((tmp = reader.readLine()) != null) {
                builder.append(tmp);
            }
            System.out.println(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Elements getAllLinks(String data) {
        Document doc = Jsoup.parse(data);
//        Element head = doc.head();
//        System.out.println(head);
//        Element body = doc.body();
//        System.out.println(body);
        Elements links = doc.select("a[href]");
//        for (Element e : links) {
//            System.out.println(e);
//        }
        return links;
    }

    // Part of class 4
    public void authenticateToWeb(String address) {
        HttpURLConnection connection;

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("login", "138094");
        loginData.put("password", "student");
        try {
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, String> entry : loginData.entrySet()) {
                String k, v;
                k = URLEncoder.encode(entry.getKey(), "UTF-8");
                v = URLEncoder.encode(entry.getValue(), "UTF-8");

                if (sb.length() > 0) sb.append("&");
                sb.append(k).append("=").append(v);
            }
            byte[] data = sb.toString().getBytes();

            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            // Basic auth example
//            String userPass = "138094:student";
//            String basicAuthData = Base64.getEncoder().encodeToString(userPass.getBytes());
//            System.out.println(basicAuthData);
//            connection.setRequestProperty("Authorization", "Basic " + basicAuthData);

            connection.getOutputStream().write(data);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();
                printResponse(stream);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authUsingCookies(String address) {
        HttpURLConnection connection;
        HashMap<String, String> cookieData = new HashMap<>();
        cookieData.put("login", "138094");
        cookieData.put("password", "student");
        try {
            StringBuilder sb = new StringBuilder();
            String result;
            for (Map.Entry<String, String> entry : cookieData.entrySet()) {
                String k, v;
                k = URLEncoder.encode(entry.getKey(), "UTF-8");
                v = URLEncoder.encode(entry.getValue(), "UTF-8");

                if (sb.length() > 0) sb.append(";");
                sb.append(k).append("=").append(v);
            }

            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "*/*");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Cookie", sb.toString());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = connection.getInputStream();
//                printResponse(stream);
                result = getResponse(stream);
                Elements links = getAllLinks(result);
                String urlBase = url.toString().substring(0, url.toString().lastIndexOf("/") + 1);
                System.out.println(urlBase);
                for (Element e : links) {
                    if (!e.attr("href").endsWith(".")) {
                        String f = e.attr("href").replace("\\", "/");
                        downloadFile(urlBase + f, "D:\\javatest");
                    }
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printResponse(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
        stream.close();
    }

    public String getResponse(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        stream.close();

        return sb.toString();
    }

    public void downloadFile(String remotePath, String localPath) {
        HttpURLConnection connection;

        try {
            URL url = new URL(remotePath);
            connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String filename = "";
                String disposition = connection.getHeaderField("Content-Disposition");
                String contentType = connection.getContentType();
                int filesize = connection.getContentLength();

                System.out.println("Content-Disposition: " + disposition + "\nContent-Type: " + contentType + "\nContent-Length: " + filesize);

                if (disposition != null) {
                    int idx = disposition.indexOf("filename=");
                    if (idx != -1) {
                        filename = disposition.substring(idx + 10, disposition.length() - 1);
                    }
                } else {
                    filename = remotePath.substring(remotePath.lastIndexOf("/"));
                }
                String filePath = localPath + File.separator + filename;
                InputStream stream = connection.getInputStream();
                FileOutputStream output = new FileOutputStream(filePath);
                int bytesRead = -1;
                byte[] buffer = new byte[1024];
                while ((bytesRead = stream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.close();
                stream.close();
            } else {
                System.out.println("Błąd: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
