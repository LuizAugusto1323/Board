package br.com.dio.board.persistence.dao;

import br.com.dio.board.dto.CardDetailsDTO;
import br.com.dio.board.persistence.entity.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static br.com.dio.board.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public CardEntity insert(CardEntity cardEntity) throws SQLException {
        var sql = "INSERT INTO cards (title, description, board_columns_id) VALUES (?, ?, ?);";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, cardEntity.getTitle());
            statement.setString(i++, cardEntity.getDescription());
            statement.setLong(i, cardEntity.getBoardColumnEntity().getId());
            statement.executeUpdate();
            if (statement instanceof StatementImpl impl) {
                cardEntity.setId(impl.getLastInsertID());
            }
        }
        return cardEntity;
    }

    public Optional<CardDetailsDTO> getByID(final Long id) throws SQLException {
        var sql =
                """
                        SELECT c.id, c.title, c.description, c.board_columns_id, b.blocked_at, b.block_description, bc.name,
                        (
                            SELECT COUNT(sub_b.id)
                            FROM blocks sub_b
                            WHERE sub_b.card_id = c.id
                        ) block_amount
                        FROM cards c
                        LEFT JOIN blocks b ON b.card_id = c.id AND b.unblocked_at IS NULL
                        INNER JOIN board_columns bc ON bc.id = c.board_columns_id
                        WHERE c.id = ?;
                        """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                        resultSet.getLong("c.id"),
                        resultSet.getString("c.title"),
                        resultSet.getString("c.description"),
                        nonNull(resultSet.getString("b.block_description")),
                        toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")),
                        resultSet.getString("b.block_description"),
                        resultSet.getInt("block_amount"),
                        resultSet.getLong("c.board_columns_id"),
                        resultSet.getString("bc.name")
                );
                return Optional.of(dto);
            }
        }

        return Optional.empty();
    }

}
