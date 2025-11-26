import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {

    public static void init() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "username TEXT UNIQUE NOT NULL," +
                         "password TEXT NOT NULL," +      // for now plain text
                         "created_at TEXT DEFAULT CURRENT_TIMESTAMP," +
                         "last_login TEXT" +
                         ");";
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
