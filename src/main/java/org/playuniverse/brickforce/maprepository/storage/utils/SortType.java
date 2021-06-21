package org.playuniverse.brickforce.maprepository.storage.utils;

public enum SortType {

    ID("Id"),
    NAME("Name");

    private final String type;

    private SortType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
