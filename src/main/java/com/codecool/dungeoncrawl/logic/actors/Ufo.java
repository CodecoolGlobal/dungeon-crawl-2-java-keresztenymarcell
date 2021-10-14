package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.utilities.Randomizer;

public class Ufo extends Monster {

    public Ufo(Cell cell) {
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
        System.out.println("tele");
//        if(checkIfCanMove(nextCell)){
            System.out.println("can");
            getCell().setActor(null);
            nextCell.setActor(this);
            setCell(nextCell);
//        }
    }

    @Override
    public String getTileName() {
        return "ufo";
    }
}
