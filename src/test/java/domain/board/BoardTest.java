package domain.board;

import domain.piece.Piece;
import domain.piece.info.File;
import domain.piece.info.Rank;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTest {
    private final Board board = new Board(BoardInitiator.init());

    @Test
    @DisplayName("기물이 잘 이동 되었는지 확인한다")
    void checkMove() {
        final Position source = new Position(File.of(0), Rank.of(1));
        final Position next = new Position(File.of(0), Rank.of(2));
        final Piece sourcePiece = board.squares().get(source);

        board.moveByPosition(source, next);
        final Piece nextPiece = board.squares().get(next);

        Assertions.assertThat(sourcePiece).isEqualTo(nextPiece);
    }

    @Test
    @DisplayName("기물을 이동 불가능한 자리로 움직이면 예외가 발생한다")
    void moveToUnmovablePosition() {
        final Position source = new Position(File.of(0), Rank.of(0));
        final Position next = new Position(File.of(0), Rank.of(1));

        Assertions.assertThatThrownBy(() -> board.moveByPosition(source, next))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("기물을 이동할 때 차례가 아닌 경우 예외가 발생한다")
    void moveWhenNotTurn() {
        final Position source = new Position(File.of(0), Rank.of(1));
        final Position next = new Position(File.of(0), Rank.of(2));

        Assertions.assertThatThrownBy(() -> board.moveByPosition(next, source))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 차례가 아닙니다.");
    }
}
