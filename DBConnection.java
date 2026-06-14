import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ticket_db?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("MySQL Connector/J driver not found. Add mysql-connector-j-9.7.0.jar to classpath.", ex);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
