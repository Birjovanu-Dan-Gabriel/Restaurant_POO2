package restaurant.model;

import java.util.HashMap;
import java.util.Map;

public class Produs implements Comparable<Produs> {
    private int id;
    private String nume;
    private double pret;
    private double kcal;

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


    private boolean esteBautura(String numeProdus) {

        if (numeProdus != null && numeProdus.length() >= 2) {
            char penultimulCaracter = numeProdus.charAt(numeProdus.length() - 2);
            return Character.isDigit(penultimulCaracter);
        }
        return false;
    }


    // Ordonam bauturi > mancare, nume, id
    @Override
    public int compareTo(Produs altProdus) {

        boolean thisEsteBautura = esteBautura(this.nume);
        boolean altEsteBautura = esteBautura(altProdus.nume);


        if (thisEsteBautura && !altEsteBautura) {
            return -1;
        }
        if (!thisEsteBautura && altEsteBautura) {
            return 1;
        }

        int comparareNume = this.nume.compareToIgnoreCase(altProdus.nume);
        if (comparareNume != 0) {
            return comparareNume;
        }

        return Integer.compare(this.id, altProdus.id);
    }
}