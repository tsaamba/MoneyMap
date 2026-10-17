
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/Expense_Tracker";

    public static Connection getConnection() throws SQLException {

        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        if (username == null || password == null) {
            throw new IllegalStateException(
                    "Please configure DB_USERNAME and DB_PASSWORD"
            );
        }

        return DriverManager.getConnection(URL, username, password);
    }
}