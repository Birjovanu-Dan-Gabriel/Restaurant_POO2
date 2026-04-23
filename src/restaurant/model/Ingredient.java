package restaurant.model;

public class Ingredient {
    private int id;
    private String nume;
    private double cantitate; // sau stoc_disponibil
    private String um; // Unitatea de măsură (nou adăugată)

    public Ingredient(int id, String nume, double cantitate, String um) {
        this.id = id;
        this.nume = nume;
        this.cantitate = cantitate;
        this.um = um;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public double getCantitate() { return cantitate; }
    public String getUm() { return um; }

    public void setCantitate(double cantitate){
        this.cantitate = cantitate;
    }
}