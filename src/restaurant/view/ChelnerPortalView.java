package restaurant.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import restaurant.model.Ingredient;
import restaurant.model.Produs;
import restaurant.service.RestaurantService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChelnerPortalView {


    private int masaSelectataIndex = -1;
    private Button butonMasaSelectat = null;
    private Map<Integer, Button> butoaneMese = new HashMap<>();


    private Label masaSelectataLabel;
    private TableView<Produs> tabelComanda;
    private TilePane grilaMese;
    private Label labelTotal;
    private Button btnFinalizeaza;

    public Scene creazaScena(Stage stage, Scene scenaClient) {

        Label titlu = new Label("Portal Chelner - Gestiune Mese");
        titlu.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogOut = new Button("🚪 Log Out");
        btnLogOut.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLogOut.setOnAction(e -> stage.setScene(scenaClient));

        HBox topBar = new HBox(titlu, spacer, btnLogOut);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);


        masaSelectataLabel = new Label("Nicio masă selectată");
        masaSelectataLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        grilaMese = new TilePane();
        grilaMese.setPadding(new Insets(20));
        grilaMese.setHgap(20);
        grilaMese.setVgap(20);
        grilaMese.setPrefColumns(4);
        grilaMese.setAlignment(Pos.CENTER);

        genereazaMese(12);

        VBox containerGrila = new VBox(grilaMese);
        containerGrila.setAlignment(Pos.CENTER);

        ScrollPane scrollMese = new ScrollPane(containerGrila);
        scrollMese.setFitToWidth(true);
        scrollMese.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #f4f6f7;");
        scrollMese.setPrefHeight(450);

        Button btnSeteazaMese = new Button("⚙️ Setează Număr Mese");
        btnSeteazaMese.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSeteazaMese.setOnAction(e -> seteazaNumarMese());

        HBox panouControlStanga = new HBox(btnSeteazaMese);
        panouControlStanga.setAlignment(Pos.CENTER_LEFT);

        VBox panouStanga = new VBox(20, panouControlStanga, scrollMese);
        panouStanga.setPadding(new Insets(15));
        panouStanga.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: white;");
        panouStanga.setPrefWidth(500);


        tabelComanda = new TableView<>();
        tabelComanda.setPrefHeight(300);

        TableColumn<Produs, String> colNume = new TableColumn<>("Produs");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colNume.setPrefWidth(250);

        TableColumn<Produs, Double> colPret = new TableColumn<>("Preț (RON)");
        colPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colPret.setPrefWidth(100);

        tabelComanda.getColumns().addAll(colNume, colPret);

        VBox butoaneActiune = new VBox(15);
        String btnStyle = "-fx-background-color: white; -fx-border-color: #34495e; -fx-border-radius: 5px; -fx-padding: 10px; -fx-pref-width: 150px; -fx-cursor: hand;";


        Button btnAdaugaProdus = new Button("Adaugă Produs");
        btnAdaugaProdus.setStyle(btnStyle);
        btnAdaugaProdus.setOnAction(e -> {
            if (masaSelectataIndex != -1) {
                deschidePopUpMeniu();
            } else {
                arataAlertaEroare("Selectează o masă mai întâi!");
            }
        });


        Button btnStergeProdus = new Button("Șterge Produs");
        btnStergeProdus.setStyle(btnStyle);
        btnStergeProdus.setOnAction(e -> {
            if (masaSelectataIndex != -1) {
                Produs p = tabelComanda.getSelectionModel().getSelectedItem();
                if (p != null) {
                    returneazaIngrediente(p);
                    ObservableList<Produs> comandaCurenta = RestaurantService.getInstance().getComandaMasa(masaSelectataIndex);
                    comandaCurenta.remove(p);


                    RestaurantService.getInstance().sincronizeazaComandaCuBucataria(masaSelectataIndex, comandaCurenta);

                    actualizeazaAspectMasa(masaSelectataIndex);
                    calculeazaTotal();
                    actualizeazaButonIncasare(masaSelectataIndex);
                } else {
                    arataAlertaEroare("Selectează un produs din tabel pentru a-l șterge!");
                }
            }
        });


        Button btnAnuleaza = new Button("Anulează Comanda");
        btnAnuleaza.setStyle(btnStyle);
        btnAnuleaza.setOnAction(e -> {
            if(masaSelectataIndex != -1) {
                ObservableList<Produs> produsePeMasa = RestaurantService.getInstance().getComandaMasa(masaSelectataIndex);
                if (!produsePeMasa.isEmpty()) {
                    for (Produs p : produsePeMasa) {
                        returneazaIngrediente(p);
                    }
                    produsePeMasa.clear();


                    RestaurantService.getInstance().sincronizeazaComandaCuBucataria(masaSelectataIndex, produsePeMasa);

                    actualizeazaAspectMasa(masaSelectataIndex);
                    calculeazaTotal();
                    actualizeazaButonIncasare(masaSelectataIndex);
                }
            }
        });

        btnFinalizeaza = new Button("Masă Goală");
        btnFinalizeaza.setDisable(true);

        btnFinalizeaza.setOnAction(e -> {
            if(masaSelectataIndex != -1) {
                RestaurantService.getInstance().getComandaMasa(masaSelectataIndex).clear();
                RestaurantService.getInstance().elibereazaMasa(masaSelectataIndex);

                actualizeazaAspectMasa(masaSelectataIndex);
                calculeazaTotal();
                actualizeazaButonIncasare(masaSelectataIndex);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setContentText("Masa a fost încasată și eliberată!");
                info.showAndWait();
            }
        });

        butoaneActiune.getChildren().addAll(btnAdaugaProdus, btnStergeProdus, btnAnuleaza, btnFinalizeaza);


        labelTotal = new Label("Total: 0.00 lei");
        labelTotal.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox panouTotal = new VBox(labelTotal);
        panouTotal.setAlignment(Pos.CENTER);
        panouTotal.setPrefSize(200, 100);
        panouTotal.setStyle("-fx-border-color: #7f8c8d; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-background-color: #f9f9f9;");

        HBox zonaJosDreapta = new HBox(40, butoaneActiune, panouTotal);
        zonaJosDreapta.setAlignment(Pos.BOTTOM_LEFT);
        zonaJosDreapta.setPadding(new Insets(20, 0, 0, 0));

        VBox panouDreapta = new VBox(15, masaSelectataLabel, tabelComanda, zonaJosDreapta);
        panouDreapta.setPadding(new Insets(20));
        panouDreapta.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: white;");
        HBox.setHgrow(panouDreapta, Priority.ALWAYS);

        HBox layoutPrincipal = new HBox(20, panouStanga, panouDreapta);
        layoutPrincipal.setPadding(new Insets(20));

        VBox root = new VBox(10, topBar, layoutPrincipal);
        root.setStyle("-fx-background-color: #ecf0f1;");

        return new Scene(root, 1280, 720);
    }



    private boolean scadeIngrediente(Produs produs) {
        Map<Ingredient, Double> reteta = produs.getReteta();
        if (reteta == null || reteta.isEmpty()) return true;

        for (Map.Entry<Ingredient, Double> entry : reteta.entrySet()) {
            int idIngredientNecesat = entry.getKey().getId();
            double cantitateNecesara = entry.getValue();

            Ingredient ingredientDinStoc = RestaurantService.getInstance().getToateIngredientele().stream()
                    .filter(i -> i.getId() == idIngredientNecesat)
                    .findFirst()
                    .orElse(null);

            if (ingredientDinStoc == null || ingredientDinStoc.getCantitate() < cantitateNecesara) {
                arataAlertaEroare("Ingrediente Insuficiente pentru " + produs.getNume() + "!\nLipsește: " + entry.getKey().getNume());
                return false;
            }
        }

        for (Map.Entry<Ingredient, Double> entry : reteta.entrySet()) {
            int idIngredientNecesat = entry.getKey().getId();
            double cantitateNecesara = entry.getValue();

            Ingredient ingredientDinStoc = RestaurantService.getInstance().getToateIngredientele().stream()
                    .filter(i -> i.getId() == idIngredientNecesat)
                    .findFirst()
                    .orElse(null);

            if (ingredientDinStoc != null) {
                ingredientDinStoc.setCantitate(ingredientDinStoc.getCantitate() - cantitateNecesara);
                RestaurantService.getInstance().actualizeazaIngredient(ingredientDinStoc);
            }
        }
        return true;
    }

    private void returneazaIngrediente(Produs produs) {
        Map<Ingredient, Double> reteta = produs.getReteta();
        if (reteta == null || reteta.isEmpty()) return;

        for (Map.Entry<Ingredient, Double> entry : reteta.entrySet()) {
            int idIngredient = entry.getKey().getId();
            double cantitateReturnata = entry.getValue();

            Ingredient ingredientDinStoc = RestaurantService.getInstance().getToateIngredientele().stream()
                    .filter(i -> i.getId() == idIngredient)
                    .findFirst()
                    .orElse(null);

            if (ingredientDinStoc != null) {
                ingredientDinStoc.setCantitate(ingredientDinStoc.getCantitate() + cantitateReturnata);
                RestaurantService.getInstance().actualizeazaIngredient(ingredientDinStoc);
            }
        }
    }



    private void deschidePopUpMeniu() {
        Stage menuStage = new Stage();
        menuStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        menuStage.setTitle("Selectează Produs");

        ListView<Produs> listaProduse = new ListView<>();
        listaProduse.getItems().addAll(RestaurantService.getInstance().getTotMeniul());

        listaProduse.setCellFactory(param -> new ListCell<Produs>() {
            @Override
            protected void updateItem(Produs item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNume() + " - " + item.getPret() + " RON");
                }
            }
        });

        Button btnAdauga = new Button("Adaugă pe Notă");
        btnAdauga.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAdauga.setOnAction(e -> {
            Produs selectat = listaProduse.getSelectionModel().getSelectedItem();
            if (selectat != null) {
                boolean gatitCuSucces = scadeIngrediente(selectat);

                if (gatitCuSucces) {
                    ObservableList<Produs> comandaCurenta = RestaurantService.getInstance().getComandaMasa(masaSelectataIndex);
                    comandaCurenta.add(selectat);


                    RestaurantService.getInstance().sincronizeazaComandaCuBucataria(masaSelectataIndex, comandaCurenta);

                    actualizeazaAspectMasa(masaSelectataIndex);
                    calculeazaTotal();
                    actualizeazaButonIncasare(masaSelectataIndex);
                    menuStage.close();
                }
            }
        });

        VBox layout = new VBox(10, new Label("Alege un produs din meniu:"), listaProduse, btnAdauga);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 350, 450);
        menuStage.setScene(scene);
        menuStage.showAndWait();
    }

    private void calculeazaTotal() {
        double total = 0.0;
        if (masaSelectataIndex != -1) {
            for (Produs p : RestaurantService.getInstance().getComandaMasa(masaSelectataIndex)) {
                total += p.getPret();
            }
        }
        labelTotal.setText(String.format("Total: %.2f lei", total));
    }


    private void genereazaMese(int numarMese) {
        grilaMese.getChildren().clear();
        butoaneMese.clear();

        for (int i = 1; i <= numarMese; i++) {
            Button btnMasa = new Button("" + i);
            btnMasa.setPrefSize(80, 80);
            butoaneMese.put(i, btnMasa);
            actualizeazaAspectMasa(i);

            final int nrMasa = i;
            btnMasa.setOnAction(e -> selecteazaMasa(nrMasa));
            grilaMese.getChildren().add(btnMasa);
        }
    }

    private void selecteazaMasa(int nrMasa) {
        int masaVeche = masaSelectataIndex;
        masaSelectataIndex = nrMasa;
        butonMasaSelectat = butoaneMese.get(nrMasa);

        if (masaVeche != -1 && masaVeche != nrMasa) {
            actualizeazaAspectMasa(masaVeche);
        }

        masaSelectataLabel.setText("Comandă curentă: Masa " + nrMasa);
        actualizeazaAspectMasa(nrMasa);

        tabelComanda.setItems(RestaurantService.getInstance().getComandaMasa(nrMasa));

        calculeazaTotal();
        actualizeazaButonIncasare(nrMasa);
    }

    private void actualizeazaAspectMasa(int nrMasa) {
        Button btn = butoaneMese.get(nrMasa);
        if (btn == null) return;

        ObservableList<Produs> produsePeMasa = RestaurantService.getInstance().getComandaMasa(nrMasa);

        String stilBaza = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-cursor: hand; ";
        boolean esteSelectata = (nrMasa == masaSelectataIndex);
        String stilMargine;

        if (esteSelectata) {
            stilMargine = "-fx-border-color: #2980b9; -fx-border-width: 4px; -fx-border-radius: 5px;";
        } else if (produsePeMasa == null || produsePeMasa.isEmpty()) {
            stilMargine = "-fx-border-color: #27ae60; -fx-border-width: 2px; -fx-border-radius: 5px;";
        } else {
            stilMargine = "-fx-border-color: #c0392b; -fx-border-width: 2px; -fx-border-radius: 5px;";
        }

        if (produsePeMasa == null || produsePeMasa.isEmpty()) {
            btn.setStyle(stilBaza + "-fx-background-color: #2ecc71; -fx-text-fill: white; " + stilMargine);
        } else {
            btn.setStyle(stilBaza + "-fx-background-color: #e74c3c; -fx-text-fill: white; " + stilMargine);
        }
    }

    private void seteazaNumarMese() {
        TextInputDialog dialog = new TextInputDialog("12");
        dialog.setTitle("Configurare Restaurant");
        dialog.setHeaderText("Schimbare capacitate restaurant");
        dialog.setContentText("Introduceți numărul total de mese:");
        Optional<String> rezultat = dialog.showAndWait();
        rezultat.ifPresent(nr -> {
            try {
                int numarMeseNou = Integer.parseInt(nr.trim());
                if (numarMeseNou > 0 && numarMeseNou <= 100) {
                    masaSelectataIndex = -1;
                    masaSelectataLabel.setText("Nicio masă selectată");
                    tabelComanda.setItems(FXCollections.observableArrayList());
                    labelTotal.setText("Total: 0.00 lei");
                    genereazaMese(numarMeseNou);
                } else {
                    arataAlertaEroare("Numărul de mese trebuie să fie între 1 și 100!");
                }
            } catch (NumberFormatException ex) {
                arataAlertaEroare("Te rog introdu un număr valid!");
            }
        });
    }

    private void actualizeazaButonIncasare(int nrMasa) {
        if (btnFinalizeaza == null) return;

        ObservableList<Produs> produse = RestaurantService.getInstance().getComandaMasa(nrMasa);

        if (produse == null || produse.isEmpty()) {
            btnFinalizeaza.setDisable(true);
            btnFinalizeaza.setStyle("-fx-background-color: #bdc3c7; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px; -fx-pref-width: 150px;");
            btnFinalizeaza.setText("Masă Goală");
        } else {
            boolean mancareGata = RestaurantService.getInstance().esteMancareaGata(nrMasa);

            if (mancareGata) {
                btnFinalizeaza.setDisable(false);
                btnFinalizeaza.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px; -fx-pref-width: 150px; -fx-font-weight: bold; -fx-cursor: hand;");
                btnFinalizeaza.setText("Încasează (Plată)");
            } else {
                btnFinalizeaza.setDisable(true);
                btnFinalizeaza.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px; -fx-pref-width: 150px;");
                btnFinalizeaza.setText("În Preparare...");
            }
        }
    }

    private void arataAlertaEroare(String mesaj) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Eroare");
        alerta.setHeaderText(null);
        alerta.setContentText(mesaj);
        alerta.showAndWait();
    }
}