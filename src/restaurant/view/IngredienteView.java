package restaurant.view;

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
import restaurant.service.RestaurantService;

import java.util.List;


public class IngredienteView {

    public Scene creazaScena(Stage stage, Scene scenaPrincipala) {
        Label titlu = new Label("Gestiune Stoc Ingrediente");
        titlu.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        TableView<Ingredient> table = new TableView<>();

        TableColumn<Ingredient, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Ingredient, String> colNume = new TableColumn<>("Nume Ingredient");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colNume.setPrefWidth(200);

        TableColumn<Ingredient, Double> colCantitate = new TableColumn<>("Cantitate / Stoc");
        colCantitate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        colCantitate.setPrefWidth(150);

        TableColumn<Ingredient, String> colUm = new TableColumn<>("U.M.");
        colUm.setCellValueFactory(new PropertyValueFactory<>("um"));
        colUm.setPrefWidth(100);

        table.getColumns().addAll(colId, colNume, colCantitate, colUm);


        List<Ingredient> ingredienteDinDB = RestaurantService.getInstance().getToateIngredientele();
        ObservableList<Ingredient> dateTabel = FXCollections.observableArrayList(ingredienteDinDB);
        table.setItems(dateTabel);


        Button btnAdauga = new Button("Adaugă Ingredient");
        Button btnSterge = new Button("Șterge Ingredient");
        Button btnModifica = new Button("Modifică Cantitate");
        Button btnInapoi = new Button("Înapoi");


        btnAdauga.setOnAction(e -> deschideFereastraAdaugare(table, dateTabel));

        btnSterge.setOnAction(e -> {
            Ingredient ingredientSelectat = table.getSelectionModel().getSelectedItem();
            if (ingredientSelectat != null) {

                RestaurantService.getInstance().eliminaIngredientDinDB(ingredientSelectat);
                dateTabel.remove(ingredientSelectat);
            } else {
                arataAlertaEroare("Te rog să selectezi un ingredient din tabel apăsând pe el!");
            }
        });


        btnModifica.setOnAction(e -> {
            Ingredient ingredientSelectat = table.getSelectionModel().getSelectedItem();
            if (ingredientSelectat != null) {
                deschideFereastraModificare(ingredientSelectat, table, dateTabel);
            } else {
                arataAlertaEroare("Selectează un ingredient din tabel pentru a-i modifica stocul!");
            }
        });

        btnInapoi.setOnAction(e -> stage.setScene(scenaPrincipala));

        HBox panouButoane = new HBox(15);
        panouButoane.setAlignment(Pos.CENTER);
        panouButoane.getChildren().addAll(btnAdauga, btnModifica, btnSterge, btnInapoi);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titlu, table, panouButoane);

        return new Scene(layout, 1280, 720);
    }


    private void deschideFereastraAdaugare(TableView<Ingredient> table, ObservableList<Ingredient> dateTabel) {
        Stage addStage = new Stage();
        addStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        addStage.setTitle("Adaugă Ingredient în Stoc");

        TextField inputNume = new TextField();
        inputNume.setPromptText("Ex: Făină");

        TextField inputCantitate = new TextField();
        inputCantitate.setPromptText("Ex: 5.5");


        ComboBox<String> comboUm = new ComboBox<>();
        comboUm.getItems().addAll("kg", "g", "L", "ml", "buc");
        comboUm.setPromptText("Unitate (U.M.)");

        Button btnAccept = new Button("Accept");
        Button btnCancel = new Button("Cancel");

        btnCancel.setOnAction(e -> addStage.close());

        btnAccept.setOnAction(e -> {
            String nume = inputNume.getText().trim();
            String cantitateStr = inputCantitate.getText().trim();
            String um = comboUm.getValue();

            if (nume.isEmpty() || cantitateStr.isEmpty() || um == null) {
                arataAlertaEroare("Toate câmpurile sunt obligatorii (inclusiv unitatea de măsură)!");
                return;
            }

            try {
                double cantitate = Double.parseDouble(cantitateStr);
                if (cantitate < 0) {
                    arataAlertaEroare("Cantitatea nu poate fi negativă!");
                    return;
                }


                Ingredient ingredientNou = new Ingredient(0, nume, cantitate, um);


                RestaurantService.getInstance().adaugaIngredient(ingredientNou);


                dateTabel.setAll(RestaurantService.getInstance().getToateIngredientele());
                addStage.close();

            } catch (NumberFormatException ex) {
                arataAlertaEroare("Cantitatea trebuie să fie un număr valid (ex: 10 sau 2.5)!");
            }
        });

        HBox panouButoane = new HBox(10);
        panouButoane.setAlignment(Pos.CENTER);
        panouButoane.getChildren().addAll(btnAccept, btnCancel);

        VBox layoutFormular = new VBox(15);
        layoutFormular.setPadding(new Insets(20));
        layoutFormular.setAlignment(Pos.CENTER);
        layoutFormular.getChildren().addAll(
                new Label("Nume Ingredient:"), inputNume,
                new Label("Cantitate:"), inputCantitate,
                new Label("Unitate de măsură:"), comboUm,
                new Label(""),
                panouButoane
        );

        Scene scene = new Scene(layoutFormular, 300, 350);
        addStage.setScene(scene);
        addStage.showAndWait();
    }


    private void deschideFereastraModificare(Ingredient ingredient, TableView<Ingredient> table, ObservableList<Ingredient> dateTabel) {
        Stage editStage = new Stage();
        editStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        editStage.setTitle("Modifică Stoc: " + ingredient.getNume());

        Label infoLabel = new Label("Stoc curent: " + ingredient.getCantitate() + " " + ingredient.getUm());

        TextField inputCantitate = new TextField();

        inputCantitate.setText(String.valueOf(ingredient.getCantitate()));

        Button btnSalveaza = new Button("Salvează");
        Button btnCancel = new Button("Cancel");

        btnCancel.setOnAction(e -> editStage.close());

        btnSalveaza.setOnAction(e -> {
            String cantitateStr = inputCantitate.getText().trim();
            if (cantitateStr.isEmpty()) {
                arataAlertaEroare("Te rog să introduci o cantitate!");
                return;
            }

            try {
                double cantitateNoua = Double.parseDouble(cantitateStr);
                if (cantitateNoua < 0) {
                    arataAlertaEroare("Stocul nu poate fi negativ!");
                    return;
                }


                RestaurantService.getInstance().actualizeazaStocIngredient(ingredient, cantitateNoua);


                dateTabel.setAll(RestaurantService.getInstance().getToateIngredientele());
                table.refresh(); // Forțăm tabelul să se redeseneze

                editStage.close();

            } catch (NumberFormatException ex) {
                arataAlertaEroare("Cantitatea trebuie să fie un număr valid (ex: 5 sau 2.5)!");
            }
        });

        HBox panouButoane = new HBox(10);
        panouButoane.setAlignment(Pos.CENTER);
        panouButoane.getChildren().addAll(btnSalveaza, btnCancel);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(infoLabel, new Label("Noua Cantitate:"), inputCantitate, panouButoane);

        Scene scene = new Scene(layout, 300, 200);
        editStage.setScene(scene);
        editStage.showAndWait();
    }


    private void arataAlertaEroare(String mesaj) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Eroare");
        alerta.setHeaderText(null);
        alerta.setContentText(mesaj);
        alerta.showAndWait();
    }
}