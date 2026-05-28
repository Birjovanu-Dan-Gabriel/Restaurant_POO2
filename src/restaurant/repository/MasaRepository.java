package restaurant.repository;

import restaurant.model.Masa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MasaRepository extends Repository<Masa> {
    private static MasaRepository instance;
    private MasaRepository() {}

    public static MasaRepository getInstance() {
        if (instance == null) instance = new MasaRepository();
        return instance;
    }

    @Override
    public void insert(Masa m) {
        String sql = "INSERT INTO mese (numar, capacitate, status_ocupat) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {

            pstmt.setInt(1, m.getId());
            pstmt.setInt(2, m.getCapacitate());
            boolean ocupata = m.getComandaActiva() != null && !m.getComandaActiva().getProduse().isEmpty();
            pstmt.setBoolean(3, ocupata);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Masa m) {
        String sql = "UPDATE mese SET status_ocupat = ? WHERE numar = ?";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {


            boolean ocupata = m.getComandaActiva() != null && !m.getComandaActiva().getProduse().isEmpty();
            pstmt.setBoolean(1, ocupata);
            pstmt.setInt(2, m.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public List<Masa> getAll() { return new ArrayList<>(); }
    @Override public Masa getById(int id) { return null; }
    @Override public void delete(int id) {}
}