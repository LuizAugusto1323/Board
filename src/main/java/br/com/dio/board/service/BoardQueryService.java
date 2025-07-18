package br.com.dio.board.service;

import br.com.dio.board.dto.BoardDetailsDTO;
import br.com.dio.board.persistence.dao.BoardColumnDAO;
import br.com.dio.board.persistence.dao.BoardDAO;
import br.com.dio.board.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> getById(final Long id) throws SQLException {
        var boardDao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        var optional = boardDao.getById(id);
        if (optional.isPresent()) {
            var entity = optional.get();
            entity.setBoardColumns(boardColumnDAO.getByBoardId(entity.getId()));
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException {
        var boardDao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        var optional = boardDao.getById(id);

        if (optional.isPresent()) {
            var entity = optional.get();
            var columns = boardColumnDAO.getByBoardIdWithDetails(entity.getId());
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
            return Optional.of(dto);
        }

        return Optional.empty();
    }

}
