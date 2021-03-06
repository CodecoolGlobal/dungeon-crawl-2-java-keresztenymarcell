package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;


public abstract class Actor implements Drawable {
    protected transient Cell cell;
    protected int health = 10;
    protected int attack;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public abstract void move(int dx, int dy);

    abstract public void attack(Cell cell);

    public boolean checkIfCanMove(Cell nextCell){
        CellType type = nextCell.getType();
        return type != CellType.WALL
                && type != CellType.CLOSED_DOOR
                && type != CellType.EMPTY
                && nextCell.getActor() == null
                && type != CellType.TREE;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell){
        this.cell = cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void setAttack(int attack){
        this.attack = attack;
    }

    public int getAttack(){
        return attack;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "cell=" + cell +
                ", health=" + health +
                ", attack=" + attack +
                '}';
    }
}
