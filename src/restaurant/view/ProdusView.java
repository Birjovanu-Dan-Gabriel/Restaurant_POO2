package restaurant.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restaurant.model.Ingredient;
import restaurant.model.Produs;
import restaurant.service.RestaurantService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProdusView {

    public Scene creazaScena(Stage stage, Scene scenaPrincipala) {
        Label titlu = new Label("Gestiune Meniu și Rețete");
        titlu.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        TableView<Produs> table = new TableView<>();

        TableColumn<Produs, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Produs, String> colNume = new TableColumn<>("Nume Produs");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colNume.setPrefWidth(150);

        TableColumn<Produs, Double> colPret = new TableColumn<>("Preț (RON)");
        colPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colPret.setPrefWidth(100);

        TableColumn<Produs, Double> colKcal = new TableColumn<>("Kcal");
        colKcal.setCellValueFactory(new PropertyValueFactory<>("kcal"));
        colKcal.setPrefWidth(80);


        TableColumn<Produs, String> colReteta = new TableColumn<>("Ingrediente (Rețetă)");
        colReteta.setCellValueFactory(cellData -> {
            Map<Ingredient, Double> reteta = cellData.getValue().getReteta();
            if (reteta == null || reteta.isEmpty()) {
                return new SimpleStringProperty("Fără ingrediente");
            }

            String textReteta = reteta.entrySet().stream()
                    .map(e -> e.getKey().getNume() + " (" + e.getValue() + " " + e.getKey().getUm() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(textReteta);
        });
        colReteta.setPrefWidth(350);

        table.getColumns().addAll(colId, colNume, colPret, colKcal, colReteta);


        List<Produs> produseDinDB = RestaurantService.getInstance().getTotMeniul();
        ObservableList<Produs> dateTabel = FXCollections.observableArrayList(produseDinDB);
        table.setItems(dateTabel);


        Button btnAdauga = new Button("Adaugă Produs Nou");
        Button btnSterge = new Button("Șterge Produs");
        Button btnInapoi = new Button("Înapoi");

        btnAdauga.setOnAction(e -> deschideFereastraAdaugareProdus(table, dateTabel));

        btnSterge.setOnAction(e -> {
            Produs produsSelectat = table.getSelectionModel().getSelectedItem();
            if (produsSelectat != null) {
                RestaurantService.getInstance().eliminaProdusDinMeniu(produsSelectat);
                dateTabel.remove(produsSelectat);
            } else {
                arataAlertaEroare("Te rog să selectezi un produs din tabel!");
            }
        });

        btnInapoi.setOnAction(e -> stage.setScene(scenaPrincipala));

        HBox panouButoane = new HBox(15);
        panouButoane.setAlignment(Pos.CENTER);
        panouButoane.getChildren().addAll(btnAdauga, btnSterge, btnInapoi);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titlu, table, panouButoane);

        return new Scene(layout, 1280, 720);
    }


    private void deschideFereastraAdaugareProdus(TableView<Produs> table, ObservableList<Produs> dateTabel) {
        Stage addStage = new Stage();
        addStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        addStage.setTitle("Creează Produs și Rețetă");

        TextField inputNume = new TextField();
        inputNume.setPromptText("Nume Produs (ex: Pizza Margherita)");

        TextField inputPret = new TextField();
        inputPret.setPromptText("Preț (RON)");

        TextField inputKcal = new TextField();
        inputKcal.setPromptText("Kcal");

        Label labelReteta = new Label("Rețeta Produsului:");
        labelReteta.setStyle("-fx-font-weight: bold;");


        ListView<String> listaIngredienteVizuala = new ListView<>();
        listaIngredienteVizuala.setPrefHeight(120);
        Map<Ingredient, Double> retetaTemporara = new HashMap<>();


        ComboBox<Ingredient> comboIngrediente = new ComboBox<>();
        comboIngrediente.getItems().addAll(RestaurantService.getInstance().getToateIngredientele());
        comboIngrediente.setPromptText("Alege Ingredient");


        comboIngrediente.setCellFactory(lv -> new ListCell<Ingredient>() {
            @Override
            protected void updateItem(Ingredient item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNume() + " (" + item.getUm() + ")");
            }
        });
        comboIngrediente.setButtonCell(comboIngrediente.getCellFactory().call(null));

        TextField inputCantitateIngredient = new TextField();
        inputCantitateIngredient.setPromptText("Cantitate");
        inputCantitateIngredient.setPrefWidth(80);

        Button btnAdaugaIngredient = new Button("+ Adaugă la Rețetă");


        btnAdaugaIngredient.setOnAction(e -> {
            Ingredient ingSelectat = comboIngrediente.getValue();
            String cant = inputCantitateIngredient.getText().trim();
            if (ingSelectat != null && !cant.isEmpty()) {
                try {
                    double cantitate = Double.parseDouble(cant);
                    if(cantitate <= 0) throw new NumberFormatException();

                    retetaTemporara.put(ingSelectat, cantitate);
                    listaIngredienteVizuala.getItems().add(ingSelectat.getNume() + " -> " + cantitate + " " + ingSelectat.getUm());
                    inputCantitateIngredient.clear();
                } catch (NumberFormatException ex) {
                    arataAlertaEroare("Cantitatea ingredientului trebuie să fie un număr pozitiv!");
                }
            } else {
                arataAlertaEroare("Selectează un ingredient și scrie cantitatea!");
            }
        });

        HBox panouAdaugareReteta = new HBox(10, comboIngrediente, inputCantitateIngredient, btnAdaugaIngredient);
        panouAdaugareReteta.setAlignment(Pos.CENTER);

        Button btnSalveazaProdus = new Button("Salvează Produsul Final");
        btnSalveazaProdus.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");


        btnSalveazaProdus.setOnAction(e -> {
            try {
                String nume = inputNume.getText().trim();
                double pret = Double.parseDouble(inputPret.getText().trim());
                double kcal = Double.parseDouble(inputKcal.getText().trim());

                if (nume.isEmpty()) {
                    arataAlertaEroare("Numele produsului este obligatoriu!");
                    return;
                }
                if (retetaTemporara.isEmpty()) {
                    arataAlertaEroare("Produsul trebuie să aibă cel puțin un ingredient în rețetă!");
                    return;
                }


                boolean existaDeja = dateTabel.stream()
                        .anyMatch(p -> p.getNume().equalsIgnoreCase(nume));

                if (existaDeja) {
                    arataAlertaEroare("Atenție: Există deja un produs cu numele exact '" + nume + "'!");
                    return;
                }



                Produs produsNou = new Produs(0, nume, pret, kcal);


                for (Map.Entry<Ingredient, Double> entry : retetaTemporara.entrySet()) {
                    produsNou.adaugaIngredientInReteta(entry.getKey(), entry.getValue());
                }


                RestaurantService.getInstance().adaugaProdusInMeniu(produsNou);


                dateTabel.setAll(RestaurantService.getInstance().getTotMeniul());
                addStage.close();

            } catch (NumberFormatException ex) {
                arataAlertaEroare("Prețul și Kcal trebuie să fie numere valide!");
            }
        });

        VBox layoutFormular = new VBox(15);
        layoutFormular.setPadding(new Insets(20));
        layoutFormular.getChildren().addAll(
                new Label("Detalii Produs:"),
                new HBox(10, new Label("Nume:"), inputNume),
                new HBox(10, new Label("Preț:"), inputPret, new Label("Kcal:"), inputKcal),
                new Separator(),
                labelReteta,
                panouAdaugareReteta,
                listaIngredienteVizuala,
                new Label(""),
                btnSalveazaProdus
        );
        layoutFormular.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layoutFormular, 450, 500);
        addStage.setScene(scene);
        addStage.showAndWait();
    }

    private void arataAlertaEroare(String mesaj) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Eroare");
        alerta.setHeaderText(null);
        alerta.setContentText(mesaj);
        alerta.showAndWait();
    }
}