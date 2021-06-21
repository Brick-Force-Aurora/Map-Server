package org.playuniverse.brickforce.maprepository.storage.file;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.playuniverse.brickforce.maprepository.storage.IStorageIndex;
import org.playuniverse.brickforce.maprepository.storage.database.OrderDirection;
import org.playuniverse.brickforce.maprepository.storage.database.sqlite.SqliteFile;
import org.playuniverse.brickforce.maprepository.storage.utils.IndexAction;
import org.playuniverse.brickforce.maprepository.storage.utils.SortType;
import org.playuniverse.console.Console;

public class FileIndex implements IStorageIndex {

    private final SqliteFile data;
    private final Console console;

    public FileIndex(Console console, File file) {
        this.data = new SqliteFile(file);
        this.console = console;
        setup();
    }

    public SqliteFile getData() {
        return data;
    }

    private void setup() {
        try (Connection connection = data.getConnection()) {
            Statement statement = connection.createStatement();
            statement.addBatch("CREATE TABLE IF NOT EXISTS `Index`(`Id` BIGINT(255) UNSIGNED UNIQUE, `Name` VARCHAR(128), `Version` INT(4))");
            statement.executeBatch();
        } catch (SQLException exception) {
            console.log(exception);
        }
    }

    @Override
    public long[] getMapIds(int offset, int amount, SortType type, OrderDirection direction) {
        try (Connection connection = data.getConnection()) {
            ResultSet result = connection.createStatement().executeQuery(
                "SELECT * FROM `Index` ORDER BY `" + type.getType() + "` " + direction.getType() + "LIMIT " + amount + " OFFSET " + offset);
            long[] output = new long[amount];
            int index = 0;
            while (result.next()) {
                output[index++] = result.getLong(1);
            }
            if (index == amount) {
                return output;
            }
            long[] resized = new long[index];
            System.arraycopy(output, 0, resized, 0, index);
            return resized;
        } catch (SQLException exception) {
            console.log(exception);
        }
        return null;
    }

    @Override
    public String[] getMapNames(int offset, int amount, SortType type, OrderDirection direction) {
        try (Connection connection = data.getConnection()) {
            ResultSet result = connection.createStatement().executeQuery(
                "SELECT * FROM `Index` ORDER BY `" + type.getType() + "` " + direction.getType() + "LIMIT " + amount + " OFFSET " + offset);
            String[] output = new String[amount];
            int index = 0;
            while (result.next()) {
                output[index++] = result.getString(2);
            }
            if (index == amount) {
                return output;
            }
            String[] resized = new String[index];
            System.arraycopy(output, 0, resized, 0, index);
            return resized;
        } catch (SQLException exception) {
            console.log(exception);
        }
        return null;
    }

    @Override
    public void update(long id, String name, IndexAction action) {
        try (Connection connection = data.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getStatement(action));
            if (action == IndexAction.DELETE) {
                statement.setLong(1, id);
            } else {
                statement.setString(1, name);
                statement.setLong(2, id);
            }
            statement.executeUpdate();
        } catch (SQLException exception) {
            console.log(exception);
        }
    }

    @Override
    public String getStatement(IndexAction action) {
        switch (action) {
        case DELETE:
            return "DELETE FROM `Index` WHERE `Id` = ?";
        case INSERT:
            return "INSERT INTO `Index`(`Name`, `Id`) VALUES (?, ?)";
        case UPDATE:
            return "UPDATE `Index` SET `Name` = ? WHERE `Id` = ?";
        default:
            return null;
        }
    }

}
