package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Sword extends Weapon {

    public Sword(Cell cell){
        super(cell);
        setAttack(1);
    }

    public String getTileName(){
        return "sword";
    }
}
