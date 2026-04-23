package restaurant.repository;

import restaurant.model.Ingredient;
import restaurant.model.Produs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProdusRepository extends Repository<Produs> {
    private static ProdusRepository instance;
    private ProdusRepository() {}
    public static ProdusRepository getInstance() {
        if (instance == null) instance = new ProdusRepository();
        return instance;
    }

    @Override
    public void insert(Produs p) {
        String sqlProdus = "INSERT INTO produse (nume, pret, kcal) VALUES (?, ?, ?)";
        String sqlReteta = "INSERT INTO retete (produs_id, ingredient_id, cantitate) VALUES (?, ?, ?)";

        try {
            Connection conn = getContext();


            PreparedStatement pstmtProdus = conn.prepareStatement(sqlProdus, Statement.RETURN_GENERATED_KEYS);
            pstmtProdus.setString(1, p.getNume());
            pstmtProdus.setDouble(2, p.getPret());
            pstmtProdus.setDouble(3, p.getKcal());
            pstmtProdus.executeUpdate();

            ResultSet rsKeys = pstmtProdus.getGeneratedKeys();
            int produsIdGenerat = -1;
            if (rsKeys.next()) {
                produsIdGenerat = rsKeys.getInt(1);
            }


            if (produsIdGenerat != -1 && p.getReteta() != null) {
                PreparedStatement pstmtReteta = conn.prepareStatement(sqlReteta);


                for (Map.Entry<Ingredient, Double> entry : p.getReteta().entrySet()) {
                    pstmtReteta.setInt(1, produsIdGenerat);       // ID Produs
                    pstmtReteta.setInt(2, entry.getKey().getId()); // ID Ingredient
                    pstmtReteta.setDouble(3, entry.getValue());    // Cantitatea necesară
                    pstmtReteta.executeUpdate();
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Produs> getAll() {
        List<Produs> produse = new ArrayList<>();
        String sqlProduse = "SELECT * FROM produse";


        String sqlReteta = "SELECT r.cantitate, i.id, i.nume, i.stoc_disponibil, i.um " +
                "FROM retete r JOIN ingrediente i ON r.ingredient_id = i.id " +
                "WHERE r.produs_id = ?";

        try {
            Connection conn = getContext();
            Statement stmt = conn.createStatement();
            ResultSet rsProduse = stmt.executeQuery(sqlProduse);

            while (rsProduse.next()) {

                int id = rsProduse.getInt("id");
                String nume = rsProduse.getString("nume");
                double pret = rsProduse.getDouble("pret");
                double kcal = rsProduse.getDouble("kcal");

                Produs produs = new Produs(id, nume, pret, kcal);


                PreparedStatement pstmtReteta = conn.prepareStatement(sqlReteta);
                pstmtReteta.setInt(1, id);
                ResultSet rsReteta = pstmtReteta.executeQuery();

                while (rsReteta.next()) {
                    // Reconstruim ingredientul
                    int ingId = rsReteta.getInt("id");
                    String ingNume = rsReteta.getString("nume");
                    double stoc = rsReteta.getDouble("stoc_disponibil");
                    String um = rsReteta.getString("um");
                    double cantitateFolosita = rsReteta.getDouble("cantitate");

                    Ingredient ingredient = new Ingredient(ingId, ingNume, stoc, um);


                    produs.adaugaIngredientInReteta(ingredient, cantitateFolosita);
                }

                produse.add(produs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produse;
    }

    @Override
    public void delete(int id) {

        String sql = "DELETE FROM produse WHERE id = ?";
        try (PreparedStatement pstmt = getContext().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public Produs getById(int id) { return null; }
    @Override public void update(Produs p) {}
}