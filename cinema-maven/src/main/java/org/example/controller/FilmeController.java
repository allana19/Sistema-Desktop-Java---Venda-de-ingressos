package org.example.controller;

import org.example.dao.FilmeDAO;
import org.example.model.Filme;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class FilmeController extends BaseController {
    private final FilmeDAO dao = new FilmeDAO();

    public List<Filme> listar() throws SQLException {
        return dao.listarTodos();
    }

    public void salvar(Integer id, String titulo, String genero, String duracaoTxt, String classificacao)
            throws SQLException {
        titulo = exigirTexto(titulo, "o título do filme");
        genero = exigirTexto(genero, "o gênero do filme");
        int duracao = lerInteiro(duracaoTxt, "a duração (minutos)");
        if (duracao <= 0) throw new IllegalArgumentException("A duração deve ser maior que zero.");

        Filme f = new Filme(id == null ? 0 : id, titulo, genero, duracao, classificacao);
        if (id == null) dao.inserir(f); else dao.atualizar(f);
    }

    public void excluir(int id) throws SQLException {
        try {
            dao.excluir(id);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("Este filme possui sessões cadastradas. Exclua as sessões antes.");
        }
    }
}
