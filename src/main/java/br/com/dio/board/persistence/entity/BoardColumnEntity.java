package br.com.dio.board.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {

    private long id;
    private String name;
    private int order;
    private BoardColumnKind kind;
    private BoardEntity boardEntity = new BoardEntity();

}
