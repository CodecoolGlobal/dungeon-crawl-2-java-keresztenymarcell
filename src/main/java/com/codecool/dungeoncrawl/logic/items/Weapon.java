package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

abstract public class Weapon extends Item {
    private int attack;

    public Weapon(Cell cell) {
        super(cell);
    }

    public void setAttack(int attack){
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }
}
