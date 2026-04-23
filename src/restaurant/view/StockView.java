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
import restaurant.service.RestaurantService;

import java.util.HashMap;
import java.util.Map;

public class StockView {

    public Scene creazaScena(Stage stage, Scene scenaManager) {
        Label titlu = new Label("Gestiune Aprovizionare Stoc");
        titlu.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TableView<Ingredient> table = new TableView<>();
        table.setPrefWidth(450);

        TableColumn<Ingredient, String> colNume = new TableColumn<>("Nume Ingredient");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colNume.setPrefWidth(200);

        TableColumn<Ingredient, String> colUm = new TableColumn<>("U.M.");
        colUm.setCellValueFactory(new PropertyValueFactory<>("um"));
        colUm.setPrefWidth(80);


        TableColumn<Ingredient, Double> colStocCurent = new TableColumn<>("Stoc Curent");
        colStocCurent.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        colStocCurent.setPrefWidth(120);

        table.getColumns().addAll(colNume, colUm, colStocCurent);

        ObservableList<Ingredient> dateTabel = FXCollections.observableArrayList(RestaurantService.getInstance().getToateIngredientele());
        table.setItems(dateTabel);


        VBox panouAprovizionare = new VBox(15);
        panouAprovizionare.setPadding(new Insets(20));
        panouAprovizionare.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-color: #bdc3c7; -fx-border-radius: 10px;");
        panouAprovizionare.setPrefWidth(400);

        Label labelAprovizionare = new Label("Panou de Aprovizionare");
        labelAprovizionare.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label labelIngredientSelectat = new Label("Ingredient selectat: Niciunul");
        labelIngredientSelectat.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        TextField inputCantitate = new TextField();
        inputCantitate.setPromptText("Introdu cantitatea adusă de furnizor...");

        Button btnAdaugaPeLista = new Button(" Adaugă pe Lista de Intrare");
        btnAdaugaPeLista.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAdaugaPeLista.setPrefWidth(250);


        ListView<String> listaVizualaAprovizionare = new ListView<>();
        listaVizualaAprovizionare.setPrefHeight(200);


        Map<Ingredient, Double> comandaAprovizionare = new HashMap<>();


        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                labelIngredientSelectat.setText("Selectat: " + newSelection.getNume() + " (" + newSelection.getUm() + ")");
            }
        });


        btnAdaugaPeLista.setOnAction(e -> {
            Ingredient selectat = table.getSelectionModel().getSelectedItem();
            if (selectat == null) {
                arataAlerta("Eroare", "Te rog selectează un ingredient din tabel făcând click pe el!", Alert.AlertType.ERROR);
                return;
            }
            try {
                double cant = Double.parseDouble(inputCantitate.getText().trim());
                if (cant <= 0) throw new NumberFormatException();


                comandaAprovizionare.put(selectat, comandaAprovizionare.getOrDefault(selectat, 0.0) + cant);


                listaVizualaAprovizionare.getItems().clear();
                for (Map.Entry<Ingredient, Double> entry : comandaAprovizionare.entrySet()) {
                    listaVizualaAprovizionare.getItems().add(entry.getKey().getNume() + " -> + " + entry.getValue() + " " + entry.getKey().getUm());
                }
                inputCantitate.clear();

            } catch (NumberFormatException ex) {
                arataAlerta("Eroare", "Cantitatea trebuie să fie un număr pozitiv și valid!", Alert.AlertType.ERROR);
            }
        });

        Button btnFinalizeaza = new Button(" Confirmă și Actualizează Stocul Bazei de Date");
        btnFinalizeaza.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px;");
        btnFinalizeaza.setPrefWidth(Double.MAX_VALUE);

        btnFinalizeaza.setOnAction(e -> {
            if (comandaAprovizionare.isEmpty()) {
                arataAlerta("Atenție", "Lista de aprovizionare este goală!", Alert.AlertType.WARNING);
                return;
            }


            for (Map.Entry<Ingredient, Double> entry : comandaAprovizionare.entrySet()) {
                Ingredient ing = entry.getKey();
                double cantitateAdusaDeFurnizor = entry.getValue();

                ing.setCantitate(ing.getCantitate() + cantitateAdusaDeFurnizor);

                RestaurantService.getInstance().actualizeazaIngredient(ing);
            }

            arataAlerta("Succes", "Sistemul a actualizat noile cantități în stoc!", Alert.AlertType.INFORMATION);


            comandaAprovizionare.clear();
            listaVizualaAprovizionare.getItems().clear();
            dateTabel.setAll(RestaurantService.getInstance().getToateIngredientele());
            table.refresh();
        });

        panouAprovizionare.getChildren().addAll(
                labelAprovizionare,
                labelIngredientSelectat,
                inputCantitate,
                btnAdaugaPeLista,
                new Separator(),
                new Label("Ingrediente gata de adăugat:"),
                listaVizualaAprovizionare,
                btnFinalizeaza
        );
        panouAprovizionare.setAlignment(Pos.TOP_CENTER);


        Button btnInapoi = new Button("⬅ Înapoi la Dashboard Manager");
        btnInapoi.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-cursor: hand;");
        btnInapoi.setOnAction(e -> stage.setScene(scenaManager));


        HBox layoutCentru = new HBox(40, table, panouAprovizionare);
        layoutCentru.setAlignment(Pos.CENTER);

        VBox layoutPrincipal = new VBox(20, btnInapoi, titlu, layoutCentru);
        layoutPrincipal.setAlignment(Pos.CENTER);
        layoutPrincipal.setPadding(new Insets(30));
        layoutPrincipal.setStyle("-fx-background-color: #f5f6fa;");

        return new Scene(layoutPrincipal, 1280, 720);
    }

    private void arataAlerta(String titlu, String mesaj, Alert.AlertType tip) {
        Alert alerta = new Alert(tip);
        alerta.setTitle(titlu);
        alerta.setHeaderText(null);
        alerta.setContentText(mesaj);
        alerta.showAndWait();
    }
}