package net.machina.networkjava.class3;

public class Main {
    public static void main(String[] args) {
        WebPage page = new WebPage();
//        JSON json = new JSON();
//        String data = page.getDataFromWeb("https://www.up.krakow.pl");
//        page.getAllLinks(data);
//        json.getDataFromJson(data);
//        JSONArray arr = json.setDataToJson();
//        page.sendDataToWeb("http://ux.up.krakow.pl/~pmazurek/java/add.php", arr.toString());
//        page.sendDataToWeb("http://ux.up.krakow.pl/~pmazurek/java/read.php", json.getPostsByUser(138094).toString());
//        page.authenticateToWeb("http://ux.up.krakow.pl/~pmazurek/java/log-in.php");
        page.authUsingCookies("http://ux.up.krakow.pl/~pmazurek/java/cookie.php");
    }
}
