package br.com.dio.board.dto;

import br.com.dio.board.persistence.entity.BoardColumnKind;

public record BoardColumnInfoDTO(long id, int order, BoardColumnKind kind) { }
