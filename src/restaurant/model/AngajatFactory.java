package restaurant.model;

public class AngajatFactory {

    public static Angajat creazaAngajat(String tip, int id, String nume, double salariu, int varsta, String gen, String dataAngajare) {
        if (tip.equalsIgnoreCase("CHELNER")) {
            return new Chelner(id, nume, salariu, varsta, gen, dataAngajare);
        } else if (tip.equalsIgnoreCase("BUCATAR")) {
            return new Bucatar(id, nume, salariu, varsta, gen, dataAngajare, "Sectie Generala");
        } else if (tip.equalsIgnoreCase("MANAGER")) {
            return new Manager(id, nume, salariu, varsta, gen, dataAngajare);
        }

        throw new IllegalArgumentException("Tip de angajat necunoscut: " + tip);
    }
}