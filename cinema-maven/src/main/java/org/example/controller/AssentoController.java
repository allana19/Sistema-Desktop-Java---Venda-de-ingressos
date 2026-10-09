package org.example.controller;

import org.example.dao.AssentoDAO;
import org.example.model.Assento;
import org.example.model.Sala;

import java.sql.SQLException;
import java.util.List;

public class AssentoController extends BaseController {
    private final AssentoDAO dao = new AssentoDAO();

    public List<Assento> listarPorSala(Sala sala) throws SQLException {
        return dao.listarPorSala(sala.getId());
    }

    public List<Assento> listar() throws SQLException {
        return dao.listarTodos();
    }
}
