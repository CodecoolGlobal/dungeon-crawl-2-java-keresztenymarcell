package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Monster {
    public Skeleton(Cell cell) {
        super(cell);
    }

    @Override
    public void move(int dx, int dy) {

    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
