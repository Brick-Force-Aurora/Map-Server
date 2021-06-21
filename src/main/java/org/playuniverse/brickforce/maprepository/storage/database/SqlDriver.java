package org.playuniverse.brickforce.maprepository.storage.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class SqlDriver implements AutoCloseable {

    private final HikariConfig config = new HikariConfig();
    private HikariDataSource source;

    public SqlDriver() {}

    protected final void setup() {
        applyConfig(config);
        source = new HikariDataSource(config);
    }

    public final Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public final HikariDataSource getSource() {
        return source;
    }

    protected final HikariConfig getConfig() {
        return config;
    }

    public final boolean isClosed() {
        return source.isClosed();
    }

    @Override
    public final void close() throws Exception {
        onClose();
        source.close();
    }

    protected abstract void applyConfig(HikariConfig config);

    protected abstract void onClose() throws Exception;

}
