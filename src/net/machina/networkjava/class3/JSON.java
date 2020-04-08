package net.machina.networkjava.class3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class JSON {
    public JSON() {}

    public void getDataFromJson(String jsonStr) {
        JSONObject object = new JSONObject(jsonStr);
        for(String s : object.keySet()) {
            System.out.println(s);
        }

        JSONArray arr = object.optJSONArray("post");
        System.out.println("Długość tablicy: " + arr.length());
        for(int i = 0; i < arr.length(); i++) {
            JSONObject x = arr.optJSONObject(i);
            for(String key : x.keySet()) {
                System.out.println(key + ": " + x.get(key).toString());
            }
            System.out.println("-------------------");
        }
    }

    public JSONArray setDataToJson() {
        JSONObject obj = new JSONObject();
        Scanner scanner = new Scanner(System.in);
        int index = 138094;
        obj.put("Indeks", index);
        System.out.print("Nick? ");
        obj.put("Nick", scanner.nextLine());
        System.out.print("Tresc? ");
        obj.put("Text", scanner.nextLine());

        JSONArray array = new JSONArray();
        array.put(obj);
        return array;
    }

    public JSONObject getPostsByUser(int userid) {
        JSONObject obj = new JSONObject();
        obj.put("Indeks", userid);
        return obj;
    }
}
