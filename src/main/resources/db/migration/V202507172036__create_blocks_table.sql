CREATE TABLE blocks(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    block_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    block_description VARCHAR(255) NOT NULL,
    unblock_at TIMESTAMP NULL,
    unblock_description VARCHAR(255) NOT NULL,
    card_id BIGINT NOT NULL,
    CONSTRAINT cards__blocks_fk FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
) ENGINE=InnoDB default charset=utf8;

-- `order` é pq order é uma palavra reservada, se quisermos usa-lá, precisa ser desta maneira.
