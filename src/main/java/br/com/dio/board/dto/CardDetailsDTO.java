package br.com.dio.board.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(
        Long id,
        boolean blocked,
        OffsetDateTime blockedAt,
        String blockDescription,
        int blockAmount,
        Long columnId,
        String columnName
) {
}
