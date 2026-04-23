package restaurant.model;
import java.util.ArrayList;
import java.util.List;

//TODO momentan am folosit 'Map<Integer, ObservableList<Produs>>' ca placeholder pt asta
//o voi implementa odata cu Rezervarile
public class Comanda implements Platibil {
    private int id;
    private List<Produs> produse = new ArrayList<>(); // Colecția 1: List
    private double totalFinal;

    public Comanda(int id) { this.id = id; }

    public void adaugaProdus(Produs p) { produse.add(p); }

    @Override
    public double calculeazaTotal() {
        return produse.stream().mapToDouble(Produs::getPret).sum();
    }

    @Override
    public void aplicaDiscount(int procent) {
        this.totalFinal = calculeazaTotal() * (1 - (procent / 100.0));
    }
}