package repository;

import static org.assertj.core.api.Assertions.assertThat;

import domain.board.Position;
import domain.piece.King;
import domain.piece.Piece;
import domain.piece.info.File;
import domain.piece.info.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardRepositoryTest {
    final FakeBoardRepository boardRepository = new FakeBoardRepository();

    @Test
    @DisplayName("기물을 저장한다.")
    void savePiece() {
        final Piece piece = King.white();

        final int pieceId = boardRepository.savePiece(piece);

        assertThat(pieceId).isEqualTo(1);
    }

    @Test
    @DisplayName("위치를 저장한다.")
    void savePosition() {
        final Position position = new Position(File.A, Rank.ONE);

        final int positionId = boardRepository.savePosition(position);

        assertThat(positionId).isEqualTo(1);
    }

    @Test
    @DisplayName("체스 보드 한 칸을 저장한다.")
    void saveSquare() {
        final Position position = new Position(File.A, Rank.ONE);
        final Piece piece = King.white();
        final int positionId = boardRepository.savePosition(position);
        final int pieceId = boardRepository.savePiece(piece);

        boardRepository.saveSquare(positionId, pieceId);

        assertThat(boardRepository.findAllSquares()).containsEntry(position, piece);
    }

    @Test
    @DisplayName("체스 보드의 모든 칸을 삭제한다.")
    void deleteSquares() {
        boardRepository.deleteSquares();

        assertThat(boardRepository.findAllSquares()).isEmpty();
    }
}
