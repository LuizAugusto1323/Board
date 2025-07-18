package br.com.dio.board.service;

import br.com.dio.board.persistence.dao.BoardColumnDAO;
import br.com.dio.board.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;

    public Optional<BoardColumnEntity> getById(final Long id) throws SQLException {
        var dao = new BoardColumnDAO(connection);
        return dao.getById(id);
    }

}
