package org.example.controller;

import org.example.dao.IngressoDAO;
import org.example.model.Assento;
import org.example.model.FaturamentoFilme;
import org.example.model.Ingresso;
import org.example.model.Sessao;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Set;


public class IngressoController extends BaseController {
    private final IngressoDAO dao = new IngressoDAO();

    public Set<Integer> assentosOcupados(Sessao sessao) throws SQLException {
        return dao.assentosOcupados(sessao.getId());
    }

    public void vender(Sessao sessao, Assento assento) throws SQLException {
        Ingresso i = new Ingresso(0, sessao.getId(), assento.getId(), sessao.getPreco(), null);
        try {
            dao.inserir(i);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Esse assento acabou de ser vendido para esta sessão.");
        }
    }

    public void cancelar(Sessao sessao, Assento assento) throws SQLException {
        dao.cancelar(sessao.getId(), assento.getId());
    }

    public List<FaturamentoFilme> faturamentoPorFilme() throws SQLException {
        return dao.faturamentoPorFilme();
    }
}
