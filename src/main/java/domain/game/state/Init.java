package domain.game.state;

import domain.board.Board;

public class Init implements State {
    @Override
    public State start(final Board board) {
        return new Start(board);
    }

    @Override
    public State end() {
        return new End();
    }

    @Override
    public boolean isInit() {
        return true;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isEnded() {
        return false;
    }

    @Override
    public boolean isNotEnded() {
        return true;
    }

    @Override
    public State isKingDead() {
        return this;
    }
}
