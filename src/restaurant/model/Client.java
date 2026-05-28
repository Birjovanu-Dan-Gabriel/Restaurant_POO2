package restaurant.model;


public class Client {
    private int id;
    private String nume;
    private String telefon;

    public Client(int id, String nume, String telefon) {
        this.id = id;
        this.nume = nume;
        this.telefon = telefon;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getTelefon() { return telefon; }

    public void setNume(String nume) { this.nume = nume; }
    public void setId(int id) {
        this.id = id;
    }
}