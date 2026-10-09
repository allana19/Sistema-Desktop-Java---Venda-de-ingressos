package org.example.dao;

import org.example.model.Filme;
import org.example.model.Sala;
import org.example.model.Sessao;
import org.example.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessaoDAO implements CrudDAO<Sessao, Integer> {
    private static final String SELECT =
            "SELECT s.id_sessao, s.data_hora, s.preco, "
          + "       f.id_filme, f.titulo, f.genero, f.duracao_min, f.classificacao, "
          + "       sa.id_sala, sa.nome, sa.capacidade "
          + "  FROM sessao s "
          + "  JOIN filme f  ON f.id_filme = s.id_filme "
          + "  JOIN sala  sa ON sa.id_sala = s.id_sala";

    @Override
    public void inserir(Sessao s) throws SQLException {
        String sql = "INSERT INTO sessao (id_filme, id_sala, data_hora, preco) VALUES (?, ?, ?, ?)";
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getFilme().getId());
            ps.setInt(2, s.getSala().getId());
            ps.setTimestamp(3, Timestamp.valueOf(s.getDataHora()));
            ps.setBigDecimal(4, s.getPreco());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void atualizar(Sessao s) throws SQLException {
        String sql = "UPDATE sessao SET id_filme = ?, id_sala = ?, data_hora = ?, preco = ? WHERE id_sessao = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getFilme().getId());
            ps.setInt(2, s.getSala().getId());
            ps.setTimestamp(3, Timestamp.valueOf(s.getDataHora()));
            ps.setBigDecimal(4, s.getPreco());
            ps.setInt(5, s.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM sessao WHERE id_sessao = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();   // os ingressos saem junto (ON DELETE CASCADE)
        }
    }

    @Override
    public Sessao buscarPorId(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " WHERE s.id_sessao = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Sessao> listarTodos() throws SQLException {
        List<Sessao> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " ORDER BY s.data_hora");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Sessao mapear(ResultSet rs) throws SQLException {
        Filme f = new Filme(rs.getInt("id_filme"), rs.getString("titulo"), rs.getString("genero"),
                rs.getInt("duracao_min"), rs.getString("classificacao"));
        Sala sa = new Sala(rs.getInt("id_sala"), rs.getString("nome"), rs.getInt("capacidade"));
        return new Sessao(rs.getInt("id_sessao"), f, sa,
                rs.getTimestamp("data_hora").toLocalDateTime(), rs.getBigDecimal("preco"));
    }
}
