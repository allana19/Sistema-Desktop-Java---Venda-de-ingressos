package org.example.controller;

import org.example.dao.SalaDAO;
import org.example.model.Sala;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class SalaController extends BaseController {
    private final SalaDAO dao = new SalaDAO();

    public List<Sala> listar() throws SQLException {
        return dao.listarTodos();
    }

    /** Cria a sala e gera os assentos automaticamente. */
    public void criar(String nome, String capacidadeTxt, String porFileiraTxt) throws SQLException {
        nome = exigirTexto(nome, "o nome da sala");
        int capacidade = lerInteiro(capacidadeTxt, "a capacidade");
        int porFileira = lerInteiro(porFileiraTxt, "o número de assentos por fileira");
        if (capacidade <= 0 || porFileira <= 0)
            throw new IllegalArgumentException("Capacidade e assentos por fileira devem ser maiores que zero.");
        if (capacidade > porFileira * 26)
            throw new IllegalArgumentException("Limite de 26 fileiras (A–Z) excedido. Aumente os assentos por fileira.");
        try {
            dao.inserirComAssentos(new Sala(0, nome, capacidade), porFileira);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Já existe uma sala com esse nome.");
        }
    }

    public void renomear(int id, String nome) throws SQLException {
        nome = exigirTexto(nome, "o nome da sala");
        Sala sala = dao.buscarPorId(id);
        if (sala == null) throw new IllegalArgumentException("Sala não encontrada.");
        sala.setNome(nome);
        try {
            dao.atualizar(sala);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Já existe uma sala com esse nome.");
        }
    }

    public void excluir(int id) throws SQLException {
        try {
            dao.excluir(id);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Esta sala possui sessões cadastradas. Exclua as sessões antes.");
        }
    }
}
