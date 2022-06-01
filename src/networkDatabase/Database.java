package networkDatabase;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database implements Serializable {
    private static final String URL =
            "jdbc:postgresql://localhost:5432/SocialNetwork";
    private static Connection connection = null;

    private Database() {
    }

    public static Connection getConnection() {
        createConnection();
        return connection;
    }

    private static void createConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

