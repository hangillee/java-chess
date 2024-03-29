package domain.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import domain.piece.info.Color;
import domain.piece.info.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PieceGeneratorTest {
    @Test
    @DisplayName("DB에 저장된 데이터를 통해 Piece를 생성한다.")
    void of() {
        final String pieceName = "WHITEKING";

        final Piece piece = PieceGenerator.of(pieceName);

        assertAll(
                () -> assertThat(piece.color()).isEqualTo(Color.WHITE),
                () -> assertThat(piece.type()).isEqualTo(Type.KING)
        );
    }
}
