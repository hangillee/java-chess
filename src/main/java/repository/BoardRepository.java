package repository;

import domain.board.Position;
import domain.piece.Piece;
import domain.piece.PieceGenerator;
import domain.piece.info.Color;
import domain.piece.info.File;
import domain.piece.info.Rank;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class BoardRepository {
    private final MysqlConnectionPool pool;

    public BoardRepository() {
        try {
            this.pool = MysqlConnectionPool.create("chess", "root", "root");
        } catch (SQLException e) {
            throw new IllegalStateException("DB 연결에 실패했습니다. " + e.getMessage());
        }
    }

    public int savePiece(final Piece piece) {
        final String color = piece.color().name();
        final String type = piece.type().name();

        final var query = "INSERT INTO piece (color, type) VALUES (?, ?)";
        final Connection connection = pool.getConnection();
        try (final var preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, color);
            preparedStatement.setString(2, type);
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            pool.releaseConnection(connection);
            return getSavedPieceId(generatedKeys);
        } catch (Exception e) {
            pool.releaseConnection(connection);
            throw new IllegalArgumentException("기물 저장에 실패했습니다.");
        }
    }

    private Integer getSavedPieceId(final ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next()) {
            final long pieceId = generatedKeys.getLong(1);
            generatedKeys.close();
            return Math.toIntExact(pieceId);
        }
        throw new IllegalArgumentException("기물 저장에 실패했습니다.");
    }

    public int savePosition(final Position position) {
        final int file = position.fileIndex();
        final int rank = position.rankIndex();

        final Connection connection = pool.getConnection();
        final String query = "INSERT INTO position (file_index, rank_index) VALUES (?, ?)";
        try (final var preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, file);
            preparedStatement.setInt(2, rank);
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            pool.releaseConnection(connection);
            return getSavedPositionId(generatedKeys);
        } catch (Exception e) {
            pool.releaseConnection(connection);
            throw new IllegalArgumentException("위치 저장에 실패했습니다.");
        }
    }

    private Integer getSavedPositionId(final ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next()) {
            final long pieceId = generatedKeys.getLong(1);
            generatedKeys.close();
            return Math.toIntExact(pieceId);
        }
        throw new IllegalArgumentException("위치 저장에 실패했습니다.");
    }

    public void saveSquare(final int positionId, final int pieceId) {
        final Connection connection = pool.getConnection();
        final String turnQuery = "INSERT INTO square (position_id, piece_id) VALUES (?, ?)";
        try (final var preparedStatement = connection.prepareStatement(turnQuery)) {
            preparedStatement.setInt(1, positionId);
            preparedStatement.setInt(2, pieceId);
            preparedStatement.executeUpdate();
            pool.releaseConnection(connection);
        } catch (Exception e) {
            pool.releaseConnection(connection);
            throw new IllegalStateException("보드 저장에 실패했습니다.");
        }
    }

    public Map<Position, Piece> findAllSquares() {
        final Connection connection = pool.getConnection();
        final String query = "SELECT * FROM square "
                + "JOIN position ON square.position_id = position.id "
                + "JOIN piece ON square.piece_id = piece.id";
        try (final var preparedStatement = connection.prepareStatement(query);
             final var resultSet = preparedStatement.executeQuery()) {
            pool.releaseConnection(connection);
            return createSquares(resultSet);
        } catch (Exception e) {
            pool.releaseConnection(connection);
            throw new IllegalStateException("보드 조회에 실패했습니다.");
        }
    }

    private Map<Position, Piece> createSquares(final ResultSet resultSet) throws SQLException {
        final Map<Position, Piece> squares = new HashMap<>();
        while (resultSet.next()) {
            final Position position = getPosition(resultSet);
            final Piece piece = getPiece(resultSet);
            squares.put(position, piece);
        }
        return squares;
    }

    private Piece getPiece(final ResultSet resultSet) throws SQLException {
        final String color = resultSet.getString("color");
        final String type = resultSet.getString("type");
        return PieceGenerator.of(color + type);
    }

    private Position getPosition(final ResultSet resultSet) throws SQLException {
        final int fileIndex = resultSet.getInt("file_index");
        final int rankIndex = resultSet.getInt("rank_index");
        return new Position(File.of(fileIndex), Rank.of(rankIndex));
    }

    public String findTurn() {
        final Connection connection = pool.getConnection();
        final String query = "SELECT turn FROM game";
        try (final var preparedStatement = connection.prepareStatement(query);
             final var resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            pool.releaseConnection(connection);
            return resultSet.getString("turn");
        } catch (Exception e) {
            pool.releaseConnection(connection);
            throw new IllegalStateException("게임 차례를 확인할 수 없습니다.");
        }
    }

    public void updateTurn(final Color turn) {
        final String turnValue = turn.name();

        final Connection connection = pool.getConnection();
        final String query = "UPDATE game SET turn = ? WHERE id = 1";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, turnValue);
            preparedStatement.executeUpdate();
            pool.releaseConnection(connection);
        } catch (Exception e) {
            pool.releaseConnection(connection);
            throw new IllegalStateException("게임 차례를 변경할 수 없습니다.");
        }
    }

    public void deleteSquares() {
        final Connection connection = pool.getConnection();
        final String query = "DELETE FROM square;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            restartAutoIncrement(connection, "square");
            deletePositions(connection);
            deletePieces(connection);
            pool.releaseConnection(connection);
        } catch (Exception e) {
            pool.releaseConnection(connection);
            throw new IllegalStateException("보드 초기화에 실패했습니다.");
        }
    }

    private void deletePositions(final Connection connection) {
        final String query = "DELETE FROM position;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            restartAutoIncrement(connection, "position");
        } catch (Exception e) {
            throw new IllegalStateException("위치 초기화에 실패했습니다.");
        }
    }

    private void deletePieces(final Connection connection) {
        final String query = "DELETE FROM piece;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            restartAutoIncrement(connection, "piece");
        } catch (Exception e) {
            throw new IllegalStateException("기물 초기화에 실패했습니다.");
        }
    }

    private void restartAutoIncrement(final Connection connection, final String tableName) {
        final String query = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("DB Auto Increment 초기화에 실패했습니다.");
        }
    }
}
