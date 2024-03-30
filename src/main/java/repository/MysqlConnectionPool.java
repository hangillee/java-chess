package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlConnectionPool implements ConnectionPool {
    private static final String SERVER = "jdbc:mysql://localhost:13306/";
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final int INITIAL_CONNECTION_POOL_SIZE = 10;

    private final List<Connection> connections;
    private final List<Connection> usedConnections;

    private MysqlConnectionPool(final List<Connection> connections) {
        this.connections = connections;
        this.usedConnections = new ArrayList<>();
    }

    public static MysqlConnectionPool create(final String database, final String username, final String password)
            throws SQLException {
        final List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < INITIAL_CONNECTION_POOL_SIZE; i++) {
            connections.add(DriverManager.getConnection(SERVER + database + OPTION, username, password));
        }
        return new MysqlConnectionPool(connections);
    }

    @Override
    public Connection getConnection() {
        final Connection connection = connections.remove(connections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(final Connection connection) {
        connections.add(connection);
        return usedConnections.remove(connection);
    }
}
