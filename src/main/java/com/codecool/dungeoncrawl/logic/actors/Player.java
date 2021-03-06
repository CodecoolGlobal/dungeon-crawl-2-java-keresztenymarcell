package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.logic.utilities.Display;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.w3c.dom.ls.LSOutput;

import java.util.LinkedList;
import java.util.List;

public class Player extends Actor {
    private String name;

    private List<Item> inventory = new LinkedList<>();

    private Weapon weapon;

    public Player(Cell cell) {
        super(cell);
        setAttack(5);
        setHealth(100);
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if(checkIfCanMove(nextCell)){
            if(hasWon(nextCell)){
                Display.showGameOverMessage("You have won!");
            }
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

    public boolean hasWon(Cell cell){
        return cell.getType() == CellType.PRIZE;
    }

    public boolean hasKey(){
        return this.inventory.stream().anyMatch(x -> x instanceof Key);
    }

    public void removeKey(){
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
        }else if(item != null){
            addItemToInventory(item);
            if(item instanceof Weapon) weapon = (Weapon)item;
        }
        this.cell.setItem(null);

    }

    public boolean isAlive(){
        return health > 0;
    }

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
