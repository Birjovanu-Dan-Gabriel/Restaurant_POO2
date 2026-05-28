package restaurant.repository;

import restaurant.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AngajatRepository extends Repository<Angajat> {
    private static AngajatRepository instance;
    private AngajatRepository() {}
    public static AngajatRepository getInstance() {
        if (instance == null) instance = new AngajatRepository();
        return instance;
    }


    @Override
    public void insert(Angajat a) {
        String sql = "INSERT INTO angajati (nume, salariu, varsta, gen, data_angajare, tip_angajat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setString(1, a.getNume());
            pstmt.setDouble(2, a.getSalariu());
            pstmt.setInt(3, a.getVarsta());
            pstmt.setString(4, a.getGen());
            pstmt.setString(5, a.getDataAngajare());
            pstmt.setString(6, (a instanceof Chelner) ? "CHELNER" : (a instanceof Bucatar) ? "BUCATAR" : "MANAGER");
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }


    @Override
    public List<Angajat> getAll() {
        List<Angajat> angajati = new ArrayList<>();
        String sql = "SELECT * FROM angajati";
        try (Statement stmt = getContext().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nume = rs.getString("nume");
                double salariu = rs.getDouble("salariu");
                int varsta = rs.getInt("varsta");
                String gen = rs.getString("gen");
                String dataAngajare = rs.getString("data_angajare");
                String tip = rs.getString("tip_angajat");

                angajati.add(AngajatFactory.creazaAngajat(tip, id, nume, salariu, varsta, gen, dataAngajare));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return angajati;
    }



    @Override
    public void update(Angajat a) {
        String sql = "UPDATE angajati SET nume = ?, salariu = ?, varsta = ?, gen = ?, data_angajare = ?, tip_angajat = ? WHERE id = ?";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setString(1, a.getNume());
            pstmt.setDouble(2, a.getSalariu());
            pstmt.setInt(3, a.getVarsta());
            pstmt.setString(4, a.getGen());
            pstmt.setString(5, a.getDataAngajare());
            pstmt.setString(6, (a instanceof Chelner) ? "CHELNER" : (a instanceof Bucatar) ? "BUCATAR" : "MANAGER");
            pstmt.setInt(7, a.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM angajati WHERE id = ?";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public Angajat getById(int id) { return null; }
}