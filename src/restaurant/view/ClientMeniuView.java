package restaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restaurant.model.Ingredient;
import restaurant.model.Produs;
import restaurant.service.RestaurantService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientMeniuView {

    public Scene creazaScena(Stage stage, Scene scenaPrincipala) {


        Label titlu = new Label("Meniul Restaurantului");
        titlu.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-family: 'Segoe UI', sans-serif;");

        Button btnInapoi = new Button("⬅ Înapoi la Prima Pagină");
        btnInapoi.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-font-size: 14px; -fx-cursor: hand;");
        btnInapoi.setOnMouseEntered(e -> btnInapoi.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 14px; -fx-cursor: hand;"));
        btnInapoi.setOnMouseExited(e -> btnInapoi.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d; -fx-font-size: 14px; -fx-cursor: hand;"));

        btnInapoi.setOnAction(e -> stage.setScene(scenaPrincipala));

        HBox antet = new HBox(titlu);
        antet.setAlignment(Pos.CENTER);

        VBox topBar = new VBox(10, btnInapoi, antet);
        topBar.setPadding(new Insets(20));


        VBox listaProduse = new VBox(20);
        listaProduse.setPadding(new Insets(20, 50, 40, 50));
        listaProduse.setStyle("-fx-background-color: white;");

        List<Produs> meniu = RestaurantService.getInstance().getTotMeniul();

        if (meniu.isEmpty()) {
            Label gol = new Label("Meniul nu a fost încărcat încă.");
            gol.setStyle("-fx-font-size: 16px; -fx-text-fill: #bdc3c7;");
            listaProduse.getChildren().add(gol);
        } else {

            for (Produs p : meniu) {
                listaProduse.getChildren().add(creazaRandMeniu(p));
                listaProduse.getChildren().add(new Separator());
            }
        }


        ScrollPane scrollPane = new ScrollPane(listaProduse);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-control-inner-background: white;");


        VBox layoutPrincipal = new VBox(topBar, scrollPane);
        layoutPrincipal.setStyle("-fx-background-color: white;");

        return new Scene(layoutPrincipal, 1280, 720);
    }


    private VBox creazaRandMeniu(Produs p) {

        Label numeLabel = new Label(p.getNume());
        numeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-family: 'Segoe UI', sans-serif;");


        Map<Ingredient, Double> reteta = p.getReteta();
        String textIngrediente = "Nu conține ingrediente specificate.";
        if (reteta != null && !reteta.isEmpty()) {
            textIngrediente = reteta.keySet().stream()
                    .map(Ingredient::getNume)
                    .collect(Collectors.joining(", "));
        }

        Label ingredienteLabel = new Label(textIngrediente);
        ingredienteLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #7f8c8d;");
        ingredienteLabel.setWrapText(true);

        VBox detaliiStanga = new VBox(5, numeLabel, ingredienteLabel);


        Label pretLabel = new Label(String.format("%.2f RON", p.getPret()));
        pretLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #D06B33;");


        Label kcalLabel = new Label(p.getKcal() + " Kcal");
        kcalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #95a5a6;");

        VBox detaliiDreapta = new VBox(5, pretLabel, kcalLabel);
        detaliiDreapta.setAlignment(Pos.CENTER_RIGHT);


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        HBox rand = new HBox(detaliiStanga, spacer, detaliiDreapta);
        rand.setAlignment(Pos.CENTER_LEFT);

        VBox cardProdus = new VBox(rand);
        cardProdus.setPadding(new Insets(10, 0, 10, 0));

        return cardProdus;
    }
}