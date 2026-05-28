package restaurant.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import restaurant.repository.*;
import restaurant.model.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import restaurant.config.DatabaseConnection;
import restaurant.exception.*;

public class RestaurantService {

    private static RestaurantService instance;
    private RestaurantService() {}
    public static RestaurantService getInstance() {
        if (instance == null) instance = new RestaurantService();
        return instance;
    }

    private List<Angajat> angajati = new ArrayList<>();
    private Set<Produs> meniuSortat = new TreeSet<>();

    //todo astea doua
    private Map<String, Double> stocIngrediente = new HashMap<>();
    private List<Rezervare> rezervari = new ArrayList<>();


    private Map<Integer, Masa> mese = new HashMap<>();



    // === Masa - Chelner - Bucatar ===

    public Masa getMasa(int nrMasa) {
        if (!mese.containsKey(nrMasa)) {
            mese.put(nrMasa, new Masa(nrMasa, 4)); // Capacitate default
        }
        return mese.get(nrMasa);
    }

    public Map<Integer, Comanda> getComenziActiveBucatarie() {
        Map<Integer, Comanda> active = new LinkedHashMap<>();
        for (Masa m : mese.values()) {
            Comanda c = m.getComandaActiva();
            if (!c.getProduse().isEmpty() && c.getStatus().equals("IN_PREPARARE")) {
                active.put(m.getId(), c);
            }
        }
        return active;
    }


    public void notificaBucataria(int nrMasa) {
        Comanda c = getMasa(nrMasa).getComandaActiva();
        if (!c.getProduse().isEmpty()) {
            c.setStatus("IN_PREPARARE");
        }
    }

    public void bucatarAprobaComanda(int nrMasa) {
        getMasa(nrMasa).getComandaActiva().setStatus("GATA");
        AuditService.getInstance().logAction("bucatar_aproba_comanda");
    }

    public boolean esteMancareaGata(int nrMasa) {
        Comanda c = getMasa(nrMasa).getComandaActiva();
        return c.getStatus().equals("GATA") && !c.getProduse().isEmpty();
    }

    public void elibereazaMasa(int nrMasa) {
        getMasa(nrMasa).elibereazaMasa();
        AuditService.getInstance().logAction("masa_eliberata");
    }

    // --- END ---



    // === COMENZI ===

    public void adaugaProdusComanda(int nrMasa, Produs p) throws StocInsuficientException {

        Map<Ingredient, Double> reteta = p.getReteta();
        if (reteta != null && !reteta.isEmpty()) {

            for (Map.Entry<Ingredient, Double> entry : reteta.entrySet()) {
                int idIngredientNecesat = entry.getKey().getId();
                double cantitateNecesara = entry.getValue();

                Ingredient ingredientDinStoc = getToateIngredientele().stream()
                        .filter(i -> i.getId() == idIngredientNecesat)
                        .findFirst()
                        .orElse(null);

                if (ingredientDinStoc == null || ingredientDinStoc.getCantitate() < cantitateNecesara) {
                    throw new StocInsuficientException("Stoc insuficient pentru produsul '" + p.getNume() + "'. Lipsește: " + entry.getKey().getNume());
                }
            }

            for (Map.Entry<Ingredient, Double> entry : reteta.entrySet()) {
                int idIngredientNecesat = entry.getKey().getId();

                Ingredient ingredientDinStoc = getToateIngredientele().stream()
                        .filter(i -> i.getId() == idIngredientNecesat)
                        .findFirst()
                        .orElse(null);

                if (ingredientDinStoc != null) {
                    ingredientDinStoc.setCantitate(ingredientDinStoc.getCantitate() - entry.getValue());
                    actualizeazaIngredient(ingredientDinStoc);
                }
                AuditService.getInstance().logAction("produs_adaugat_comanda");
                AuditService.getInstance().logAction("ingrediente_actualizate");
            }
        }
    }

    public void stergeProdusComanda(int nrMasa, Produs p) {
        getMasa(nrMasa).getComandaActiva().stergeProdus(p);
        AuditService.getInstance().logAction("produs_sters_comanda");
    }

    public void stergeComanda(int nrMasa) {
        getMasa(nrMasa).getComandaActiva().golesteComanda();
        AuditService.getInstance().logAction("comanda_stearsa");
    }


    // --- END ---



    // === ANGAJATI ===

    public void adaugaAngajat(Angajat a) {
        angajati.add(a);
        AngajatRepository.getInstance().insert(a);
        AuditService.getInstance().logAction("adauga_angajat");
        System.out.println("Angajat adăugat: " + a.getNume());
    }

    public List<Angajat> getTotiAngajatii(){
        return AngajatRepository.getInstance().getAll();
    }

    public void actualizeazaAngajat(Angajat a) {
        AngajatRepository.getInstance().update(a);
        AuditService.getInstance().logAction("modifica_angajat");
    }

    public void eliminaAngajat(Angajat a){
        angajati.remove(a);
        AngajatRepository.getInstance().delete(a.getId());
        AuditService.getInstance().logAction("stergere_angajat");
    }

    // --- END ---



    // === INGREDIENTE ===

    public List<Ingredient> getToateIngredientele() {
        return IngredientRepository.getInstance().getAll();
    }

    public void adaugaIngredient(Ingredient i) {
        IngredientRepository.getInstance().insert(i);
        AuditService.getInstance().logAction("adauga_ingredient");
    }

    public void actualizeazaStocIngredient(Ingredient i, double cantitateNoua) {
        i.setCantitate(cantitateNoua);
        IngredientRepository.getInstance().update(i);
        AuditService.getInstance().logAction("modificare_stoc");
    }

    public void eliminaIngredientDinDB(Ingredient i) {
        IngredientRepository.getInstance().delete(i.getId());
        AuditService.getInstance().logAction("sterge_ingredient");
    }

    // --- END ---



    // === PRODUSE ===

    public List<Produs> getTotMeniul() {
        List<Produs> produseDinDB = ProdusRepository.getInstance().getAll();
        meniuSortat.clear();
        meniuSortat.addAll(produseDinDB);
        return new ArrayList<>(meniuSortat);
    }

    public void adaugaProdusInMeniu(Produs p) {
        ProdusRepository.getInstance().insert(p);
        meniuSortat.add(p);
        AuditService.getInstance().logAction("adauga_produs_reteta");
    }

    public void eliminaProdusDinMeniu(Produs p) {
        ProdusRepository.getInstance().delete(p.getId());
        meniuSortat.remove(p);
        AuditService.getInstance().logAction("sterge_produs");
    }

    public void actualizeazaIngredient(Ingredient i) {
        IngredientRepository.getInstance().update(i);
    }

    // --- END ---

    public String getRolAngajatDupaUsername(String username) {
        String query = "SELECT tip_angajat FROM angajati WHERE REPLACE(nume, ' ', '') = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            String usernameCurat = username.replace(" ", "");
            statement.setString(1, usernameCurat);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("tip_angajat");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean areRezervareInCurand(int nrMasa) {

        String query = "SELECT COUNT(*) AS total FROM rezervari WHERE masa_id = ? AND data_ora BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 30 MINUTE)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, nrMasa);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Eroare la verificarea rezervărilor pentru masa " + nrMasa + ": " + e.getMessage());
        }

        return false;
    }
}