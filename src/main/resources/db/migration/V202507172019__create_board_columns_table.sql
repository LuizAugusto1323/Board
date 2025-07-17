CREATE TABLE board_columns(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    `order` int NOT NULL,
    kind VARCHAR(7),
    board_id BIGINT NOT NULL,
    CONSTRAINT boards__board_columns_fk FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE KEY unique_board_id_order (board_id, `order`)
) ENGINE=InnoDB default charset=utf8;

-- `order` é pq order é uma palavra reservada, se quisermos usa-lá, precisa ser desta maneira.
