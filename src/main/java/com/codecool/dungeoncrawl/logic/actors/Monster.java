package com.codecool.dungeoncrawl.logic.actors;
import com.codecool.dungeoncrawl.logic.Cell;

import java.util.ArrayList;
import java.util.List;

abstract public class Monster extends Actor{
    public static List<Monster> hasMoved = new ArrayList<>();

    public Monster(Cell cell){
        super(cell);
    }

    public void attack(Cell playerCell) {
        Player player = (Player)playerCell.getActor();
        player.setHealth(player.getHealth() - this.getAttack());
    }

    @Override
    public abstract void move(int dx, int dy);

}
