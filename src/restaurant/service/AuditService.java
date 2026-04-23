package restaurant.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    //daca nu exista fisierul se va crea automat
    private final String FILE_PATH = "audit.csv";

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logAction(String numeActiune) {
        // Folosim try-with-resources pt a inchide automat fisierul
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println(numeActiune + "," + timestamp);

        } catch (IOException e) {
            System.err.println("Eroare la scrierea in audit: " + e.getMessage());
        }
    }
}