package br.com.dio.board.service;

import br.com.dio.board.dto.BoardColumnInfoDTO;
import br.com.dio.board.exceptions.CardBlockedException;
import br.com.dio.board.exceptions.CardFinishedException;
import br.com.dio.board.exceptions.EntityNotFoundException;
import br.com.dio.board.persistence.dao.BlockDAO;
import br.com.dio.board.persistence.dao.CardDAO;
import br.com.dio.board.persistence.entity.BoardColumnKind;
import br.com.dio.board.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            dao.insert(entity);
            connection.commit();
            return entity;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void moveToNextColumn(final long cardId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.getByID(cardId);
            var dto = optional.orElseThrow(() -> new EntityNotFoundException("O CARD com ID " + cardId + " nao foi encontrado"));
            if (dto.blocked())
                throw new CardBlockedException("O CARD com ID " + cardId + " esta bloqueado, precisa desbloquear para move-lo");

            var currentColumn = boardColumnsInfo
                    .stream()
                    .filter(bc -> bc.id() == dto.columnId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O CARD informado pertence a outro BOARD"));

            if (currentColumn.kind().equals(BoardColumnKind.FINAL))
                throw new CardFinishedException("O CARD ja foi finalizado!");

            var nextColumn = boardColumnsInfo
                    .stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O CARD esta cancelado"));

            dao.moveToNextColumn(nextColumn.id(), cardId);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void cancel(final long cardId, final long cancelColumnId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.getByID(cardId);
            var dto = optional.orElseThrow(() -> new EntityNotFoundException("O CARD com ID " + cardId + " nao foi encontrado"));

            if (dto.blocked())
                throw new CardBlockedException("O CARD com ID " + cardId + " esta bloqueado, precisa desbloquear para cancelar");

            var currentColumn = boardColumnsInfo
                    .stream()
                    .filter(bc -> bc.id() == dto.columnId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O CARD informado pertence a outro BOARD"));

            if (currentColumn.kind().equals(BoardColumnKind.FINAL))
                throw new CardFinishedException("O CARD ja foi finalizado!");

            boardColumnsInfo
                    .stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O CARD esta cancelado"));

            dao.moveToNextColumn(cancelColumnId, cardId);
            connection.commit();

        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void block(final long cardId, final String description, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var blockDao = new BlockDAO(connection);
            var optional = dao.getByID(cardId);
            var dto = optional.orElseThrow(() -> new EntityNotFoundException("O CARD com ID " + cardId + " nao foi encontrado"));

            if (dto.blocked())
                throw new CardBlockedException("O CARD com ID " + cardId + " ja esta bloqueado!");

            var currentColumn = boardColumnsInfo.stream().filter(bc -> bc.id() == dto.columnId()).findFirst().orElseThrow();

            if (currentColumn.kind().equals(BoardColumnKind.FINAL) || currentColumn.kind().equals(BoardColumnKind.CANCEL)) {
                var message = "O CARD informado esta em uma coluna do tipo %s".formatted(currentColumn.kind());
                throw new IllegalStateException(message);
            }

            blockDao.block(description, cardId);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

}
