package DAL.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {

    private static final Dotenv dotenv = Dotenv.configure().directory("src").load();

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://" + dotenv.get("DB_SERVER")
                + ";databaseName=" + dotenv.get("DB_DATABASE")
                + ";encrypt=false;trustServerCertificate=true"
                + ";loginTimeout=5";
        return DriverManager.getConnection(url, dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"));
    }

    public static <T> List<T> select(String sql, RowMapper<T> mapper) {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapper.map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void create(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
