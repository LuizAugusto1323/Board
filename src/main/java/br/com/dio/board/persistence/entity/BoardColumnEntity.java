package br.com.dio.board.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardColumnEntity {

    private long id;
    private String name;
    private int order;
    private BoardColumnKind kind;
    private BoardEntity boardEntity = new BoardEntity();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CardEntity> cardEntities = new ArrayList<>();

}
