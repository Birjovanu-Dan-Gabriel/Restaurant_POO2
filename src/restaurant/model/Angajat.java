package restaurant.model;

// Clasa abstracta

public abstract class Angajat {
    // un angajat poate avea un singur id si un singur nume
    private final int id;
    private final String nume;
    protected double salariu;
    protected int varsta;
    protected String gen;
    protected String dataAngajare;

    public Angajat (int id, String nume, double salariu,int varsta, String gen, String dataAngajare){
        this.id = id;
        this.nume = nume;
        this.salariu = salariu;
        this.varsta = varsta;
        this.gen = gen;
        this.dataAngajare =dataAngajare;
    }

    // Getteri
    public String getNume(){ return nume; }
    public int getId() { return id; }
    public double getSalariu() { return salariu; }
    public int getVarsta(){ return varsta; }
    public String getGen(){ return  gen; }
    public String getDataAngajare() { return dataAngajare; }
}
