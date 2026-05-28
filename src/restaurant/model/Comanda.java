package restaurant.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Comanda implements Platibil {
    private int id;
    private ObservableList<Produs> produse;
    private String status; // Statusuri posibile: "IN_PREPARARE", "GATA", "FINALIZATA"

    public Comanda() {
        this.produse = FXCollections.observableArrayList();
        this.status = "IN_PREPARARE"; // Orice comandă nouă începe așa
    }

    // Getteri și Setteri
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public ObservableList<Produs> getProduse() { return produse; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Metode specifice pentru a gestiona lista de produse
    public void adaugaProdus(Produs p) {
        this.produse.add(p);
    }
    public void stergeProdus(Produs p) {
        this.produse.remove(p);
    }
    public void golesteComanda() {
        this.produse.clear();
    }


    @Override
    public double calculeazaTotal() {
        double total = 0.0;
        for (Produs p : produse) {
            total += p.getPret();
        }
        return total;
    }
}