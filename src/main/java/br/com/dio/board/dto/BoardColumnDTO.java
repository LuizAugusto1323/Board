package br.com.dio.board.dto;

import br.com.dio.board.persistence.entity.BoardColumnKind;

public record BoardColumnDTO(
        Long id,
        String name,
        BoardColumnKind kind,
        int cardsAmount
) {}
