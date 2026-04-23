package restaurant;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restaurant.view.*;

public class RestaurantGUI extends Application {

    private Scene scenaClient;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurant App - Bine ați venit!");

        // --- Fundal ---
        Pane backgroundPane = new Pane();
        try {
            String imagePath = getClass().getResource("/resources/images/clientBgk.jpg").toExternalForm();
            backgroundPane.setStyle(
                    "-fx-background-image: url('" + imagePath + "');" +
                            "-fx-background-size: cover;" +
                            "-fx-background-position: center center;"
            );

            javafx.scene.effect.GaussianBlur blur = new javafx.scene.effect.GaussianBlur(8);
            backgroundPane.setEffect(blur);
        } catch (Exception e) {
            System.out.println("Eroare: Nu am putut încărca imaginea de fundal!");
            backgroundPane.setStyle("-fx-background-color: #2c3e50;");
        }


        Label titlu = new Label("Bun venit la Pizzeria Dan");

        titlu.setTranslateY(-80);
        titlu.setStyle("-fx-font-size: 46px; -fx-font-weight: 800; -fx-text-fill: white; " +
                "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif; " +
                "-fx-effect: dropshadow(gaussian, black, 25, 0.5, 0, 5);");

        Label subtitlu = new Label("Descoperă gustul excelenței");
        subtitlu.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: #e0e0e0; " +
                "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif; " +
                "-fx-effect: dropshadow(gaussian, black, 15, 0.3, 0, 3);");


        String buttonStyle = "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #D06B33, #783510); " +
                "-fx-background-radius: 25px; " +
                "-fx-border-color: rgba(255,255,255,0.4); " +
                "-fx-border-radius: 25px; " +
                "-fx-border-width: 1.5px; " +
                "-fx-padding: 10px 20px;";

        String buttonHoverStyle = "-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #E37B40, #8A4116); " +
                "-fx-background-radius: 25px; " +
                "-fx-border-color: rgba(255,255,255,0.9); " +
                "-fx-border-radius: 25px; " +
                "-fx-border-width: 1.5px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-cursor: hand;";


        String buttonTextStyle = "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Segoe UI', sans-serif; " +
                "-fx-text-fill: white; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 4, 0.3, 0, 2);";


        Label textMeniu = new Label("Vezi Meniul");
        textMeniu.setStyle(buttonTextStyle);

        Button btnVeziMeniu = new Button();
        btnVeziMeniu.setGraphic(textMeniu);
        btnVeziMeniu.setStyle(buttonStyle);
        btnVeziMeniu.setPrefWidth(200);
        btnVeziMeniu.setOnMouseEntered(e -> btnVeziMeniu.setStyle(buttonHoverStyle));
        btnVeziMeniu.setOnMouseExited(e -> btnVeziMeniu.setStyle(buttonStyle));

        btnVeziMeniu.setOnAction(e -> {
            ClientMeniuView meniuView = new ClientMeniuView();
            primaryStage.setScene(meniuView.creazaScena(primaryStage, scenaClient));
        });


        Label textRezervare = new Label("Fă o Rezervare");
        textRezervare.setStyle(buttonTextStyle);

        Button btnRezervare = new Button();
        btnRezervare.setGraphic(textRezervare);
        btnRezervare.setStyle(buttonStyle);
        btnRezervare.setPrefWidth(200);
        btnRezervare.setOnMouseEntered(e -> btnRezervare.setStyle(buttonHoverStyle));
        btnRezervare.setOnMouseExited(e -> btnRezervare.setStyle(buttonStyle));

        VBox panouActiuniClient = new VBox(15, btnVeziMeniu, btnRezervare);
        panouActiuniClient.setAlignment(Pos.CENTER_RIGHT);


        Button btnLoginAdmin = new Button("Acces Personal");
        btnLoginAdmin.setStyle("-fx-background-color: transparent; -fx-text-fill: #bdc3c7; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 13px;");
        btnLoginAdmin.setOnMouseEntered(e -> btnLoginAdmin.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 13px; -fx-cursor: hand;"));
        btnLoginAdmin.setOnMouseExited(e -> btnLoginAdmin.setStyle("-fx-background-color: transparent; -fx-text-fill: #bdc3c7; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 13px;"));
        btnLoginAdmin.setOnAction(e -> deschideFereastraLogin(primaryStage));


        VBox layoutContinut = new VBox(30);
        layoutContinut.setAlignment(Pos.CENTER_RIGHT);
        layoutContinut.setPadding(new Insets(0, 100, 0, 0));
        layoutContinut.setStyle("-fx-background-color: transparent;");

        layoutContinut.getChildren().addAll(titlu, subtitlu, panouActiuniClient, new Label(""), btnLoginAdmin);


        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundPane, layoutContinut);

        scenaClient = new Scene(root, 1280, 720);
        primaryStage.setScene(scenaClient);
        primaryStage.show();
    }


    private void deschideFereastraLogin(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        loginStage.setTitle("Autentificare Personal");

        TextField inputUser = new TextField();
        inputUser.setPromptText("Nume Utilizator (ex: Manager)");

        PasswordField inputParola = new PasswordField();
        inputParola.setPromptText("Parolă");

        Button btnLogin = new Button("Intră în cont");
        btnLogin.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        btnLogin.setOnAction(e -> {
            String user = inputUser.getText().trim();
            String parola = inputParola.getText();


            if (parola.equals("admin")) {

                if (user.equalsIgnoreCase("Manager")) {
                    ManagerPortalView view = new ManagerPortalView();
                    primaryStage.setScene(view.creazaScena(primaryStage, scenaClient));
                    loginStage.close();
                } else if (user.equalsIgnoreCase("Bucatar")) {
                    BucatarPortalView view = new BucatarPortalView();
                    primaryStage.setScene(view.creazaScena(primaryStage, scenaClient));
                    loginStage.close();
                } else if (user.equalsIgnoreCase("Chelner")) {
                    ChelnerPortalView view = new ChelnerPortalView();
                    primaryStage.setScene(view.creazaScena(primaryStage, scenaClient));
                    loginStage.close();
                } else {

                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Eroare");
                    alerta.setHeaderText(null);
                    alerta.setContentText("Utilizator necunoscut! Folosește: Manager, Bucatar sau Chelner.");
                    alerta.showAndWait();
                }
            } else {

                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Eroare");
                alerta.setHeaderText(null);
                alerta.setContentText("Parolă incorectă!");
                alerta.showAndWait();
            }
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
        layout.getChildren().addAll(
                new Label("Introduceți datele de acces:"),
                inputUser,
                inputParola,
                btnLogin
        );

        Scene scene = new Scene(layout, 320, 270);
        loginStage.setScene(scene);
        loginStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}