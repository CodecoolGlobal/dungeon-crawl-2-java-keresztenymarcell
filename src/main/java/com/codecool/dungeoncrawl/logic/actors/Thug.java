package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Thug extends Monster {

    public Thug(Cell cell) {
        super(cell);
        setAttack(5);
        setHealth(30);
    }

    @Override
    public void move(int dx, int dy) {
    }

    @Override
    public String getTileName() {
        return "thug";
    }
}
