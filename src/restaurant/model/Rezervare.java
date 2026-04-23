package restaurant.model;
import java.time.LocalDateTime;

//TODO - urmatoare chestie de implementat
public class Rezervare {
    private int id;
    private LocalDateTime dataOra;
    private Client client;
    private int numarPersoane;

    public Rezervare(int id, LocalDateTime dataOra, Client client, int numarPersoane) {
        this.id = id;
        this.dataOra = dataOra;
        this.client = client;
        this.numarPersoane = numarPersoane;
    }

    public int getId() { return id; }
    public LocalDateTime getDataOra() { return dataOra; }
    public Client getClient() { return client; }
}