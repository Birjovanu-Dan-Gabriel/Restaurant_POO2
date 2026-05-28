package restaurant.model;


public class Chelner extends Angajat { //
    private int meseServite;

    public Chelner(int id, String nume, double salariu,int varsta, String gen, String dataAngajare) {
        super(id, nume, salariu, varsta, gen, dataAngajare);
        this.meseServite = 0;
    }

}