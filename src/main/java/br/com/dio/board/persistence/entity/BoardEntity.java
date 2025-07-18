package br.com.dio.board.persistence.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardEntity {

    private long id;
    private String name;
    private List<BoardColumnEntity> boardColumns = new ArrayList<>();

}
