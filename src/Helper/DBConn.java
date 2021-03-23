package Helper;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    public static final String CONSOLE_RED = "\u001B[31m";
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_RESET = "\u001B[0m";
    public static Connection connection(String dbNameNoExtension){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:databases/" + dbNameNoExtension + ".db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(CONSOLE_RED+e+CONSOLE_RESET);
        }
        return conn;
    }
}
