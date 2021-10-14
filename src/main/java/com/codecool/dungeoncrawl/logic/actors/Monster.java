package com.codecool.dungeoncrawl.logic.actors;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;

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

    public void hitHero(GameMap map){
        List<Actor> neighbors = new ArrayList<>();
        neighbors.add(map.getCells()[this.getCell().getX()][this.getCell().getY() + 1].getActor());
        neighbors.add(map.getCells()[this.getCell().getX()][this.getCell().getY() - 1].getActor());
        neighbors.add(map.getCells()[this.getCell().getX() + 1][this.getCell().getY()].getActor());
        neighbors.add(map.getCells()[this.getCell().getX() - 1][this.getCell().getY()].getActor());

        if(neighbors.contains(map.getPlayer())){
            map.getPlayer().setHealth(map.getPlayer().getHealth() - 1);
        }
    }

}
