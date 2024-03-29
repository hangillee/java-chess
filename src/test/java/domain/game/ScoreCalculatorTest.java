package domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import domain.board.Board;
import domain.board.Position;
import domain.piece.Bishop;
import domain.piece.King;
import domain.piece.Knight;
import domain.piece.Pawn;
import domain.piece.Piece;
import domain.piece.Queen;
import domain.piece.Rook;
import domain.piece.info.Color;
import domain.piece.info.File;
import domain.piece.info.Rank;
import domain.piece.info.Type;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScoreCalculatorTest {
    @Test
    @DisplayName("게임은 색깔 별 기물의 점수를 계산할 수 있다.")
    void calculateWhiteScores() {
        Board board = new Board(generateSquares());
        Game game = new Game(board);

        assertAll(
                () -> assertThat(game.calculateScore(Color.WHITE)).isEqualTo(19.5),
                () -> assertThat(game.calculateScore(Color.BLACK)).isEqualTo(20)

        );
    }

    @Test
    @DisplayName("King이 잡히면 즉시 패배이기 때문에 점수는 0점이다.")
    void calculateScoreWhenKingIsDead() {
        Board board = new Board(generateDeadKingSquares());
        Game game = new Game(board);

        assertAll(
                () -> assertThat(game.calculateScore(Color.WHITE)).isZero(),
                () -> assertThat(game.calculateScore(Color.BLACK)).isEqualTo(20)
        );
    }

    /*
    .KR.....  8
    P.PB....  7
    .P..Q...  6
    ........  5
    .....nq.  4
    .....p.p  3
    .....pp.  2
    ....rk..  1
    */
    private Map<Position, Piece> generateSquares() {
        return Map.ofEntries(
                Map.entry(new Position(File.B, Rank.EIGHT), new King(Color.BLACK, Type.KING)),
                Map.entry(new Position(File.C, Rank.EIGHT), new Rook(Color.BLACK, Type.ROOK)),
                Map.entry(new Position(File.A, Rank.SEVEN), new Pawn(Color.BLACK, Type.PAWN)),
                Map.entry(new Position(File.C, Rank.SEVEN), new Pawn(Color.BLACK, Type.PAWN)),
                Map.entry(new Position(File.D, Rank.SEVEN), new Bishop(Color.BLACK, Type.BISHOP)),
                Map.entry(new Position(File.B, Rank.SIX), new Pawn(Color.BLACK, Type.PAWN)),
                Map.entry(new Position(File.E, Rank.SIX), new Queen(Color.BLACK, Type.QUEEN)),
                Map.entry(new Position(File.F, Rank.FOUR), new Knight(Color.WHITE, Type.KNIGHT)),
                Map.entry(new Position(File.G, Rank.FOUR), new Queen(Color.WHITE, Type.QUEEN)),
                Map.entry(new Position(File.F, Rank.THREE), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.H, Rank.THREE), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.F, Rank.TWO), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.G, Rank.TWO), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.E, Rank.ONE), new Rook(Color.WHITE, Type.ROOK)),
                Map.entry(new Position(File.F, Rank.ONE), new King(Color.WHITE, Type.KING))
        );
    }

    private Map<Position, Piece> generateDeadKingSquares() {
        return Map.ofEntries(
                Map.entry(new Position(File.B, Rank.EIGHT), new King(Color.BLACK, Type.KING)),
                Map.entry(new Position(File.C, Rank.EIGHT), new Rook(Color.BLACK, Type.ROOK)),
                Map.entry(new Position(File.A, Rank.SEVEN), new Pawn(Color.BLACK, Type.PAWN)),
                Map.entry(new Position(File.C, Rank.SEVEN), new Pawn(Color.BLACK, Type.PAWN)),
                Map.entry(new Position(File.D, Rank.SEVEN), new Bishop(Color.BLACK, Type.BISHOP)),
                Map.entry(new Position(File.B, Rank.SIX), new Pawn(Color.BLACK, Type.PAWN)),
                Map.entry(new Position(File.E, Rank.SIX), new Queen(Color.BLACK, Type.QUEEN)),
                Map.entry(new Position(File.F, Rank.FOUR), new Knight(Color.WHITE, Type.KNIGHT)),
                Map.entry(new Position(File.G, Rank.FOUR), new Queen(Color.WHITE, Type.QUEEN)),
                Map.entry(new Position(File.F, Rank.THREE), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.H, Rank.THREE), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.F, Rank.TWO), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.G, Rank.TWO), new Pawn(Color.WHITE, Type.PAWN)),
                Map.entry(new Position(File.E, Rank.ONE), new Rook(Color.WHITE, Type.ROOK))
        );
    }
}
