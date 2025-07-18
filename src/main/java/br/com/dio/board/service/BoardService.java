package br.com.dio.board.service;

import br.com.dio.board.persistence.dao.BoardColumnDAO;
import br.com.dio.board.persistence.dao.BoardDAO;
import br.com.dio.board.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    public BoardEntity insert(BoardEntity entity) throws SQLException {
        var boardDao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);

        try {
            boardDao.insert(entity);
            var columns = entity
                    .getBoardColumns()
                    .stream()
                    .peek(c -> c.setBoardEntity(entity))
                    .toList();

            for (var column : columns) {
                boardColumnDao.insert(column);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        return entity;
    }

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
