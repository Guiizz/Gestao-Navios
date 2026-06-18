package com.gestaonavios.gestaonavios.DAL.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionManager {

    private static final Properties config = carregarConfig();

    // Lê a configuração da BD a partir de src/bd.properties
    private static Properties carregarConfig() {
        Properties props = new Properties();
        Path ficheiro = Path.of("src", "bd.properties");
        try (InputStream in = Files.newInputStream(ficheiro)) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível ler " + ficheiro.toAbsolutePath(), e);
        }
        return props;
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://" + config.getProperty("db.host")
                + ":" + config.getProperty("db.port")
                + ";databaseName=" + config.getProperty("db.name")
                + ";encrypt=false;trustServerCertificate=true"
                + ";loginTimeout=5";
        return DriverManager.getConnection(url,
                config.getProperty("db.user"), config.getProperty("db.password"));
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
