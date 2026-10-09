package org.example.dao;

import org.example.model.FaturamentoFilme;
import org.example.model.Ingresso;
import org.example.util.ConexaoFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IngressoDAO implements CrudDAO<Ingresso, Integer> {
    private static final String SELECT =
            "SELECT id_ingresso, id_sessao, id_assento, valor_pago, data_venda FROM ingresso";

    @Override
    public void inserir(Ingresso i) throws SQLException {
        String sql = "INSERT INTO ingresso (id_sessao, id_assento, valor_pago) VALUES (?, ?, ?)";
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getIdSessao());
            ps.setInt(2, i.getIdAssento());
            ps.setBigDecimal(3, i.getValorPago());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) i.setId(rs.getInt(1));
            }
        }
    }

    @Override
    public void atualizar(Ingresso i) throws SQLException {
        String sql = "UPDATE ingresso SET id_sessao = ?, id_assento = ?, valor_pago = ? WHERE id_ingresso = ?";
        try (Connection c = ConexaoFactory.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, i.getIdSessao());
            ps.setInt(2, i.getIdAssento());
            ps.setBigDecimal(3, i.getValorPago());
            ps.setInt(4, i.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void excluir(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("DELETE FROM ingresso WHERE id_ingresso = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Ingresso buscarPorId(Integer id) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " WHERE id_ingresso = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Ingresso> listarTodos() throws SQLException {
        List<Ingresso> lista = new ArrayList<>();
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT + " ORDER BY data_venda");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void cancelar(int idSessao, int idAssento) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM ingresso WHERE id_sessao = ? AND id_assento = ?")) {
            ps.setInt(1, idSessao);
            ps.setInt(2, idAssento);
            ps.executeUpdate();
        }
    }

    /** IDs dos assentos já vendidos na sessão. */
    public Set<Integer> assentosOcupados(int idSessao) throws SQLException {
        Set<Integer> ids = new HashSet<>();
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT id_assento FROM ingresso WHERE id_sessao = ?")) {
            ps.setInt(1, idSessao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
        }
        return ids;
    }

    public int contarPorSessao(int idSessao) throws SQLException {
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM ingresso WHERE id_sessao = ?")) {
            ps.setInt(1, idSessao);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public List<FaturamentoFilme> faturamentoPorFilme() throws SQLException {
        List<FaturamentoFilme> linhas = new ArrayList<>();
        String sql = "SELECT f.titulo, COUNT(i.id_ingresso) AS ingressos, COALESCE(SUM(i.valor_pago), 0) AS total "
                   + "  FROM filme f "
                   + "  LEFT JOIN sessao s ON s.id_filme = f.id_filme "
                   + "  LEFT JOIN ingresso i ON i.id_sessao = s.id_sessao "
                   + " GROUP BY f.id_filme, f.titulo "
                   + " ORDER BY total DESC, f.titulo";
        try (Connection c = ConexaoFactory.getConexao();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                linhas.add(new FaturamentoFilme(rs.getString("titulo"), rs.getInt("ingressos"),
                        rs.getBigDecimal("total")));
            }
        }
        return linhas;
    }

    private Ingresso mapear(ResultSet rs) throws SQLException {
        return new Ingresso(rs.getInt("id_ingresso"), rs.getInt("id_sessao"), rs.getInt("id_assento"),
                rs.getBigDecimal("valor_pago"), rs.getTimestamp("data_venda").toLocalDateTime());
    }
}
