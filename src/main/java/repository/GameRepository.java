package repository;

import domain.piece.info.Color;
import java.sql.Connection;

public class GameRepository {
    private final Connection connection;

    public GameRepository() {
        connection = DBConnector.getConnection();
    }

    public GameRepository(final Connection connection) {
        this.connection = connection;
    }

    public void saveGame(final Color turn, final boolean isStarted, final boolean isGameOver) {
        final String turnValue = turn.name();
        final String turnQuery = "INSERT INTO game (turn, is_started, is_over) VALUES (?, ?, ?)";
        try (final var preparedStatement = connection.prepareStatement(turnQuery)) {
            preparedStatement.setString(1, turnValue);
            preparedStatement.setBoolean(2, isStarted);
            preparedStatement.setBoolean(3, isGameOver);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("게임을 저장할 수 없습니다.");
        }
    }

    public boolean isGameAlreadyStarted() {
        final String query = "SELECT * FROM game";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            throw new IllegalStateException("게임 시작 여부를 확인할 수 없습니다.");
        }
    }

    public String findTurn() {
        final String query = "SELECT turn FROM game";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("turn");
        } catch (Exception e) {
            throw new IllegalStateException("게임 차례를 확인할 수 없습니다.");
        }
    }

    public void updateTurn(final Color turn) {
        final String turnValue = turn.name();
        final String query = "UPDATE game SET turn = ? WHERE id = 1";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, turnValue);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("게임 차례를 변경할 수 없습니다.");
        }
    }
}
