package restaurant.repository;

import restaurant.model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ClientRepository extends Repository<Client> {
    private static ClientRepository instance;
    private ClientRepository() {}
    public static ClientRepository getInstance() {
        if (instance == null) instance = new ClientRepository();
        return instance;
    }

    @Override
    public void insert(Client c) {
        String sql = "INSERT INTO clienti (nume, telefon) VALUES (?, ?)";


        try (PreparedStatement pstmt = getContext().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, c.getNume());
            pstmt.setString(2, c.getTelefon());
            pstmt.executeUpdate();


            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    c.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea clientului:");
            e.printStackTrace();
        }
    }

    @Override
    public List<Client> getAll() {
        List<Client> clienti = new ArrayList<>();
        String sql = "SELECT * FROM clienti";
        try (Statement stmt = getContext().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clienti.add(new Client(rs.getInt("id"), rs.getString("nume"), rs.getString("telefon")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return clienti;
    }

    @Override public Client getById(int id) { return null; }
    @Override public void update(Client c) {}
    @Override public void delete(int id) {}
}