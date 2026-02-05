package com.clinica.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/clinica";
    private static final String USER = "postgres";
    private static final String PASS = "30082006";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado", e);
        }
    }
}