package br.com.dio.board.service;

import br.com.dio.board.dto.CardDetailsDTO;
import br.com.dio.board.persistence.dao.CardDAO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection;

    public Optional<CardDetailsDTO> getById(final Long id) throws SQLException {
        var dao = new CardDAO(connection);
        return dao.getByID(id);
    }

}
