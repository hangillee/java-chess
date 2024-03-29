package repository;

import static org.assertj.core.api.Assertions.assertThat;

import domain.piece.info.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameRepositoryTest {
    private final FakeGameRepository GameRepository = new FakeGameRepository();

    @Test
    @DisplayName("게임을 저장한다.")
    void saveGame() {
        GameRepository.saveGame(Color.WHITE, false, false);

        assertThat(GameRepository.isGameAlreadyStarted()).isTrue();
    }

    @Test
    @DisplayName("게임의 턴을 찾는다.")
    void findTurn() {
        GameRepository.saveGame(Color.WHITE, false, false);

        assertThat(GameRepository.findTurn()).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("게임의 턴을 변경한다.")
    void updateTurn() {
        GameRepository.saveGame(Color.WHITE, false, false);
        GameRepository.updateTurn(Color.BLACK);

        assertThat(GameRepository.findTurn()).isEqualTo("BLACK");
    }

    @Test
    @DisplayName("게임이 시작되었는지 확인한다.")
    void isGameAlreadyStarted() {
        GameRepository.saveGame(Color.WHITE, true, false);

        assertThat(GameRepository.isGameAlreadyStarted()).isTrue();
    }
}
