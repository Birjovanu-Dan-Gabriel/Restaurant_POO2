package restaurant.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import restaurant.exception.StocInsuficientException;
import restaurant.repository.*;
import restaurant.model.*;
import java.util.*;


public class RestaurantService {


    private static RestaurantService instance;
    private RestaurantService() {}
    public static RestaurantService getInstance() {
        if (instance == null) instance = new RestaurantService();
        return instance;
    }

    private List<Angajat> angajati = new ArrayList<>();
    private Set<Produs> meniuSortat = new TreeSet<>(); // Colectie sortată
    private Map<String, Double> stocIngrediente = new HashMap<>();
    private List<Rezervare> rezervari = new ArrayList<>();
    private Map<Integer, List<Produs>> comenziActive = new LinkedHashMap<>();
    private Map<Integer, List<Produs>> comenziBucatarie = new LinkedHashMap<>();
    private Map<Integer, Boolean> statusMancare = new HashMap<>(); // false = se gătește, true = e gata
    private Map<Integer, ObservableList<Produs>> memorieMeseChelner = new HashMap<>();



    // --- ACTIUNI ANGAJATI ---

    public void adaugaAngajat(Angajat a) {
        angajati.add(a); // Salvare in memorie
        AngajatRepository.getInstance().insert(a); // Salvare in DB
        AuditService.getInstance().logAction("adauga_angajat"); // Scriere in Audit
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
        System.out.println("Angajatul " + a.getNume() + " a fost sters");
    }

    // === END ACTIUNI ANGAJATI ===




    // --- ACTIUNI INGREDIENTE ---


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

    // === END ACTIUNI INGREDIENTE ===



    // --- ACTIUNI PRODUSE ---

    public List<Produs> getTotMeniul() {

        List<Produs> produseDinDB = ProdusRepository.getInstance().getAll();


        meniuSortat.clear();
        meniuSortat.addAll(produseDinDB);

        //returam pt afisare
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


    // === END ACTIUNI PRODUSE ===


    //TODO -- e placeholder ce e aici

    // --- Actiuni comanda ---

//    public void trimiteComandaLaBucatarie(int nrMasa, List<Produs> produse) {
//
//        comenziActive.put(nrMasa, new java.util.ArrayList<>(produse));
//        System.out.println("Comanda pentru masa " + nrMasa + " a fost trimisă la bucătărie.");
//    }

//    public Map<Integer, List<Produs>> getComenziActive() {
//        return comenziActive;
//    }
//
//    public void finalizeazaComandaBucatar(int nrMasa) {
//        comenziActive.remove(nrMasa);
//    }

    public void sincronizeazaComandaCuBucataria(int nrMasa, List<Produs> produse) {
        if (produse.isEmpty()) {
            comenziBucatarie.remove(nrMasa);
            statusMancare.remove(nrMasa);
        } else {

            comenziBucatarie.put(nrMasa, new java.util.ArrayList<>(produse));
            statusMancare.put(nrMasa, false);
        }
    }

    public Map<Integer, List<Produs>> getComenziBucatarie() {
        return comenziBucatarie;
    }


    public void bucatarAprobaComanda(int nrMasa) {
        comenziBucatarie.remove(nrMasa);
        statusMancare.put(nrMasa, true);
    }


    public boolean esteMancareaGata(int nrMasa) {
        return statusMancare.getOrDefault(nrMasa, false);
    }


    public void elibereazaMasa(int nrMasa) {
        statusMancare.remove(nrMasa);
    }



    public ObservableList<Produs> getComandaMasa(int nrMasa) {
        // Dacă masa nu a fost deschisă niciodată, îi creăm o listă goală
        if (!memorieMeseChelner.containsKey(nrMasa)) {
            memorieMeseChelner.put(nrMasa, FXCollections.observableArrayList());
        }
        return memorieMeseChelner.get(nrMasa);
    }

    // === END ACTIUNI COMANDA ===


}