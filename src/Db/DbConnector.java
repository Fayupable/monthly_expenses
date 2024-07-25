package Db;

import Db.Exception.DbConnectException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DbConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/monthly_expense?useSSL=false&serverTimezone=UTC";
    private static final String USER="root";
    private static final String PASSWORD="12345678";
    private static Connection conn;



    public static Connection getConnection() throws DbConnectException {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Logger.getLogger(DbConnector.class.getName()).info("Connection established");
            return conn;
        } catch (SQLException e) {
            Logger.getLogger(DbConnector.class.getName()).severe("Connection failed: " + e.getMessage());
            throw new DbConnectException("Failed to establish database connection.", e);
        }
    }
}
