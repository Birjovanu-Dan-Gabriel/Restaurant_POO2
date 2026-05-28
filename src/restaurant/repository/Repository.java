package restaurant.repository;

import restaurant.config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Repository<T> {
    protected Connection getContext() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    // metodele abstracte implementate pt fiecare obiect
    public abstract void insert(T object);
    public abstract T getById(int id);
    public abstract List<T> getAll();
    public abstract void update(T object);
    public abstract void delete(int id);
}