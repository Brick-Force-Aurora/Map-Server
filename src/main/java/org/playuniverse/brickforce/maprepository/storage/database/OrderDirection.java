package org.playuniverse.brickforce.maprepository.storage.database;

public enum OrderDirection {

    ASCENDING("ASC"),
    DESCENDING("DESC");

    private final String type;

    private OrderDirection(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
