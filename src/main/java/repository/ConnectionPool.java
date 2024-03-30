package repository;

import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection();

    boolean releaseConnection(final Connection connection);
}
