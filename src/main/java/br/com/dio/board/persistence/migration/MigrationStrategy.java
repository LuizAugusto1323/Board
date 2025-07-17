package br.com.dio.board.persistence.migration;

import lombok.AllArgsConstructor;
import org.flywaydb.core.Flyway;

import java.sql.Connection;

@AllArgsConstructor
public class MigrationStrategy {

    private final Connection connection;

    public void executeMigration() {
        var url = "jdbc:mysql://localhost:3306/board";
        var user = "root";
        var password = "board123";

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .load();

        flyway.migrate();
    }

}
