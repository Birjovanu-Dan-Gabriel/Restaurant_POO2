package restaurant.model;

import java.util.HashMap;
import java.util.Map;

public class Produs implements Comparable<Produs> {
    private int id;
    private String nume;
    private double pret;
    private double kcal;

    // Aici se stocheata "reteta" produsului (ingredientele necesare)
    private Map<Ingredient, Double> reteta;

    public Produs(int id, String nume, double pret, double kcal) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.kcal = kcal;
        this.reteta = new HashMap<>();
    }

    public void adaugaIngredientInReteta(Ingredient ingredient, double cantitate) {
        this.reteta.put(ingredient, cantitate);
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public double getPret() { return pret; }
    public double getKcal() { return kcal; }
    public Map<Ingredient, Double> getReteta() { return reteta; }

    
    @Override
    public int compareTo(Produs altProdus) {
        // Mai intai comparam preturile
        int compararePret = Double.compare(this.pret, altProdus.pret);
        if (compararePret != 0) {
            return compararePret;
        }

        // Daca sunt egale, le ordonam dupa nume
        int comparareNume = this.nume.compareToIgnoreCase(altProdus.nume);
        if (comparareNume != 0) {
            return comparareNume;
        }

        // Daca au si acelasi nume facem diferenta prin id
        return Integer.compare(this.id, altProdus.id);
    }
}