package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.App;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.codecool.dungeoncrawl.logic.items.*;

import java.util.LinkedList;
import java.util.List;

public class Player extends Actor {

    private List<Item> inventory = new LinkedList<>();

    private Weapon weapon;


    public Player(Cell cell) {
        super(cell);
        setAttack(5);
        setHealth(50);
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if(checkIfCanMove(nextCell)){
            getCell().setActor(null);
            nextCell.setActor(this);
            setCell(nextCell);
        }else if(nextCell.getType() == CellType.CLOSED_DOOR && hasKey()){
            getCell().setActor(null);
            nextCell.setType(CellType.OPEN_DOOR);
            removeKey();
            nextCell.setActor(this);
            setCell(nextCell);
        }
        else if(nextCell.getActor() instanceof Monster){
            attack(nextCell);
        }

    }

    private boolean hasKey(){
        return this.inventory.stream().anyMatch(x -> x instanceof Key);
    }

    private void removeKey(){
        for(Item item: inventory){
            if(item instanceof Key){
                inventory.remove(item);
                break;
            }
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

    public void pickUpItem() {
        Item item = this.cell.getItem();
        if(item instanceof Apple){
            this.setHealth(getHealth() + ((Apple) item).getPlusHealth());
        }else{
            addItemToInventory(item);
        }
        this.cell.setItem(null);
    }
}
