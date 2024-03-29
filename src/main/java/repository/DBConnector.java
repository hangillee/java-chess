package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String SERVER = "localhost:13306"; // MySQL 서버 주소
    private static final String TEST_SERVER = "localhost:13307"; // MySQL 테스트 서버 주소
    private static final String DATABASE = "chess"; // MySQL DATABASE 이름
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root"; //  MySQL 서버 아이디
    private static final String PASSWORD = "root"; // MySQL 서버 비밀번호

    private static Connection connection;

    private DBConnector() {
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getTestConnection() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + TEST_SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("DB 테스트 연결 오류:" + e.getMessage());
        }
    }
}
