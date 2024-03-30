package domain.game.state;

import domain.board.Board;

public interface State {
    State start(final Board board);

    State end();

    boolean isInit();

    boolean isStarted();

    boolean isEnded();

    boolean isNotEnded();

    State isKingDead();
}
