package view.dto;

import domain.board.Position;
import domain.piece.Piece;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import view.PieceShape;

public class DtoMapper {

    private DtoMapper() {
    }

    public static RankInfo getPieceShapeOn(final Map<Position, Piece> squares, final int rank) {
        final List<Piece> pieces = getPiecesOn(squares, rank);
        final List<String> pieceShapes = new ArrayList<>();

        for (final Piece piece : pieces) {
            addByPieceColor(piece, pieceShapes);
        }
        return new RankInfo(pieceShapes);
    }

    private static List<Piece> getPiecesOn(final Map<Position, Piece> squares, final int rank) {
        return squares.entrySet()
                .stream()
                .filter(entry -> entry.getKey().rankIndex() == rank)
                .sorted(Comparator.comparingInt(entry -> entry.getKey().fileIndex()))
                .map(Entry::getValue)
                .toList();
    }

    private static void addByPieceColor(final Piece piece, final List<String> pieceShapes) {
        if (piece.isWhite()) {
            pieceShapes.add(PieceShape.whiteShapeOf(piece.type().name()));
            return;
        }
        pieceShapes.add(PieceShape.blackShapeOf(piece.type().name()));
    }
}
