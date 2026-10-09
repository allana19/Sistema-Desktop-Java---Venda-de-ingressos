package org.example.controller;

import org.example.dao.IngressoDAO;
import org.example.dao.SessaoDAO;
import org.example.model.Filme;
import org.example.model.Sala;
import org.example.model.Sessao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

public class SessaoController extends BaseController {
    private final SessaoDAO dao = new SessaoDAO();
    private final IngressoDAO ingressoDAO = new IngressoDAO();

    public List<Sessao> listar() throws SQLException {
        return dao.listarTodos();
    }

    public void salvar(Integer id, Filme filme, Sala sala, String dataHoraTxt, String precoTxt) throws SQLException {
        if (filme == null) throw new IllegalArgumentException("Selecione o filme (cadastre um na aba Filmes).");
        if (sala == null) throw new IllegalArgumentException("Selecione a sala (cadastre uma na aba Salas).");
        LocalDateTime dataHora = lerDataHora(dataHoraTxt);
        BigDecimal preco = lerDecimal(precoTxt, "o preço");
        if (preco.signum() < 0) throw new IllegalArgumentException("O preço não pode ser negativo.");

        Sessao s = new Sessao(id == null ? 0 : id, filme, sala, dataHora, preco);
        try {
            if (id == null) {
                dao.inserir(s);
            } else {
                // trocar de sala com ingressos vendidos deixaria ingressos apontando para assentos de outra sala
                Sessao atual = dao.buscarPorId(id);
                if (atual != null && atual.getSala().getId() != sala.getId()
                        && ingressoDAO.contarPorSessao(id) > 0) {
                    throw new IllegalArgumentException(
                            "Não é possível trocar a sala de uma sessão que já tem ingressos vendidos.");
                }
                dao.atualizar(s);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Já existe uma sessão nessa sala nesse mesmo horário.");
        }
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }
}
