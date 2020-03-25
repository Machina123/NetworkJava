package net.machina.networkjava.class1;

import java.io.Serializable;

public class Adres implements Serializable {

    String ulica, miasto, kodPoczt;
    int numerBudynku, numerLokalu;

    public Adres(String ulica, String miasto, String kodPoczt, int numerBudynku, int numerLokalu) {
        this.ulica = ulica;
        this.miasto = miasto;
        this.kodPoczt = kodPoczt;
        this.numerBudynku = numerBudynku;
        this.numerLokalu = numerLokalu;
    }

    @Override
    public String toString() {
        return "Adres{" +
                "cls=" + super.toString() +
                "ulica='" + ulica + '\'' +
                ", miasto='" + miasto + '\'' +
                ", kodPoczt='" + kodPoczt + '\'' +
                ", numerBudynku=" + numerBudynku +
                ", numerLokalu=" + numerLokalu +
                '}';
    }
}
