package domain.piece.info;

import java.util.Arrays;

public enum Score {
    PAWN(Type.PAWN, 1),
    KNIGHT(Type.KNIGHT, 2.5),
    BISHOP(Type.BISHOP, 3),
    ROOK(Type.ROOK, 5),
    QUEEN(Type.QUEEN, 9),
    KING(Type.KING, 0);

    private final Type type;
    private final double value;

    Score(final Type type, final double value) {
        this.type = type;
        this.value = value;
    }

    public double findScoreByType(final Type type) {
        return Arrays.stream(Score.values())
                .filter(score -> score.type == type)
                .findFirst()
                .map(Score::value)
                .orElseThrow(() -> new IllegalArgumentException("해당 타입의 점수가 존재하지 않습니다."));
    }

    private double value() {
        return value;
    }
}
