package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Monster {
    public Skeleton(Cell cell) {
        super(cell);
        setAttack(2);
        setHealth(8);
    }



    @Override
    public String getTileName() {
        return "skeleton";
    }
}
