package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.utilities.Randomizer;

public class Wizard extends Monster {

    public Wizard(Cell cell) {
        super(cell);
        setAttack(3);
        setHealth(20);
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

    public void teleport(Cell nextCell){
//        if(checkIfCanMove(nextCell)){
            getCell().setActor(null);
            nextCell.setActor(this);
            setCell(nextCell);
//        }
    }

    @Override
    public String getTileName() {
        return "wizard";
    }
}
