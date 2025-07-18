package br.com.dio.board.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Data
public class BoardEntity {

    private long id;
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardColumns = new ArrayList<>();


    public BoardColumnEntity getInitialColumn() {
        return getFilteredColumn(bc -> bc.getKind().equals(BoardColumnKind.INITIAL));
    }

    public BoardColumnEntity getCancelColumn() {
        return getFilteredColumn(bc -> bc.getKind().equals(BoardColumnKind.CANCEL));
    }

    private BoardColumnEntity getFilteredColumn(Predicate<BoardColumnEntity> predicate) {
        return boardColumns.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow();
    }

}
