package repository;

import domain.board.Position;
import domain.piece.Piece;
import domain.piece.PieceGenerator;
import domain.piece.info.File;
import domain.piece.info.Rank;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class BoardRepository {
    private final Connection connection;

    public BoardRepository(final Connection connection) {
        this.connection = connection;
    }

    public int savePiece(final Piece piece) {
        final String color = piece.color().name();
        final String type = piece.type().name();

        final var query = "INSERT INTO piece (color, type) VALUES (?, ?)";
        try (final var preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, color);
            preparedStatement.setString(2, type);
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            return getSavedPieceId(generatedKeys);
        } catch (Exception e) {
            throw new IllegalArgumentException("기물 저장에 실패했습니다.");
        }
    }

    private Integer getSavedPieceId(final ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next()) {
            final long pieceId = generatedKeys.getLong(1);
            return Math.toIntExact(pieceId);
        }
        throw new IllegalArgumentException("기물 저장에 실패했습니다.");
    }

    public int savePosition(final Position position) {
        final int file = position.fileIndex();
        final int rank = position.rankIndex();

        final String query = "INSERT INTO position (file_index, rank_index) VALUES (?, ?)";
        try (final var preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, file);
            preparedStatement.setInt(2, rank);
            preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            return getSavedPositionId(generatedKeys);
        } catch (Exception e) {
            throw new IllegalArgumentException("위치 저장에 실패했습니다.");
        }
    }

    private Integer getSavedPositionId(final ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next()) {
            final long pieceId = generatedKeys.getLong(1);
            return Math.toIntExact(pieceId);
        }
        throw new IllegalArgumentException("위치 저장에 실패했습니다.");
    }

    public void saveSquare(final int positionId, final int pieceId) {
        final String turnQuery = "INSERT INTO square (position_id, piece_id) VALUES (?, ?)";
        try (final var preparedStatement = connection.prepareStatement(turnQuery)) {
            preparedStatement.setInt(1, positionId);
            preparedStatement.setInt(2, pieceId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("보드 저장에 실패했습니다.");
        }
    }

    public Map<Position, Piece> findAllSquares() {
        final String query = "SELECT * FROM square "
                + "JOIN position ON square.position_id = position.id "
                + "JOIN piece ON square.piece_id = piece.id";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();
            return createSquares(resultSet);
        } catch (Exception e) {
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

    public void deleteSquares() {
        final String query = "DELETE FROM square;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            restartAutoIncrement("square");
            deletePositions();
            deletePieces();
        } catch (Exception e) {
            throw new IllegalStateException("보드 초기화에 실패했습니다.");
        }
    }

    private void deletePositions() {
        final String query = "DELETE FROM position;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            restartAutoIncrement("position");
        } catch (Exception e) {
            throw new IllegalStateException("위치 초기화에 실패했습니다.");
        }
    }

    private void deletePieces() {
        final String query = "DELETE FROM piece;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            restartAutoIncrement("piece");
        } catch (Exception e) {
            throw new IllegalStateException("기물 초기화에 실패했습니다.");
        }
    }

    private void restartAutoIncrement(final String tableName) {
        final String query = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1;";
        try (final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("DB Auto Increment 초기화에 실패했습니다.");
        }
    }
}
