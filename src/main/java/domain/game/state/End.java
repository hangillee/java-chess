package domain.game.state;

import domain.board.Board;

public class End implements State {
    @Override
    public State start(final Board board) {
        return new Start(board);
    }

    @Override
    public State end() {
        throw new IllegalStateException("게임이 이미 종료되어 있습니다.");
    }

    @Override
    public boolean isInit() {
        return false;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isEnded() {
        return true;
    }

    @Override
    public boolean isNotEnded() {
        return false;
    }

    @Override
    public State isKingDead() {
        return this;
    }
}
