package br.com.dio.board.persistence.entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlockEntity {

    private long id;
    private OffsetDateTime blockedAt;
    private String blockDescription;
    private OffsetDateTime unblockedAt;
    private String unblockedDescription;

}
