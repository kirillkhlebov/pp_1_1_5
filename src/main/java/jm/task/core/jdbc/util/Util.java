package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
//        String dbName = "my_database";
//        String dbUserName = "root";
//        String dbPassword = "";
//        String connectionString = "jdbc:mysql://localhost/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8";
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/usersdatabase", "root", "12345");
        return connection;
    }
}
