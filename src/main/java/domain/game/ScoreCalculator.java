package domain.game;

import domain.board.Position;
import domain.piece.Piece;
import domain.piece.info.Color;
import domain.piece.info.Score;
import domain.piece.info.Type;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreCalculator {
    public static final double PAWN_ALTERNATIVE_SCORE = 0.5;

    public double calculate(final Map<Position, Piece> squares, final Color color) {
        if (!isKingAlive(squares, color)) {
            return Score.findScoreByType(Type.KING);
        }
        return calculateTotalScore(squares, color) - calculatePawnScore(squares, color);
    }

    private double calculateTotalScore(final Map<Position, Piece> squares, final Color color) {
        return squares.values()
                .stream()
                .filter(piece -> piece.isColorOf(color))
                .mapToDouble(piece -> Score.findScoreByType(piece.type()))
                .sum();
    }

    private double calculatePawnScore(final Map<Position, Piece> squares, final Color color) {
        return countPawnOnSameFile(squares, color) * PAWN_ALTERNATIVE_SCORE;
    }

    private int countPawnOnSameFile(final Map<Position, Piece> squares, final Color color) {
        return groupingPawnOnFileByCount(squares, color)
                .values()
                .stream()
                .filter(count -> count > 1L)
                .mapToInt(Math::toIntExact)
                .sum();
    }

    private Map<Integer, Long> groupingPawnOnFileByCount(final Map<Position, Piece> squares, final Color color) {
        return squares.keySet()
                .stream()
                .filter(position -> squares.get(position).isColorOf(color))
                .filter(position -> squares.get(position).type() == Type.PAWN)
                .collect(Collectors.groupingBy(Position::fileIndex, Collectors.counting()));
    }

    private boolean isKingAlive(final Map<Position, Piece> squares, final Color color) {
        return squares.values()
                .stream()
                .filter(piece -> piece.isColorOf(color))
                .anyMatch(piece -> piece.type() == Type.KING);
    }
}
