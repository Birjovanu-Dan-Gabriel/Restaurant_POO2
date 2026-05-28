package restaurant.model;

import java.time.LocalDateTime;

public class RezervareBuilder {

    private int id = 0; // Default
    private LocalDateTime dataOra;
    private Client client;
    private int numarPersoane = 0; // Default
    private Masa masa;


    public RezervareBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public RezervareBuilder setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
        return this;
    }

    public RezervareBuilder setClient(Client client) {
        this.client = client;
        return this;
    }

    public RezervareBuilder setNumarPersoane(int numarPersoane) {
        this.numarPersoane = numarPersoane;
        return this;
    }

    public RezervareBuilder setMasa(Masa masa) {
        this.masa = masa;
        return this;
    }


    public Rezervare build() {
        // Aici poți adăuga validări! De exemplu:
        if (dataOra == null || client == null || masa == null) {
            throw new IllegalStateException("Nu se poate crea rezervarea: Date incomplete!");
        }
        return new Rezervare(id, dataOra, client, numarPersoane, masa);
    }
}