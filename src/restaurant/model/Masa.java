package restaurant.model;

public class Masa {
    private int numar;
    private boolean ocupata;

    public Masa(int numar) {
        this.numar = numar;
        this.ocupata = false;
    }

    public int getNumar() { return numar; }

    public boolean isOcupata() {
        return ocupata;
    }
    public void setOcupata(boolean ocupata) { this.ocupata = ocupata; }
}