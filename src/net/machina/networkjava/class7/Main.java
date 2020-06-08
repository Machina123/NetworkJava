package net.machina.networkjava.class7;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static DBConnection connection;

    public static void main(String[] args) {
        connection = new DBConnection();
        String address = "https://www.onet.pl/";
//        startSingleThread(address);
        startMultiThread(address, 4);
    }

    public static void startSingleThread(String address) {
        connection.connect();
        connection.dropTables();
        connection.createTables();

        getAllLinks(address);

        LocalTime finish = LocalTime.now().plusMinutes(2);
        while(LocalTime.now().isBefore(finish)) {
            String next = "";
            do {
                next = connection.getLinkToVisit();
            } while (next.isEmpty());
            getAllLinks(next);
        }
        System.out.println(connection.getTempSize() + " in temp, " + connection.getVisitedSize() + " in visited");
        connection.disconnect();
    }

    public static void startMultiThread(String address, int numThreads) {
        connection.connect();
        connection.dropTables();
        connection.createTables();

        getAllLinks(address);

        LocalTime finish = LocalTime.now().plusMinutes(2);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<Boolean>> futures = new ArrayList<>();
        List<String> links;
        while(LocalTime.now().isBefore(finish)) {
            do {
                links = connection.getMultipleLinksToVisit(numThreads);
            } while(links == null);
            
            for(String l : links) {
                SpiderThread thread = new SpiderThread(l, connection);
                futures.add(executorService.submit(thread));
            }
            for(int x = 0; x < numThreads; x++) {
                try {
                    boolean res = futures.get(x).get(5000, TimeUnit.MILLISECONDS);
                    if(res) {
                        connection.addLinkToVisitedTab(links.get(x));
                    }
                    connection.deleteFromTemp(links.get(x));
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            futures.clear();
        }
        System.out.println(connection.getTempSize() + " in temp, " + connection.getVisitedSize() + " in visited");
        connection.disconnect();
    }

    public static boolean getAllLinks(String address) {
        PageConnection pageConnection = new PageConnection();
        StringBuilder webBuilder = pageConnection.getWebpage(address);

        if (webBuilder != null) {
            Document doc = Jsoup.parse(webBuilder.toString());
            if (doc.body() != null) {
                connection.addLinkToVisitedTab(address);
                Elements links = doc.select("a[href]");
                if (links.size() > 0) {
                    System.out.println("Found " + links.size() + " link(s) on " + address);
                    for (Element e : links) {
                        if (e.attr("href").startsWith("http")) {
                            connection.addLinkToTempTab(e.attr("href"));
                        }
                    }
                }
            }
            return true;
        } else {
            System.out.println("Cannot open webpage: " + address);
        }
        return false;
    }
}
