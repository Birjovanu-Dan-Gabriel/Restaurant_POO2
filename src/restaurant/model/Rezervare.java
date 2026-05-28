package restaurant.model;

import java.time.LocalDateTime;

public class Rezervare {
    private int id;
    private LocalDateTime dataOra;
    private Client client;
    private int numarPersoane;
    private Masa masa;

    public Rezervare(int id, LocalDateTime dataOra, Client client, int numarPersoane, Masa masa) {
        this.id = id;
        this.dataOra = dataOra;
        this.client = client;
        this.numarPersoane = numarPersoane;
        this.masa = masa;
    }


    public int getId() { return id; }
    public LocalDateTime getDataOra() { return dataOra; }
    public Client getClient() { return client; }
    public int getNumarPersoane() { return numarPersoane; }
    public Masa getMasa() { return masa; }
}