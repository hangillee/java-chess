package repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DBConnectorTest {
    @Test
    @DisplayName("DB 연결 테스트")
    void getConnection() {
        assertThat(DBConnector.getConnection()).isNotNull();
    }
}
