package br.com.dio.board.service;

import br.com.dio.board.persistence.dao.BoardDAO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        try {
            if (!dao.exist(id)) return false;
            dao.delete(id);
            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            return false;
        }
    }

}
