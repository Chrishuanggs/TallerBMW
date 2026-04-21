package util;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/taller_bmw";
    private static final String USER = "root";
    private static final String PASS = "Derek123";

    static {
        com.bmwcomponents.db.DBConnection.configure(URL, USER, PASS);
    }

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return com.bmwcomponents.db.DBConnection.getConnection();
    }
}
