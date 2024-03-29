package domain.piece;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PieceGenerator {
    private static final Map<String, Piece> pieces = new ConcurrentHashMap<>();

    static {
        pieces.put("WHITEKING", King.white());
        pieces.put("BLACKKING", King.black());
        pieces.put("WHITEQUEEN", Queen.white());
        pieces.put("BLACKQUEEN", Queen.black());
        pieces.put("WHITEROOK", Rook.white());
        pieces.put("BLACKROOK", Rook.black());
        pieces.put("WHITEBISHOP", Bishop.white());
        pieces.put("BLACKBISHOP", Bishop.black());
        pieces.put("WHITEKNIGHT", Knight.white());
        pieces.put("BLACKKNIGHT", Knight.black());
        pieces.put("WHITEPAWN", Pawn.white());
        pieces.put("BLACKPAWN", Pawn.black());
        pieces.put("NONENONE", None.none());
    }

    private PieceGenerator() {
    }

    public static Piece of(String pieceName) {
        return pieces.get(pieceName);
    }
}
