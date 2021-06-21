package org.playuniverse.brickforce.maprepository.storage;

import org.playuniverse.brickforce.maprepository.storage.database.OrderDirection;
import org.playuniverse.brickforce.maprepository.storage.utils.IndexAction;
import org.playuniverse.brickforce.maprepository.storage.utils.SortType;

public interface IStorageIndex {
    
    public static final OrderDirection DEFAULT_DIRECTION = OrderDirection.ASCENDING;
    public static final int PAGE_SIZE = 12;
    
    default long[] getMapIdsByPage(int page) {
        return getMapIdsByPage(page, DEFAULT_DIRECTION);
    }
    
    default long[] getMapIdsByPage(int page, OrderDirection direction) {
        return getMapIds(page * PAGE_SIZE, PAGE_SIZE, direction);
    }
    
    default long[] getMapIdsByPage(int page, SortType type, OrderDirection direction) {
        return getMapIds(page * PAGE_SIZE, PAGE_SIZE, type, direction);
    }
    
    default long[] getMapIds(int offset, int amount) {
        return getMapIds(offset, amount, DEFAULT_DIRECTION);
    }
    
    default long[] getMapIds(int offset, int amount, OrderDirection direction) {
        return getMapIds(offset, amount, SortType.ID, DEFAULT_DIRECTION);
    }
    
    long[] getMapIds(int offset, int amount, SortType type, OrderDirection direction);

    
    default String[] getMapNamesByPage(int page) {
        return getMapNamesByPage(page, DEFAULT_DIRECTION);
    }
    
    default String[] getMapNamesByPage(int page, OrderDirection direction) {
        return getMapNames(page * PAGE_SIZE, PAGE_SIZE, direction);
    }
    
    default String[] getMapNamesByPage(int page, SortType type, OrderDirection direction) {
        return getMapNames(page * PAGE_SIZE, PAGE_SIZE, type, direction);
    }
    
    default String[] getMapNames(int offset, int amount) {
        return getMapNames(offset, amount, DEFAULT_DIRECTION);
    }
    
    default String[] getMapNames(int offset, int amount, OrderDirection direction) {
        return getMapNames(offset, amount, SortType.NAME, DEFAULT_DIRECTION);
    }
    
    String[] getMapNames(int offset, int amount, SortType type, OrderDirection direction);
    
    void update(long id, String name, IndexAction action);
    
    String getStatement(IndexAction action);
    
}
