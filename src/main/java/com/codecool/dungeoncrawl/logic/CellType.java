package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    TREE("tree"),
    GRASS("grass"),
    BUSH("bush"),
    LATTER("latter"),
    HOUSE("house"),
    CLOSED_DOOR("closedDoor");


    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
