package restaurant.model;

public class Masa {
    private int id;
    private int capacitate;
    private Comanda comandaActiva;

    public Masa(int id, int capacitate) {
        this.id = id;
        this.capacitate = capacitate;
        this.comandaActiva = new Comanda();
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCapacitate() { return capacitate; }
    public void setCapacitate(int capacitate) { this.capacitate = capacitate; }

    public Comanda getComandaActiva() { return comandaActiva; }
    public void setComandaActiva(Comanda comandaActiva) { this.comandaActiva = comandaActiva; }

    public void elibereazaMasa() {
        this.comandaActiva = new Comanda();
    }
}