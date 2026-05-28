package restaurant.model;

public class Bucatar extends Angajat { //

    private String sectie;

    public Bucatar(int id, String nume, double salariu,int varsta, String gen, String dataAngajare, String sectie) {
        super(id, nume, salariu, varsta, gen, dataAngajare);
        this.sectie = sectie;
    }
}