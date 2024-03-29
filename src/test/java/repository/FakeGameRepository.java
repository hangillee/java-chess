package repository;

import domain.piece.info.Color;
import java.util.HashMap;
import java.util.Map;

public class FakeGameRepository {
    private Map<Integer, Columns> games = new HashMap<>();

    public void saveGame(final Color turn, final boolean isStarted, final boolean isGameOver) {
        final int gameId = games.size() + 1;
        games.put(gameId, new Columns(turn, isStarted, isGameOver));
    }

    public boolean isGameAlreadyStarted() {
        return !games.isEmpty();
    }

    public String findTurn() {
        return games.get(1).turn().name();
    }

    public void updateTurn(final Color turn) {
        games.put(1, new Columns(turn, true, false));
    }

    record Columns(Color turn, boolean isStarted, boolean isGameOver) {
    }
}
