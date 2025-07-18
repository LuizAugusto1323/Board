CREATE TABLE cards(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    board_columns_id BIGINT NOT NULL,
    CONSTRAINT board_columns__cards_fk FOREIGN KEY (board_columns_id) REFERENCES board_columns(id) ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8;
