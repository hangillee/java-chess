package domain.game;

import domain.board.Board;
import domain.board.BoardInitiator;
import domain.board.Position;
import domain.game.state.Init;
import domain.game.state.State;
import domain.piece.Piece;
import domain.piece.info.Color;
import java.util.Map;
import repository.GameRepository;

public class Game {
    private static final GameRepository gameRepository = new GameRepository();

    private State state;
    private final Board board;

    public Game() {
        this.state = new Init();
        this.board = new Board(BoardInitiator.init());
    }

    public Game(final Board board) {
        this.state = new Init();
        this.board = board;
    }

    public void moveByPosition(final Position source, final Position target) {
        if (state.isInit() || state.isEnded()) {
            throw new IllegalStateException("게임 플레이 중이 아닙니다.");
        }
        board.moveByPosition(source, target);
        checkKingIsAlive();
    }

    private void checkKingIsAlive() {
        if (board.isKingDead()) {
            end();
            gameRepository.updateGameOver();
        }
    }

    public double calculateScore(final Color color) {
        final ScoreCalculator scoreCalculator = new ScoreCalculator();
        return scoreCalculator.calculate(board.squares(), color);
    }

    public void start() {
        state = state.start();
        if (gameRepository.isGameAlreadyStarted() && !gameRepository.isGameOver()) {
            board.initTurnIfExist();
            board.initBoardIfExist();
            return;
        }
        gameRepository.saveGame(Color.WHITE, true, false);
        board.saveSquares();
    }

    public void end() {
        state = state.end();
    }

    public boolean isInit() {
        return state.isInit();
    }

    public boolean isStarted() {
        return state.isStarted();
    }

    public boolean isEnded() {
        return state.isEnded();
    }

    public boolean isNotEnded() {
        return state.isNotEnded();
    }

    public boolean isTurnOf(final Color color) {
        return board.isTurnOf(color);
    }

    public Map<Position, Piece> board() {
        return board.squares();
    }
}
