package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Apple extends Item{

    private int plusHealth = 30;

    @Override
    public String getTileName() {
        return "apple";
    }

    public Apple(Cell cell){
        super(cell);
    }

    public int getPlusHealth() {
        return plusHealth;
    }
}
