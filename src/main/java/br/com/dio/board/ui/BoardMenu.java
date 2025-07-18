package br.com.dio.board.ui;

import br.com.dio.board.dto.BoardColumnInfoDTO;
import br.com.dio.board.persistence.entity.BoardColumnEntity;
import br.com.dio.board.persistence.entity.BoardEntity;
import br.com.dio.board.persistence.entity.CardEntity;
import br.com.dio.board.service.BoardColumnQueryService;
import br.com.dio.board.service.BoardQueryService;
import br.com.dio.board.service.CardQueryService;
import br.com.dio.board.service.CardService;
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
                    option = Integer.parseInt(scanner.next());
                } catch (NumberFormatException e) {
                    System.out.println("Por favor informe um numero valido:");
                }

                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
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
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() throws SQLException {
        var card = new CardEntity();
        System.out.println("Informe o titulo do CARD:");
        card.setTitle(scanner.next());
        System.out.println("Informe a descricao do card");
        card.setDescription(scanner.next());
        card.setBoardColumnEntity(boardEntity.getInitialColumn());
        try (var connection = getConnection()) {
            new CardService(connection).insert(card);
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o ID do CARD que deseja mover para a proxima coluna");
        var cardId = scanner.nextLong();
        var boardColumnsInfo = boardEntity
                .getBoardColumns()
                .stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();

        try (var connection = getConnection()) {
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void blockCard() {

    }

    private void unblockCard() {
    }

    private void cancelCard() {
        System.out.println("Informe o ID do CARD que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();
        var cancelColumn = boardEntity.getCancelColumn();

        var boardColumnsInfo = boardEntity
                .getBoardColumns()
                .stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();

        try (var connection = getConnection()) {
            new CardService(connection).cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(boardEntity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board: [%s, %s]\n", b.id(), b.name());
                b.dtos().forEach(c ->
                        System.out.printf("Coluna: [%s], tipo: [%s], tem: [%s] cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showColumn() throws SQLException {
        var columnsIds = boardEntity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        var selectedColumnId = -1L;
        while (!columnsIds.contains(selectedColumnId)) {
            System.out.printf("Escolha uma coluna do BOARD: %s\n", boardEntity.getName());
            boardEntity.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumnId = scanner.nextLong();
        }
        try (var connection = getConnection()) {
            var column = new BoardColumnQueryService(connection).getById(selectedColumnId);
            column.ifPresent(co -> {
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCardEntities().forEach(ca -> {
                    System.out.printf("Card %s - %s\nDescricao: %s\n", ca.getId(), ca.getTitle(), ca.getDescription());
                    System.out.println("---------------------------------------------------");
                });
            });
        }
    }

    private void showCard() throws SQLException {
        System.out.println("Informe o ID do CARD que deseja visualizar");
        var selectedCardId = scanner.nextLong();
        try (var connection = getConnection()) {
            new CardQueryService(connection)
                    .getById(selectedCardId)
                    .ifPresentOrElse(
                            cardDetailsDTO -> {
                                System.out.printf("CARD numero: %s, Nome: %s\n", cardDetailsDTO.id(), cardDetailsDTO.title());
                                System.out.printf("Descricao: %s\n", cardDetailsDTO.description());
                                System.out.printf(cardDetailsDTO.blocked() ? "Esta bloqueado. Motivo: %s" : "Nao esta bloqueado\n", cardDetailsDTO.blockDescription());
                                System.out.printf("Ja foi bloqueado %s vezes\n", cardDetailsDTO.blockAmount());
                                System.out.printf("No momento esta na coluna: %s - %s\n", cardDetailsDTO.columnId(), cardDetailsDTO.columnName());
                            },
                            () -> System.out.printf("Nao existe um card com o ID %s.\n", selectedCardId));
        }
    }
}
