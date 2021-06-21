package org.playuniverse.brickforce.maprepository.storage.database.sqlite;

import java.io.File;

import org.playuniverse.brickforce.maprepository.storage.database.SqlDriver;

import com.zaxxer.hikari.HikariConfig;

public class SqliteFile extends SqlDriver {
    
    private final File file;
    
    public SqliteFile(File file) {
        this.file = file;
        setup();
    }

    @Override
    protected void applyConfig(HikariConfig config) {
        config.setJdbcUrl("jdbc:sqlite:" + file.getPath());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    @Override
    protected void onClose() throws Exception {
        
    }
    
}
