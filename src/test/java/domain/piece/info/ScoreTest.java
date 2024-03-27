package domain.piece.info;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScoreTest {
    @Test
    @DisplayName("기물의 종류에 따라 점수를 반환한다.")
    void findScoreByType() {
        // given
        final Type type = Type.PAWN;

        // when
        final double score = Score.PAWN.findScoreByType(type);

        // then
        assertThat(score).isEqualTo(1);
    }

}
