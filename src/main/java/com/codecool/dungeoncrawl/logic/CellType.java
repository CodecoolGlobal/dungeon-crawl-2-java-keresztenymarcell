package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    CLOSED_DOOR("closed"),
    OPEN_DOOR("open"),
    TREE("tree"),
    GRASS("grass"),
    BUSH("bush"),
    LATTER("latter"),
    HOUSE("house");


    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
