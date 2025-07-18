package br.com.dio.board.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.dio.board.persistence.converter.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void block(final String description, final long cardId) throws SQLException {
        var sql = "INSERT INTO blocks (blocked_at, block_description, card_id) VALUES (?, ?, ?)";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setTimestamp(i++, toTimestamp(OffsetDateTime.now()));
            statement.setString(i++, description);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        var sql = "UPDATE blocks SET unblocked_at = ?, unblock_description = ? WHERE card_id = ? AND unblock_description IS NULL;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setTimestamp(i++, toTimestamp(OffsetDateTime.now()));
            statement.setString(i++, reason);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

}
