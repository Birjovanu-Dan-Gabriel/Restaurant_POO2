package restaurant.model;

public class Bucatar extends Angajat { //
     //TODO regandeste asta
    private String sectie;

    public Bucatar(int id, String nume, double salariu,int varsta, String gen, String dataAngajare, String sectie) {
        super(id, nume, salariu, varsta, gen, dataAngajare);
        this.sectie = sectie;
    }
}