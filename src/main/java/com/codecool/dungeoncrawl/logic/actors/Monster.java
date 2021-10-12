package com.codecool.dungeoncrawl.logic.actors;
import com.codecool.dungeoncrawl.logic.Cell;

abstract public class Monster extends Actor{

    public Monster(Cell cell){
        super(cell);
    }

    public void attack(Cell playerCell) {
        Player player = (Player)playerCell.getActor();
        player.setHealth(player.getHealth() - this.getAttack());
    }

    @Override
    public void move(int dx, int dy) {

    }
}
