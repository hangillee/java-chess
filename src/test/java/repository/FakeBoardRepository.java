package repository;

import domain.board.Position;
import domain.piece.Piece;
import java.util.HashMap;
import java.util.Map;

public class FakeBoardRepository {
    private final Map<Integer, Piece> pieces = new HashMap<>();
    private final Map<Integer, Position> positions = new HashMap<>();
    private final Map<Integer, Map<Integer, Integer>> squares = new HashMap<>();

    public int savePiece(final Piece piece) {
        final int pieceId = pieces.size() + 1;
        pieces.put(pieceId, piece);
        return pieceId;
    }

    public int savePosition(final Position position) {
        final int positionId = positions.size() + 1;
        positions.put(positionId, position);
        return positionId;
    }

    public void saveSquare(final int positionId, final int pieceId) {
        final Map<Integer, Integer> square = new HashMap<>();
        square.put(positionId, pieceId);
        squares.put(squares.size() + 1, square);
    }

    public Map<Position, Piece> findAllSquares() {
        final Map<Position, Piece> result = new HashMap<>();
        for (final Map<Integer, Integer> square : squares.values()) {
            for (final Map.Entry<Integer, Integer> entry : square.entrySet()) {
                final Position position = positions.get(entry.getKey());
                final Piece piece = pieces.get(entry.getValue());
                result.put(position, piece);
            }
        }
        return result;
    }

    public void deleteSquares() {
        pieces.clear();
        positions.clear();
        squares.clear();
    }
}
