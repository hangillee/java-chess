package domain.game.state;

import domain.board.Board;

public class Start implements State {
    private final Board board;

    public Start(final Board board) {
        this.board = board;
    }

    @Override
    public State start(final Board board) {
        throw new IllegalStateException("게임이 이미 시작되어 있습니다.");
    }

    @Override
    public State end() {
        return new End();
    }

    @Override
    public boolean isInit() {
        return false;
    }

    @Override
    public boolean isStarted() {
        return true;
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
        if (board.isKingDead()) {
            return new End();
        }
        return this;
    }
}
