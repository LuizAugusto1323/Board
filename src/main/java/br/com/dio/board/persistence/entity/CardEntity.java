package br.com.dio.board.persistence.entity;

import lombok.Data;

@Data
public class CardEntity {

    private long id;
    private String title;
    private String description;

}
