package restaurant.repository;

import restaurant.model.Rezervare;
import restaurant.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RezervareRepository extends Repository<Rezervare> {
    private static RezervareRepository instance;
    private RezervareRepository() {}

    public static RezervareRepository getInstance() {
        if (instance == null) instance = new RezervareRepository();
        return instance;
    }

    @Override
    public void insert(Rezervare r) {
        // Am adăugat numar_persoane și masa_id în interogare
        String sql = "INSERT INTO rezervari (data_ora, client_id, numar_persoane, masa_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(r.getDataOra()));
            pstmt.setInt(2, r.getClient().getId());
            pstmt.setInt(3, r.getNumarPersoane());
            pstmt.setInt(4, r.getMasa().getId());

            pstmt.executeUpdate();
            System.out.println("Rezervare adăugată cu succes în baza de date!");

        } catch (SQLException e) {
            System.err.println("Eroare la salvarea rezervării:");
            e.printStackTrace();
        }
    }

    @Override public List<Rezervare> getAll() { return new ArrayList<>(); }
    @Override public Rezervare getById(int id) { return null; }
    @Override public void update(Rezervare r) {}
    @Override public void delete(int id) {}
}