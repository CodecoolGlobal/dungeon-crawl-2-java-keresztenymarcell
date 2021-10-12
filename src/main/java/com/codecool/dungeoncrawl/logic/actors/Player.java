package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;

import java.util.LinkedList;
import java.util.List;

public class Player extends Actor {

    private List<Item> inventory = new LinkedList<>();

    private Weapon weapon;

    public Player(Cell cell) {
        super(cell);
        setAttack(5);
    }

    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if(nextCell.getType() != CellType.WALL && nextCell.getActor() == null){
            getCell().setActor(null);
            nextCell.setActor(this);
            setCell(nextCell);
        }
        else if(nextCell.getActor() instanceof Monster){
            attack(nextCell);
        } else if (nextCell.getItem() != null && nextCell.getItem() instanceof Key) {
            cell.setActor(null);
            nextCell.setActor(this);
            addItemToInventory(nextCell.getItem());
            cell = nextCell;
        }
    }

    public void attack(Cell nextCell){
        int actualAttack = this.getAttack();
        if(weapon != null)
            actualAttack += weapon.getAttack();
        Actor enemy = nextCell.getActor();
        enemy.setHealth(enemy.getHealth() - actualAttack);
        if(enemy.getHealth() <= 0)
            nextCell.setActor(null);
        else{
            enemy.attack(this.getCell());
        }
    }

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public void removeItemFromInventory(Item item) {
        if (item != null) {
            inventory.remove(item);
        }
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public String getTileName() {
        return "player";
    }
}
