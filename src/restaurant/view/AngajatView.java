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
import restaurant.model.Angajat;
import restaurant.model.AngajatFactory;
import restaurant.model.Bucatar;
import restaurant.model.Chelner;
import restaurant.repository.AngajatRepository;


import java.util.List;

//TODO -- vezi ce nu are ce cauta aici si treuie mutat in service (pt toate viewurile)
public class AngajatView {
    public Scene creazaScenaAngajati(Stage stage, Scene scenaPricipala) {
        Label titlu = new Label("Gestiune Angajați");
        titlu.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // --- Coloane Tabel ---
        TableView<Angajat> table = new TableView<>();

        TableColumn<Angajat, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(50);

        TableColumn<Angajat, String> colNume = new TableColumn<>("Nume");
        colNume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colNume.setPrefWidth(200);

        TableColumn<Angajat, Double> colSalariu = new TableColumn<>("Salariu (RON)");
        colSalariu.setCellValueFactory(new PropertyValueFactory<>("salariu"));
        colSalariu.setPrefWidth(150);


        TableColumn<Angajat, Integer> colVarsta = new TableColumn<>("Vârstă");
        colVarsta.setCellValueFactory(new PropertyValueFactory<>("varsta"));
        colVarsta.setPrefWidth(70);

        TableColumn<Angajat, String> colGen = new TableColumn<>("Gen");
        colGen.setCellValueFactory(new PropertyValueFactory<>("gen"));
        colGen.setPrefWidth(70);

        TableColumn<Angajat, String> colData = new TableColumn<>("Data Angajare");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataAngajare"));
        colData.setPrefWidth(120);

        TableColumn<Angajat, String> colTip = new TableColumn<>("Funcție");
        colTip.setCellValueFactory(cellData -> {
            Angajat a = cellData.getValue();
            if (a instanceof Chelner) return new SimpleStringProperty("Chelner");
            if (a instanceof Bucatar) return new SimpleStringProperty("Bucătar");
            return new SimpleStringProperty("Necunoscut");
        });
        colTip.setPrefWidth(150);

        table.getColumns().addAll(colId, colNume, colSalariu, colVarsta, colGen, colData, colTip);

        // === Final Coloane ===


        // --- Prelucreare Date din BD ---

        List<Angajat> angajatiDinDB = restaurant.service.RestaurantService.getInstance().getTotiAngajatii();
        ObservableList<Angajat> dateTabel = FXCollections.observableArrayList(angajatiDinDB);
        table.setItems(dateTabel);


        // --- Creare butoane ---

        Button btnAdauga = new Button("Adaugă Angajat");
        Button btnModifica = new Button("Modifică Angajat");
        Button btnSterge = new Button("Șterge Angajat");
        Button btnInapoi = new Button("Înapoi");


        //      Buton Adauga

        btnAdauga.setOnAction(e -> deschideFereastraAdaugareAngajat(dateTabel));

        btnModifica.setOnAction(e -> {
            Angajat angajatSelectat = table.getSelectionModel().getSelectedItem();
            if (angajatSelectat != null) {
                deschideFereastraModificareAngajat(angajatSelectat, dateTabel, table);
            } else {
                arataAlertaEroare("Te rog să selectezi un angajat din tabel pentru a-l modifica!");
            }
        });

        //      Buton Sterge
        btnSterge.setOnAction(e -> {
            Angajat angajatSelectat = table.getSelectionModel().getSelectedItem();

            if (angajatSelectat != null) {
                restaurant.service.RestaurantService.getInstance().eliminaAngajat(angajatSelectat);
                dateTabel.remove(angajatSelectat);
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Atenție");
                alerta.setHeaderText(null);
                alerta.setContentText("Te rog să selectezi un angajat din tabel apăsând pe el!");
                alerta.showAndWait();
            }
        });

        //      Buton Back
        btnInapoi.setOnAction(e -> stage.setScene(scenaPricipala));

        //      Pozitionare Butoane
        HBox panouButoane = new HBox(15);
        panouButoane.setAlignment(Pos.CENTER);
        panouButoane.getChildren().addAll(btnAdauga, btnModifica, btnSterge, btnInapoi);

        //  --- Final Butoane ---


        // --- Asamblarea Scenei ---
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20)); // Lăsăm un spațiu de 20px pe margini
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titlu, table, panouButoane);

        return new Scene(layout, 1280, 720);
    }

    // --- POP-UP ADAUGARE ANGAJAT ---

    private void deschideFereastraAdaugareAngajat(ObservableList<Angajat> dateTabel) {
        Stage addStage = new Stage();
        addStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        addStage.setTitle("Adaugă Angajat Nou");

        //  --- Campuri de completat ---
        javafx.scene.control.TextField inputNume = new javafx.scene.control.TextField();
        inputNume.setPromptText("Introduceți Numele");

        javafx.scene.control.TextField inputSalariu = new javafx.scene.control.TextField();
        inputSalariu.setPromptText("Salariu (ex: 3500)");

        javafx.scene.control.ComboBox<String> comboTip = new javafx.scene.control.ComboBox<>();
        comboTip.getItems().addAll("Chelner", "Bucatar");
        comboTip.setPromptText("Selectați Funcția");

        javafx.scene.control.TextField inputVarsta = new javafx.scene.control.TextField();
        inputVarsta.setPromptText("Vârstă (ex: 25)");

        javafx.scene.control.ComboBox<String> comboGen = new javafx.scene.control.ComboBox<>();
        comboGen.getItems().addAll("M", "F", "Altul");
        comboGen.setPromptText("Gen");

        DatePicker inputData = new DatePicker();
        inputData.setPromptText("Data Angajării");

        //  === END Campuri de Completat ===


        //  --- Creare Butoane ---
        Button btnAccept = new Button("Accept");
        Button btnCancel = new Button("Cancel");

        //      Buton Cancel

        btnCancel.setOnAction(e -> addStage.close());

        //      Buton Accept

        btnAccept.setOnAction(e -> {
            String nume = inputNume.getText().trim();
            String salariuStr = inputSalariu.getText().trim();
            String tip = comboTip.getValue();
            String varstaStr = inputVarsta.getText().trim();
            String gen = comboGen.getValue();
            java.time.LocalDate dataSelectata = inputData.getValue();


            if (nume.isEmpty() || salariuStr.isEmpty() || tip == null || varstaStr.isEmpty() || gen == null || dataSelectata == null) {
                arataAlertaEroare("Toate câmpurile sunt obligatorii!");
                return;
            }

            try {
                // Parsam cifrele
                double salariu = Double.parseDouble(salariuStr);
                int varsta = Integer.parseInt(varstaStr);

                // Erorile posibile
                if (salariu <= 0 || varsta < 18) {
                    arataAlertaEroare("Salariul trebuie > 0, iar vârsta minimă este 18 ani!");
                    return;
                }

                // transformam data din calendar în text (format YYYY-MM-DD)
                String dataAngajare = dataSelectata.toString();


                Angajat angajatNou = AngajatFactory.creazaAngajat(tip.toUpperCase(), 0, nume, salariu, varsta, gen, dataAngajare);
                restaurant.service.RestaurantService.getInstance().adaugaAngajat(angajatNou);
                dateTabel.setAll(AngajatRepository.getInstance().getAll());
                addStage.close();

            } catch (NumberFormatException ex) {
                arataAlertaEroare("Salariul și Vârsta trebuie să fie numere valide (ex: nu litere)!");
            }
        });


        HBox panouButoane = new HBox(10);
        panouButoane.setAlignment(Pos.CENTER);
        panouButoane.getChildren().addAll(btnAccept, btnCancel);

        //  === END Butoane ===


        VBox layoutFormular = new VBox(10); // Spatiu mai mic intre elemente
        layoutFormular.setPadding(new javafx.geometry.Insets(20));
        layoutFormular.setAlignment(Pos.CENTER);

        // --- Adaugam totyl pe ecran ---
        layoutFormular.getChildren().addAll(
                new Label("Nume Angajat:"), inputNume,
                new Label("Salariu:"), inputSalariu,
                new Label("Vârstă:"), inputVarsta,
                new Label("Gen:"), comboGen,
                new Label("Data Angajării:"), inputData,
                new Label("Funcție:"), comboTip,
                new Label(""), // Spatiu gol inainte de butoane
                panouButoane
        );


        Scene scene = new Scene(layoutFormular, 350, 500);
        addStage.setScene(scene);
        addStage.showAndWait();
    }

    // --- POP-UP MODIFICARE ANGAJAT ---
    private void deschideFereastraModificareAngajat(Angajat angajat, ObservableList<Angajat> dateTabel, TableView<Angajat> table) {
        Stage editStage = new Stage();
        editStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        editStage.setTitle("Modifică Angajat: " + angajat.getNume());

        //  --- Campurile pre-completate  ---
        TextField inputNume = new TextField(angajat.getNume());
        TextField inputSalariu = new TextField(String.valueOf(angajat.getSalariu()));
        TextField inputVarsta = new TextField(String.valueOf(angajat.getVarsta()));

        ComboBox<String> comboGen = new ComboBox<>();
        comboGen.getItems().addAll("M", "F", "Altul");
        comboGen.setValue(angajat.getGen());

        ComboBox<String> comboTip = new ComboBox<>();
        comboTip.getItems().addAll("Chelner", "Bucatar");


        if (angajat instanceof Chelner) comboTip.setValue("Chelner");
        else if (angajat instanceof Bucatar) comboTip.setValue("Bucatar");

        DatePicker inputData = new DatePicker();
        try {
            // Convertim String-ul din baza de date inapoi in local-date pt calendar
            inputData.setValue(java.time.LocalDate.parse(angajat.getDataAngajare()));
        } catch (Exception e) {
            System.out.println("Nu am putut parsa data veche.");
        }

        Button btnSalveaza = new Button("Salvează Modificările");
        Button btnCancel = new Button("Cancel");

        btnCancel.setOnAction(e -> editStage.close());

        btnSalveaza.setOnAction(e -> {
            String numeNou = inputNume.getText().trim();
            String salariuStr = inputSalariu.getText().trim();
            String tipNou = comboTip.getValue();
            String varstaStr = inputVarsta.getText().trim();
            String genNou = comboGen.getValue();
            java.time.LocalDate dataSelectata = inputData.getValue();

            if (numeNou.isEmpty() || salariuStr.isEmpty() || tipNou == null || varstaStr.isEmpty() || genNou == null || dataSelectata == null) {
                arataAlertaEroare("Toate câmpurile sunt obligatorii!");
                return;
            }

            try {
                double salariuNou = Double.parseDouble(salariuStr);
                int varstaNoua = Integer.parseInt(varstaStr);

                if (salariuNou <= 0 || varstaNoua < 18) {
                    arataAlertaEroare("Salariul trebuie > 0, iar vârsta minimă este 18 ani!");
                    return;
                }

                // folosim Factory pentru a recrea angajatul cu acelasi ID, dar date noi
                Angajat angajatModificat = AngajatFactory.creazaAngajat(
                        tipNou.toUpperCase(),
                        angajat.getId(), // !! trebuie sa se pastreze id-ul vechi
                        numeNou,
                        salariuNou,
                        varstaNoua,
                        genNou,
                        dataSelectata.toString()
                );


                System.out.println("Trimitem spre modificare angajatul cu ID: " + angajatModificat.getId());
                restaurant.service.RestaurantService.getInstance().actualizeazaAngajat(angajatModificat);


                dateTabel.setAll(AngajatRepository.getInstance().getAll());
                table.refresh();
                editStage.close();

            } catch (NumberFormatException ex) {
                arataAlertaEroare("Salariul și Vârsta trebuie să fie numere valide!");
            }
        });

        HBox panouButoane = new HBox(10);
        panouButoane.setAlignment(Pos.CENTER);
        panouButoane.getChildren().addAll(btnSalveaza, btnCancel);

        VBox layoutFormular = new VBox(10);
        layoutFormular.setPadding(new Insets(20));
        layoutFormular.setAlignment(Pos.CENTER);
        layoutFormular.getChildren().addAll(
                new Label("Nume Angajat:"), inputNume,
                new Label("Salariu:"), inputSalariu,
                new Label("Vârstă:"), inputVarsta,
                new Label("Gen:"), comboGen,
                new Label("Data Angajării:"), inputData,
                new Label("Funcție:"), comboTip,
                new Label(""),
                panouButoane
        );

        Scene scene = new Scene(layoutFormular, 350, 500);
        editStage.setScene(scene);
        editStage.showAndWait();
    }

    private void arataAlertaEroare(String mesaj) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alerta.setTitle("Eroare Validare");
        alerta.setHeaderText(null);
        alerta.setContentText(mesaj);
        alerta.showAndWait();
    }


}
