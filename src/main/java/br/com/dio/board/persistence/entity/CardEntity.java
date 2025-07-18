package br.com.dio.board.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class CardEntity {

    private long id;
    private String title;
    private String description;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BoardColumnEntity boardColumnEntity;

}
