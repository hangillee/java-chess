package chess.controller;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.command.Command;
import chess.domain.dto.BoardDto;
import chess.exception.GameIsNotStartException;
import chess.view.InputView;
import chess.view.OutputView;

import java.util.List;

public class ChessController {
    private static final String END_TRUE = "true";

    private ChessGame chessGame;

    public void run(ChessGame chessGame) {
        boolean isPlaying = true;

        OutputView.printManual();
        while (isPlaying) {
            isPlaying = playGame(chessGame);
        }
    }

    public boolean playGame(ChessGame chessGame) {
        try {
            String inputCommand = InputView.inputCommand();
            Command.findCommand(inputCommand).execute(chessGame, inputCommand);
            checkGameStart(chessGame);
            OutputView.printBoard(chessGame.getBoard());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return chessGame.isBeforeEnd();
    }

    private void checkGameStart(ChessGame chessGame) {
        if (chessGame.isBeforeStart()) {
            throw new GameIsNotStartException();
        }
    }

    public BoardDto start() {
        chessGame = new ChessGame();
        chessGame.settingBoard();
        Board board = chessGame.getBoard();
        return new BoardDto(board, chessGame.nowTurn());
    }

    public BoardDto move(String target, String destination) {
        chessGame.move(target, destination);
        if (chessGame.isBeforeEnd()) {
            return new BoardDto(chessGame.getBoard(), chessGame.nowTurn());
        }
        return new BoardDto(chessGame.getBoard(), chessGame.nowTurn().name(), END_TRUE);
    }

    public List<String> movablePosition(String target) {
        return chessGame.findMovablePosition(target);
    }

    public BoardDto boardDto() {
        return new BoardDto(chessGame.getBoard(), chessGame.nowTurn());
    }
}