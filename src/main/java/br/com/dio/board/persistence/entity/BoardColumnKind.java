package br.com.dio.board.persistence.entity;

import java.util.stream.Stream;

public enum BoardColumnKind {
    INITIAL, FINAL, CANCEL, PENDING;

    public static BoardColumnKind toKind(final String name) {
        return Stream
                .of(BoardColumnKind.values())
                .filter(b -> b.name().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
