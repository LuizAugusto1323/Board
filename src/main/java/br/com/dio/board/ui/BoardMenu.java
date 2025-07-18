package br.com.dio.board.ui;

import br.com.dio.board.persistence.entity.BoardEntity;
import br.com.dio.board.service.BoardQueryService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static br.com.dio.board.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {

    private final BoardEntity boardEntity;
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() {
        try {
            System.out.printf("Bem vindo ao BOARD %s, selecione a opcao que deseja:\n", boardEntity.getId());
            int option = -1;

            while (option != 9) {
                System.out.println("1 - Criar um novo CARD");
                System.out.println("2 - Mover um CARD existente");
                System.out.println("3 - Bloquear um CARD");
                System.out.println("4 - Desbloquear um CARD");
                System.out.println("5 - Cancelar um CARD");
                System.out.println("6 - Visualizar BOARD");
                System.out.println("7 - Visualizar coluna com CARDS");
                System.out.println("8 - Ver CARD");
                System.out.println("9 - Voltar ao menu anterior um CARD");
                System.out.println("10 - Sair");

                try {
                    option = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Por favor informe um numero valido:");
                }

                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCard();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCArd();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando ao menu anterior...");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opcao invalida, informe outra opcao do menu.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Nao foi possivel executar o MENU");
            System.exit(0);
        }
    }

    private void createCard() {

    }

    private void moveCard() {

    }

    private void blockCard() {

    }

    private void unblockCard() {
    }

    private void cancelCArd() {

    }

    private void showBoard() throws SQLException {
        try(var connection = getConnection()){
            var optional = new BoardQueryService(connection).showBoardDetails(boardEntity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.dtos().forEach(c ->
                        System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showColumn() {

    }

    private void showCard() {

    }
}
