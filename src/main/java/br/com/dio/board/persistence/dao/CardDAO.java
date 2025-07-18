package br.com.dio.board.persistence.dao;

import br.com.dio.board.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

import java.sql.Connection;

@AllArgsConstructor
public class CardDAO {

    private final Connection connection;

    public CardDetailsDTO getByID(final Long id) {
        return null;
    }

}
