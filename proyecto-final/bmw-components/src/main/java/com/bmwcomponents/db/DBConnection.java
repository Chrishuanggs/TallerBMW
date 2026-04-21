package com.bmwcomponents.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String url;
    private static String user;
    private static String password;

    private DBConnection() {}

    public static void configure(String url, String user, String password) {
        DBConnection.url = url;
        DBConnection.user = user;
        DBConnection.password = password;
    }

    public static Connection getConnection() throws SQLException {
        if (url == null)
            throw new IllegalStateException("DBConnection no configurado. Llama configure() al iniciar.");
        return DriverManager.getConnection(url, user, password);
    }
}
