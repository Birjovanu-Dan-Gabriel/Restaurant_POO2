package restaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import restaurant.model.Produs;
import restaurant.service.RestaurantService;

import java.util.List;
import java.util.Map;

public class BucatarPortalView {


    private VBox listaComenziContainer;

    public Scene creazaScena(Stage stage, Scene scenaClient) {

        Label titlu = new Label("Portal Bucătărie - Comenzi Active");
        titlu.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Region spacerTop = new Region();
        HBox.setHgrow(spacerTop, Priority.ALWAYS);

        Button btnRefresh = new Button("Actualizează");
        btnRefresh.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnRefresh.setOnAction(e -> randeazaComenzi());

        Button btnLogOut = new Button("Log Out");
        btnLogOut.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnLogOut.setOnAction(e -> stage.setScene(scenaClient));

        HBox topBar = new HBox(15, titlu, spacerTop, btnRefresh, btnLogOut);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 0 0 1px 0;");


        listaComenziContainer = new VBox(20);
        listaComenziContainer.setPadding(new Insets(30));
        listaComenziContainer.setAlignment(Pos.TOP_CENTER);


        randeazaComenzi();


        ScrollPane scrollPane = new ScrollPane(listaComenziContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #ecf0f1;");


        VBox layoutPrincipal = new VBox(topBar, scrollPane);
        layoutPrincipal.setStyle("-fx-background-color: #ecf0f1;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(layoutPrincipal, 1280, 720);
    }


    private void randeazaComenzi() {
        listaComenziContainer.getChildren().clear();


        Map<Integer, List<Produs>> comenziReale = RestaurantService.getInstance().getComenziBucatarie();

        if (comenziReale == null || comenziReale.isEmpty()) {
            Label labelGol = new Label("Nu există nicio comandă activă momentan.");
            labelGol.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");
            listaComenziContainer.getChildren().add(labelGol);
        } else {
            for (Map.Entry<Integer, List<Produs>> entry : comenziReale.entrySet()) {
                HBox cardComanda = creazaCardComanda(entry.getKey(), entry.getValue());
                listaComenziContainer.getChildren().add(cardComanda);
            }
        }
    }


    private HBox creazaCardComanda(int nrMasa, List<Produs> produse) {

        VBox detaliiStanga = new VBox(10);

        Label lblMasa = new Label("Masa " + nrMasa);
        lblMasa.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        detaliiStanga.getChildren().add(lblMasa);

        for (Produs p : produse) {
            Label lblProdus = new Label("- " + p.getNume());
            lblProdus.setStyle("-fx-font-size: 16px; -fx-text-fill: #34495e;");
            detaliiStanga.getChildren().add(lblProdus);
        }


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button btnFinalizeaza = new Button("Finalizează");
        btnFinalizeaza.setStyle("-fx-background-color: white; -fx-border-color: #7f8c8d; -fx-border-radius: 5px; " +
                "-fx-padding: 10px 30px; -fx-font-size: 16px; -fx-cursor: hand;");


        btnFinalizeaza.setOnMouseEntered(e -> btnFinalizeaza.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-border-color: #27ae60; -fx-border-radius: 5px; -fx-padding: 10px 30px; -fx-font-size: 16px; -fx-cursor: hand;"));
        btnFinalizeaza.setOnMouseExited(e -> btnFinalizeaza.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: #7f8c8d; -fx-border-radius: 5px; -fx-padding: 10px 30px; -fx-font-size: 16px; -fx-cursor: hand;"));


        btnFinalizeaza.setOnAction(e -> {
            RestaurantService.getInstance().bucatarAprobaComanda(nrMasa);
            randeazaComenzi();
        });


        HBox card = new HBox(detaliiStanga, spacer, btnFinalizeaza);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        card.setMaxWidth(800);

        return card;
    }
}