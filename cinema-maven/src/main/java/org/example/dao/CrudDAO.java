package org.example.dao;

import java.sql.SQLException;
import java.util.List;

/** Contrato comum de CRUD para todos os DAOs */
public interface CrudDAO<T, ID> {
    void inserir(T entidade) throws SQLException;
    void atualizar(T entidade) throws SQLException;
    void excluir(ID id) throws SQLException;
    T buscarPorId(ID id) throws SQLException;
    List<T> listarTodos() throws SQLException;
}
