package game.database;

// Standard DB connector for MySQL

import java.sql.*;

public class Connector {

    private static Connector instance = new Connector();
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost/connectfour?connectionTimeout=3000&socketTimeout=3000&useSSL=false";
    private static String USER = "application";
    private static String PASS = "application";

    private Connector() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static Connection getConnection() throws SQLException {
        return instance.createConnection();
    }

}
