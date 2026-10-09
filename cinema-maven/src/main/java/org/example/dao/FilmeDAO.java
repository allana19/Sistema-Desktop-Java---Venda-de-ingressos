package org.example.dao;

import org.example.model.Filme;
import org.example.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO implements CrudDAO<Filme, Integer> {
    private static final String SELECT =
            "SELECT id_filme, titulo, genero, duracao_min, classificacao FROM filme";

    @Override
    public void inserir(Filme f) throws SQLException {
        String sql = "INSERT INTO filme (titulo, genero, duracao_min, classificacao) VALUES (?, ?, ?, ?)";
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getTitulo());
            ps.setString(2, f.getGenero());
            ps.setInt(3, f.getDuracaoMin());
            ps.setString(4, f.getClassificacao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) f.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void atualizar(Filme f) throws SQLException {
        String sql = "UPDATE filme SET titulo = ?, genero = ?, duracao_min = ?, classificacao = ? WHERE id_filme = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, f.getTitulo());
            ps.setString(2, f.getGenero());
            ps.setInt(3, f.getDuracaoMin());
            ps.setString(4, f.getClassificacao());
            ps.setInt(5, f.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM filme WHERE id_filme = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Filme buscarPorId(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " WHERE id_filme = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Filme> listarTodos() throws SQLException {
        List<Filme> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " ORDER BY titulo");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Filme mapear(ResultSet rs) throws SQLException {
        return new Filme(rs.getInt("id_filme"), rs.getString("titulo"), rs.getString("genero"),
                rs.getInt("duracao_min"), rs.getString("classificacao"));
    }
}
