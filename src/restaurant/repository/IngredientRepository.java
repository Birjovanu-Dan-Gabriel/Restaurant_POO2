package restaurant.repository;

import restaurant.model.Ingredient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository extends Repository<Ingredient> {
    private static IngredientRepository instance;
    private IngredientRepository() {}
    public static IngredientRepository getInstance() {
        if (instance == null) instance = new IngredientRepository();
        return instance;
    }

    @Override
    public void insert(Ingredient i) {

        String sql = "INSERT INTO ingrediente (nume, stoc_disponibil, um) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setString(1, i.getNume());
            pstmt.setDouble(2, i.getCantitate());
            pstmt.setString(3, i.getUm());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ingredient> getAll() {
        List<Ingredient> ingrediente = new ArrayList<>();
        String sql = "SELECT * FROM ingrediente";
        try (Statement stmt = getContext().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nume = rs.getString("nume");
                double stoc = rs.getDouble("stoc_disponibil");
                String um = rs.getString("um");

                ingrediente.add(new Ingredient(id, nume, stoc, um));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingrediente;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ingrediente WHERE id = ?";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setInt(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getStoc(String nume) {
        String sql = "SELECT stoc_disponibil FROM ingrediente WHERE nume = ?";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setString(1, nume);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("stoc_disponibil");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }




    @Override
    public void update(Ingredient i) {

        String sql = "UPDATE ingrediente SET nume = ?, um = ?, stoc_disponibil = ? WHERE id = ?";

        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setString(1, i.getNume());
            pstmt.setString(2, i.getUm());
            pstmt.setDouble(3, i.getCantitate());
            pstmt.setInt(4, i.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public Ingredient getById(int id) { return null; }
}