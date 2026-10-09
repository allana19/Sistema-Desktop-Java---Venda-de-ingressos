package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexaoFactory {
    private static final Properties PROPS = carregar();

    private ConexaoFactory() {}

    private static Properties carregar() {
        Properties p = new Properties();
        try (InputStream in = ConexaoFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Arquivo db.properties não encontrado em src/main/resources.");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler o db.properties.", e);
        }
        return p;
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }
}
