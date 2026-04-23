package restaurant.model;


//TODO cand fac conturile calumea sa imi incrementeze mesele servite pt angajatul bun
public class Chelner extends Angajat { //
    private int meseServite;

    public Chelner(int id, String nume, double salariu,int varsta, String gen, String dataAngajare) {
        super(id, nume, salariu, varsta, gen, dataAngajare);
        this.meseServite = 0;
    }

    public void incrementMese() { this.meseServite++; }
}