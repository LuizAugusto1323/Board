package br.com.dio.board.persistence.dao;

import br.com.dio.board.dto.BoardColumnDTO;
import br.com.dio.board.persistence.entity.BoardColumnEntity;
import br.com.dio.board.persistence.entity.BoardColumnKind;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(BoardColumnEntity entity) throws SQLException {
        var sql = "INSERT INTO board_columns (name, `order`, kind, board_id) VALUES (?, ?, ?, ?);";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getName());
            statement.setInt(i++, entity.getOrder());
            statement.setString(i++, entity.getKind().name());
            statement.setLong(i++, entity.getBoardEntity().getId());
            statement.executeUpdate();
            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
            return entity;
        }
    }

    public List<BoardColumnEntity> getByBoardId(long id) throws SQLException {
        var sql = "SELECT * FROM board_columns WHERE board_id = ? ORDER BY `order`;";
        List<BoardColumnEntity> entities = new ArrayList<>();

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                BoardColumnEntity entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(BoardColumnKind.toKind(resultSet.getString("kind")));
                entities.add(entity);
            }
        }
        return entities;
    }

    public List<BoardColumnDTO> getByBoardIdWithDetails(long id) throws SQLException {
        var sql = """
                  SELECT bc.id, bc.name, bc.kind,
                         COUNT(
                                SELECT c.id
                                FROM c
                                WHERE c.board_columns_id = bc.id
                         ) cards_amount
                  FROM board_columns bc
                  WHERE board_id = ?
                  ORDER BY `order`;
                """;

        List<BoardColumnDTO> dtos = new ArrayList<>();

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                var dto = new BoardColumnDTO(
                        resultSet.getLong("bc.id"),
                        resultSet.getString("bc.name"),
                        BoardColumnKind.toKind(resultSet.getString("bc.kind")),
                        resultSet.getInt("cards_amount")
                );

                dtos.add(dto);
            }
        }
        return dtos;
    }
}
