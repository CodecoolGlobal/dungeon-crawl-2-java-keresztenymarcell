package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Ufo extends Monster {

    public Ufo(Cell cell) {
        super(cell);
        setAttack(3);
        setHealth(20);
    }

    @Override
    public void move(int dx, int dy) {

    }

    @Override
    public String getTileName() {
        return "ufo";
    }
}
