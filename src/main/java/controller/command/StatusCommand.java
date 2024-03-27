package controller.command;

import domain.game.Game;
import domain.piece.info.Color;
import java.util.List;
import view.OutputView;

public class StatusCommand implements Command {
    private static final String STATUS = "status";
    private static StatusCommand instance;

    private final Game game;

    public StatusCommand(final Game game) {
        this.game = game;
    }

    public static StatusCommand of(final Game game) {
        if (instance == null) {
            instance = new StatusCommand(game);
        }
        return instance;
    }

    @Override
    public void execute(final List<String> commandTokens) {
        final double whiteScore = game.calculateScore(Color.WHITE);
        final double blackScore = game.calculateScore(Color.BLACK);
        OutputView.printGameStatus(whiteScore, blackScore);
    }

    @Override
    public boolean isSameAs(final String value) {
        return STATUS.equals(value);
    }
}
