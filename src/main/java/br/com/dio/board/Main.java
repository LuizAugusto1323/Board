package br.com.dio.board;

import br.com.dio.board.persistence.migration.MigrationStrategy;
import br.com.dio.board.ui.MainMenu;

import java.sql.SQLException;

import static br.com.dio.board.persistence.config.ConnectionConfig.getConnection;

public class Main {
    public static void main(String[] args) {
       try(var connection = getConnection()) {
           new MigrationStrategy(connection).executeMigration();
           new MainMenu().execute();
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }
}
