package repository;

import domain.piece.info.Color;
import java.sql.Connection;

public class GameRepository {
    private final ConnectionPool pool;

    public GameRepository() {
        try {
            this.pool = MysqlConnectionPool.create("chess", "root", "root");
        } catch (Exception e) {
            throw new IllegalStateException("DB 연결에 실패했습니다.");
        }
    }

    public void saveGame(final Color turn, final boolean isStarted, final boolean isGameOver) {
        final String turnValue = turn.name();

        final Connection connection = pool.getConnection();
        final String turnQuery = "INSERT INTO game (turn, is_started, is_over) VALUES (?, ?, ?)";
        try (final var preparedStatement = connection.prepareStatement(turnQuery)) {
            preparedStatement.setString(1, turnValue);
            preparedStatement.setBoolean(2, isStarted);
            preparedStatement.setBoolean(3, isGameOver);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("게임을 저장할 수 없습니다.");
        } finally {
            pool.releaseConnection(connection);
        }
    }

    public boolean isGameAlreadyStarted() {
        final Connection connection = pool.getConnection();
        final String query = "SELECT * FROM game";
        try (final var preparedStatement = connection.prepareStatement(query);
             final var resultSet = preparedStatement.executeQuery()) {
            return resultSet.next();
        } catch (Exception e) {
            throw new IllegalStateException("게임 시작 여부를 확인할 수 없습니다.");
        } finally {
            pool.releaseConnection(connection);
        }
    }

    public boolean isGameOver() {
        final Connection connection = pool.getConnection();
        final String query = "SELECT is_over FROM game";
        try (final var preparedStatement = connection.prepareStatement(query);
             final var resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getBoolean("is_over");
        } catch (Exception e) {
            throw new IllegalStateException("게임 종료 여부를 확인할 수 없습니다.");
        } finally {
            pool.releaseConnection(connection);
        }
    }

    public void updateGameOver() {
        final Connection connection = pool.getConnection();
        final String query = "UPDATE game SET is_over = true WHERE id = 1";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("게임 종료 여부를 변경할 수 없습니다.");
        } finally {
            pool.releaseConnection(connection);
        }
    }
}
