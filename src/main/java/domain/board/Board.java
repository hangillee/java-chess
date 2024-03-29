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
import repository.DBConnector;
import repository.GameRepository;

public class Board {
    private static final int BOARD_UPPER_BOUND = 7;
    private static final int BOARD_LOWER_BOUND = 0;

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
        final BoardRepository boardRepository = new BoardRepository(DBConnector.getConnection());
        boardRepository.deleteSquares();
    }

    private void updateTurn() {
        final GameRepository gameRepository = new GameRepository();
        turn = Color.opposite(turn);
        gameRepository.updateTurn(turn);
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
                .noneMatch(piece -> piece.type() == Type.KING);
    }

    public Map<Position, Piece> squares() {
        return squares;
    }

    public void saveSquares() {
        final BoardRepository boardRepository = new BoardRepository(DBConnector.getConnection());
        squares.forEach((position, piece) -> {
            final int positionId = boardRepository.savePosition(position);
            final int pieceId = boardRepository.savePiece(piece);
            boardRepository.saveSquare(positionId, pieceId);
        });
    }

    public void initTurnIfExist() {
        final GameRepository gameRepository = new GameRepository();
        turn = Color.valueOf(gameRepository.findTurn());
    }

    public void initBoardIfExist() {
        final BoardRepository boardRepository = new BoardRepository(DBConnector.getConnection());
        squares.clear();
        squares.putAll(boardRepository.findAllSquares());
    }

    public boolean isTurnOf(final Color color) {
        return color == turn;
    }
}
