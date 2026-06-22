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

    // Liga os parâmetros (?) de forma segura — evita injeção SQL e problemas com aspas/datas
    private static void bind(PreparedStatement stmt, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    public static <T> List<T> select(String sql, RowMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            bind(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static int create(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            bind(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            // 547 = violação de constraint (FK/CHECK) no SQL Server
            if (e.getErrorCode() == 547) {
                throw new RuntimeException(
                        "Não é possível concluir a operação porque o registo está associado a outros dados.", e);
            }
            throw new RuntimeException("Erro na base de dados: " + e.getMessage(), e);
        }
    }
}
