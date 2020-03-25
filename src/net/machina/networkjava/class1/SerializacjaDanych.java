package net.machina.networkjava.class1;

import java.io.*;

public class SerializacjaDanych {

    public SerializacjaDanych() {
    }

    public void generateSerializableFile(String name) {
        Adres adres = new Adres("Kwiatowa", "Kraków", "30-303", 23,5);
        Osoba osoba1 = new Osoba("Jan", "Kowalski", adres);
        Osoba osoba2 = new Osoba("Ewa", "Kowalska", adres);
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(name));
            stream.writeObject(osoba1);
            stream.writeObject(osoba2);
            stream.flush();
            stream.close();
            System.out.println("Zapisano dane w pliku " + name);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void readSerializableFile(String name) {
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(name));
            Osoba osoba3 = (Osoba) stream.readObject();
            Osoba osoba4 = (Osoba) stream.readObject();
            stream.close();

            System.out.println(osoba3.toString());
            System.out.println(osoba4.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
