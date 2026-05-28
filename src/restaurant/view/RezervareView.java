package restaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import restaurant.model.Client;
import restaurant.model.Masa;
import restaurant.model.Rezervare;
import restaurant.repository.ClientRepository;
import restaurant.repository.RezervareRepository;
import restaurant.model.RezervareBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RezervareView {

    public Scene creazaScena(Stage stage, Scene scenaPrincipalaClient) {

        Label titlu = new Label("Rezervare Masă");
        titlu.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");


        TextField txtNume = new TextField();
        txtNume.setPromptText("Ex: Ion Popescu");

        TextField txtTelefon = new TextField();
        txtTelefon.setPromptText("Ex: 07XX XXX XXX");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Alegeți data");


        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);


                if (item != null && !empty) {

                    if (item.isBefore(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #bdc3c7;");
                    }


                    if (item.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #c0392b;");
                    }
                }
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);


        ComboBox<String> comboOra = new ComboBox<>();
        for (int i = 10; i <= 21; i++) {
            comboOra.getItems().add(i + ":00");
        }
        comboOra.setPromptText("Ora rezervării");

        ComboBox<Integer> comboMasa = new ComboBox<>();
        for (int i = 1; i <= 12; i++) {
            comboMasa.getItems().add(i);
        }
        comboMasa.setPromptText("Număr Masă");


        double fieldWidth = 200;
        txtNume.setPrefWidth(fieldWidth);
        txtTelefon.setPrefWidth(fieldWidth);
        datePicker.setPrefWidth(fieldWidth);
        comboOra.setPrefWidth(fieldWidth);
        comboMasa.setPrefWidth(fieldWidth);


        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(20);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Nume complet:"), 0, 0); grid.add(txtNume, 1, 0);
        grid.add(new Label("Telefon:"), 0, 1);      grid.add(txtTelefon, 1, 1);
        grid.add(new Label("Data:"), 0, 2);         grid.add(datePicker, 1, 2);
        grid.add(new Label("Ora:"), 0, 3);          grid.add(comboOra, 1, 3);
        grid.add(new Label("Masă dorită:"), 0, 4);  grid.add(comboMasa, 1, 4);


        Button btnConfirma = new Button("Confirmă Rezervarea");
        btnConfirma.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-cursor: hand;");

        Button btnAnuleaza = new Button("⬅ Înapoi la Meniu");
        btnAnuleaza.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-cursor: hand;");
        btnAnuleaza.setOnAction(e -> stage.setScene(scenaPrincipalaClient));


        btnConfirma.setOnAction(e -> {
           // calidare campuri goale
            if (txtNume.getText().isEmpty() || txtTelefon.getText().isEmpty() ||
                    datePicker.getValue() == null || comboOra.getValue() == null ||
                    comboMasa.getValue() == null) {

                arataAlerta(Alert.AlertType.WARNING, "Atenție", "Te rugăm să completezi toate câmpurile!");
                return;
            }

            LocalDate data = datePicker.getValue();


            if (data.getDayOfWeek() == DayOfWeek.SUNDAY) {
                arataAlerta(Alert.AlertType.WARNING, "Restaurant Închis", "Nu preluăm rezervări duminica. Te rugăm să alegi o altă zi!");
                return;
            }

            try {

                LocalTime ora = LocalTime.parse(comboOra.getValue());
                LocalDateTime dataOra = LocalDateTime.of(data, ora);


                Client clientNou = new Client(0, txtNume.getText(), txtTelefon.getText());
                ClientRepository.getInstance().insert(clientNou);


                Masa masaSelectata = new Masa(comboMasa.getValue(), 4);


                Rezervare rezervareNoua = new RezervareBuilder()
                        .setDataOra(dataOra)
                        .setClient(clientNou)
                        .setMasa(masaSelectata)
                        .build();
                RezervareRepository.getInstance().insert(rezervareNoua);


                arataAlerta(Alert.AlertType.INFORMATION, "Succes", "Rezervarea pe numele " + clientNou.getNume() + " a fost confirmată cu succes!");
                stage.setScene(scenaPrincipalaClient);

            } catch (Exception ex) {
                arataAlerta(Alert.AlertType.ERROR, "Eroare", "A apărut o eroare la procesarea datelor: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        HBox panouButoane = new HBox(20, btnAnuleaza, btnConfirma);
        panouButoane.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30, titlu, grid, panouButoane);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #fdfdfd;");

        return new Scene(layout, 1280, 720);
    }

    private void arataAlerta(Alert.AlertType tip, String titlu, String mesaj) {
        Alert alerta = new Alert(tip);
        alerta.setTitle(titlu);
        alerta.setHeaderText(null);
        alerta.setContentText(mesaj);
        alerta.showAndWait();
    }
}