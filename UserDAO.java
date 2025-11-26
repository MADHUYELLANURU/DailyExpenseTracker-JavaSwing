import java.sql.*;

public class UserDAO {

    // Register new user
    public static boolean register(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password); // later: store hash instead
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            // username already exists etc.
            System.err.println("Register error: " + e.getMessage());
            return false;
        }
    }

    // Validate login and return user id, or -1 if invalid
    public static int validateLogin(String username, String password) {
        String sql = "SELECT id, password FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String dbPass = rs.getString("password");
                if (dbPass.equals(password)) {  // later: check hash
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return -1;
    }

    // Update last_login to NOW
    public static void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = datetime('now') WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Update last login error: " + e.getMessage());
        }
    }

    // Get last_login as String
    public static String getLastLogin(int userId) {
        String sql = "SELECT last_login FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("last_login");
            }
        } catch (SQLException e) {
            System.err.println("Get last login error: " + e.getMessage());
        }
        return null;
    }
}
