package domain.board;

import domain.piece.None;
import domain.piece.Piece;
import domain.piece.info.Color;
import domain.piece.info.Direction;
import domain.piece.info.Type;
import domain.strategy.MoveStrategy;
import java.util.List;
import java.util.Map;
import repository.BoardRepository;

public class Board {
    private static final int BOARD_UPPER_BOUND = 7;
    private static final int BOARD_LOWER_BOUND = 0;
    private static final BoardRepository boardRepository = new BoardRepository();

    private Color turn;
    private final Map<Position, Piece> squares;

    public Board(final Map<Position, Piece> squares) {
        this.turn = Color.WHITE;
        this.squares = squares;
    }

    public void moveByPosition(final Position source, final Position target) {
        final Piece currentPiece = squares.get(source);
        validateTurnOfPiece(currentPiece);
        final List<Direction> directions = currentPiece.movableDirections();

        final List<Position> movablePositions = findMovablePositions(source, currentPiece, directions);
        target.isMovable(movablePositions);

        updateBoard(source, target, currentPiece);
        updateTurn();
    }

    private void validateTurnOfPiece(final Piece currentPiece) {
        if (currentPiece.isNotSameColor(turn)) {
            throw new IllegalArgumentException("현재 차례가 아닙니다.");
        }
    }

    private List<Position> findMovablePositions(final Position source, final Piece currentPiece,
                                                final List<Direction> directions) {
        final MoveStrategy strategy = currentPiece.strategy();
        final List<Position> positions = strategy.movablePositions(source, directions, squares);
        return positions.stream()
                .filter(this::isFileInBoard)
                .filter(this::isRankInBoard)
                .toList();
    }

    private void updateBoard(final Position source, final Position target, final Piece currentPiece) {
        final Piece targetPiece = squares.get(target);
        currentPiece.isSameColor(targetPiece.color());
        squares.put(target, currentPiece);
        squares.put(source, new None(Color.NONE, Type.NONE));
        deleteSquares();
        saveSquares();
    }

    private void deleteSquares() {
        boardRepository.deleteSquares();
    }

    private void updateTurn() {
        turn = Color.opposite(turn);
        boardRepository.updateTurn(turn);
    }

    private boolean isFileInBoard(final Position source) {
        int file = source.fileIndex();
        return file >= BOARD_LOWER_BOUND && file <= BOARD_UPPER_BOUND;
    }

    private boolean isRankInBoard(final Position source) {
        int rank = source.rankIndex();
        return rank >= BOARD_LOWER_BOUND && rank <= BOARD_UPPER_BOUND;
    }

    public boolean isKingDead() {
        return squares.values()
                .stream()
                .filter(piece -> piece.isColorOf(turn))
                .noneMatch(Piece::isKing);
    }

    public Map<Position, Piece> squares() {
        return squares;
    }

    public void saveSquares() {
        squares.forEach((position, piece) -> {
            final int positionId = boardRepository.savePosition(position);
            final int pieceId = boardRepository.savePiece(piece);
            boardRepository.saveSquare(positionId, pieceId);
        });
    }

    public void initTurnIfExist() {
        turn = Color.valueOf(boardRepository.findTurn());
    }

    public void initBoardIfExist() {
        squares.clear();
        squares.putAll(boardRepository.findAllSquares());
    }
}
