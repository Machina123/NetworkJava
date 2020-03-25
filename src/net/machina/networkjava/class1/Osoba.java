package net.machina.networkjava.class1;

import java.io.Serializable;

public class Osoba implements Serializable {

    String imie, nazwisko;
    Adres adres;

    public Osoba(String imie, String nazwisko, Adres adres) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.adres = adres;
        System.out.println("Dodano nową osobę");
    }

    @Override
    public String toString() {
        return "Osoba{" +
                "cls=" + super.toString() +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", adres=" + adres.toString() +
                '}';
    }
}
