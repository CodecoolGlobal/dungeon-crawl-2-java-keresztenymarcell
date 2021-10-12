package com.codecool.dungeoncrawl.logic.actors;
import com.codecool.dungeoncrawl.logic.Cell;

abstract public class Monster extends Actor{

    public Monster(Cell cell){
        super(cell);
    }

    @Override
    public void move(int dx, int dy) {

    }
}
