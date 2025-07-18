package br.com.dio.board.ui;

import br.com.dio.board.persistence.entity.BoardColumnEntity;
import br.com.dio.board.persistence.entity.BoardColumnKind;
import br.com.dio.board.persistence.entity.BoardEntity;
import br.com.dio.board.service.BoardQueryService;
import br.com.dio.board.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static br.com.dio.board.persistence.config.ConnectionConfig.getConnection;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void execute() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de BOARDS, escolha a opcao desejada:");

        int option = -1;

        while (true) {
            System.out.println("1 - Criar um novo BOARD");
            System.out.println("2 - Selecionar um BOARD existente");
            System.out.println("3 - Excluir um BOARD");
            System.out.println("4 - Sair");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor informe um numero valido:");
            }

            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opcao invalida, informe outra opcao do menu.");
            }
        }
    }

    private void createBoard() throws SQLException {
        var entity = new BoardEntity();
        System.out.println("Informe o nome do seu BOARD:");
        entity.setName(scanner.nextLine());
        System.out.println("Seu board tera colunas alem das 3 padroes? se sim informe quantas, se nao digite 0:");
        var additionalColumns = scanner.nextInt();
        scanner.nextLine();
        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do seu BOARD:");
        var initialColumnName = scanner.nextLine();
        var initialColumn = createColumn(initialColumnName, BoardColumnKind.INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendende:");
            var pendingColumnName = scanner.nextLine();
            var pendingColumn = createColumn(pendingColumnName, BoardColumnKind.PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final:");
        var finalColumnName = scanner.nextLine();
        var finalColumn = createColumn(finalColumnName, BoardColumnKind.FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do BOARD:");
        var cancelColumnName = scanner.nextLine();
        var cancelColumn = createColumn(cancelColumnName, BoardColumnKind.CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            service.insert(entity);
            System.out.println("BOARD criado com sucesso!");
        }
    }

    private void selectBoard() throws SQLException {
        System.out.println("Informe o ID do BOARD que deseja selecionar:");
        var id = scanner.nextLong();
        scanner.nextLine();
        try (var connection = getConnection()) {
            var service = new BoardQueryService(connection);
            var optional = service.getById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.printf("Nao foi encontrado um BOARD com o ID %s\n", id)
            );
        }
    }

    private void deleteBoard() throws SQLException {
        System.out.println("Informe o ID do BOARD que deseja excluir:");
        var id = scanner.nextLong();
        scanner.nextLine();
        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("O BOARD %s foi deletado com sucesso!\n", id);
            } else {
                System.out.printf("Nao foi encontrado um BOARD com o ID %s\n", id);
            }
        }

    }

    private BoardColumnEntity createColumn(
            final String columnName,
            final BoardColumnKind kind,
            final int order
    ) {
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName(columnName);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }

}
