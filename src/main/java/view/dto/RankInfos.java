package view.dto;

import domain.board.Position;
import domain.piece.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RankInfos {
    private static final int BOARD_UPPER_BOUND = 7;
    private static final int BOARD_LOWER_BOUND = 0;

    private final List<RankInfo> rankInfos;

    private RankInfos(List<RankInfo> rankInfos) {
        this.rankInfos = rankInfos;
    }

    public static RankInfos of(final Map<Position, Piece> squares) {
        final List<RankInfo> rankInfos = new ArrayList<>();
        for (int rank = BOARD_UPPER_BOUND; rank >= BOARD_LOWER_BOUND; rank--) {
            final RankInfo pieceShapeOfRank = DtoMapper.getPieceShapeOn(squares, rank);
            rankInfos.add(pieceShapeOfRank);
        }
        return new RankInfos(rankInfos);
    }


    public List<RankInfo> getRankInfos() {
        return Collections.unmodifiableList(rankInfos);
    }
}
