package restaurant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//  Functie de conectare la baza de date


public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection connection;

    //TODO de facut local env si pus datele acolo
    private final String url = "jdbc:mysql://localhost:3306/restaurant_db";
    private final String user = "root";
    private final String pass = "ParolaTa123!"; // Parola setată în terminal

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driverul MySQL nu a fost găsit: " + e.getMessage());
        }
    }


    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}