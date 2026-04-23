package restaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManagerPortalView {

    private Scene scenaManager;

    public Scene creazaScena(Stage stage, Scene scenaClient) {
        Label titlu = new Label("Dashboard Manager");
        titlu.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-family: 'Segoe UI', sans-serif;");

        Label subtitlu = new Label("Alege secțiunea pe care dorești să o administrezi");
        subtitlu.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");


        Button btnAngajati = new Button("Gestionare Angajați");
        Button btnIngrediente = new Button("Gestionare Ingrediente");
        Button btnProduse = new Button("Gestionare Produse (Meniu)");
        Button btnStock = new Button("Gestionare Stock");
        Button btnLogOut = new Button("Log Out");


        String buttonStyle = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; " +
                "-fx-background-color: #34495e; -fx-background-radius: 8px; -fx-padding: 15px 20px;";
        String hoverStyle = "-fx-background-color: #2c3e50; -fx-cursor: hand; -fx-background-radius: 8px;";

        Button[] butoaneMenu = {btnAngajati, btnIngrediente, btnProduse, btnStock};
        for (Button btn : butoaneMenu) {
            btn.setPrefWidth(300);
            btn.setStyle(buttonStyle);
            btn.setOnMouseEntered(e -> btn.setStyle(buttonStyle + hoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(buttonStyle));
        }


        btnLogOut.setPrefWidth(300);
        btnLogOut.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #e74c3c; -fx-background-radius: 8px; -fx-padding: 15px 20px;");
        btnLogOut.setOnMouseEntered(e -> btnLogOut.setStyle("-fx-background-color: #c0392b; -fx-cursor: hand; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-radius: 8px; -fx-padding: 15px 20px;"));
        btnLogOut.setOnMouseExited(e -> btnLogOut.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #e74c3c; -fx-background-radius: 8px; -fx-padding: 15px 20px;"));


        VBox layout = new VBox(20, titlu, subtitlu, new Label(""), btnAngajati, btnIngrediente, btnProduse, btnStock, new Label(""), btnLogOut);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: #ecf0f1;");


        scenaManager = new Scene(layout, 1280, 720);


        btnAngajati.setOnAction(e -> {
            AngajatView view = new AngajatView();
            stage.setScene(view.creazaScenaAngajati(stage, scenaManager));
        });


        btnIngrediente.setOnAction(e -> {
            IngredienteView view = new IngredienteView();
            stage.setScene(view.creazaScena(stage, scenaManager));
        });

        btnProduse.setOnAction(e -> {
            ProdusView view = new ProdusView();
            stage.setScene(view.creazaScena(stage, scenaManager));
        });

        btnStock.setOnAction(e -> {
            StockView view = new StockView();
            stage.setScene(view.creazaScena(stage, scenaManager));
        });


        btnLogOut.setOnAction(e -> stage.setScene(scenaClient));

        return scenaManager;
    }
}