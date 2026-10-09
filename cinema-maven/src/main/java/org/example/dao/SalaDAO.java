package org.example.dao;

import org.example.model.Sala;
import org.example.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO implements CrudDAO<Sala, Integer> {
    private static final String SELECT = "SELECT id_sala, nome, capacidade FROM sala";

    @Override
    public void inserir(Sala s) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO sala (nome, capacidade) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getNome());
            ps.setInt(2, s.getCapacidade());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getInt(1));
            }
        }
    }


    public void inserirComAssentos(Sala s, int assentosPorFileira) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao()) {
            c.setAutoCommit(false);
            try {
                int idSala;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO sala (nome, capacidade) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, s.getNome());
                    ps.setInt(2, s.getCapacidade());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        idSala = rs.getInt(1);
                    }
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO assento (id_sala, fileira, numero) VALUES (?, ?, ?)")) {
                    for (int i = 0; i < s.getCapacidade(); i++) {
                        char fileira = (char) ('A' + i / assentosPorFileira);
                        ps.setInt(1, idSala);
                        ps.setString(2, String.valueOf(fileira));
                        ps.setInt(3, i % assentosPorFileira + 1);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                c.commit();
                s.setId(idSala);
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    @Override
    public void atualizar(Sala s) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("UPDATE sala SET nome = ? WHERE id_sala = ?")) {
            ps.setString(1, s.getNome());
            ps.setInt(2, s.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM sala WHERE id_sala = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();   // os assentos saem junto (ON DELETE CASCADE)
        }
    }

    @Override
    public Sala buscarPorId(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " WHERE id_sala = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Sala> listarTodos() throws SQLException {
        List<Sala> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Sala mapear(ResultSet rs) throws SQLException {
        return new Sala(rs.getInt("id_sala"), rs.getString("nome"), rs.getInt("capacidade"));
    }
}
