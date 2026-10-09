package org.example.dao;

import org.example.model.Assento;
import org.example.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssentoDAO implements CrudDAO<Assento, Integer> {
    private static final String SELECT = "SELECT id_assento, id_sala, fileira, numero FROM assento";

    @Override
    public void inserir(Assento a) throws SQLException {
        String sql = "INSERT INTO assento (id_sala, fileira, numero) VALUES (?, ?, ?)";
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getIdSala());
            ps.setString(2, a.getFileira());
            ps.setInt(3, a.getNumero());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void atualizar(Assento a) throws SQLException {
        String sql = "UPDATE assento SET id_sala = ?, fileira = ?, numero = ? WHERE id_assento = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, a.getIdSala());
            ps.setString(2, a.getFileira());
            ps.setInt(3, a.getNumero());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM assento WHERE id_assento = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Assento buscarPorId(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " WHERE id_assento = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Assento> listarTodos() throws SQLException {
        return consultar(SELECT + " ORDER BY id_sala, fileira, numero", null);
    }

    public List<Assento> listarPorSala(int idSala) throws SQLException {
        return consultar(SELECT + " WHERE id_sala = ? ORDER BY fileira, numero", idSala);
    }

    private List<Assento> consultar(String sql, Integer idSala) throws SQLException {
        List<Assento> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (idSala != null) ps.setInt(1, idSala);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Assento mapear(ResultSet rs) throws SQLException {
        return new Assento(rs.getInt("id_assento"), rs.getInt("id_sala"),
                rs.getString("fileira"), rs.getInt("numero"));
    }
}
