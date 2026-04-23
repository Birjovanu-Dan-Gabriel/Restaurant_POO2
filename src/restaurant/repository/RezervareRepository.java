package restaurant.repository;

import restaurant.model.Rezervare;
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
        String sql = "INSERT INTO rezervari (data_ora, client_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(r.getDataOra()));
            pstmt.setInt(2, r.getClient().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override public List<Rezervare> getAll() { return new ArrayList<>(); }
    @Override public Rezervare getById(int id) { return null; }
    @Override public void update(Rezervare r) {}
    @Override public void delete(int id) {}
}