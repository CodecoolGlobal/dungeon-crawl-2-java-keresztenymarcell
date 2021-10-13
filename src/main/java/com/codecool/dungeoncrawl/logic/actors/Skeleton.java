package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;


public class Skeleton extends Monster {
    public Skeleton(Cell cell) {
        super(cell);
        setAttack(2);
        setHealth(8);
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if(checkIfCanMove(nextCell)){
            getCell().setActor(null);
            nextCell.setActor(this);
            setCell(nextCell);
        }
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
