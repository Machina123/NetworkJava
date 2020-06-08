package net.machina.networkjava.class7;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;

public class SpiderThread implements Callable<Boolean> {

    private String address;
    private DBConnection connection;

    public SpiderThread(String address, DBConnection connection) {
        this.address = address;
        this.connection = connection;
    }

    @Override
    public Boolean call() throws Exception {
        PageConnection pageConnection = new PageConnection();
        StringBuilder webBuilder = pageConnection.getWebpage(address);

        if (webBuilder != null) {
            Document doc = Jsoup.parse(webBuilder.toString());
            if (doc.body() != null) {
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
