package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.actors.Monster;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.utilities.Randomizer;

import java.util.ArrayList;
import java.util.List;


public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public void moveMonsters(){
        for(Cell[] row: cells){
            for(Cell col: row){
                Monster monster;
                if(col.getActor() != player){
                    monster = (Monster)(col.getActor());
                }
                else monster = null;
                if(monster instanceof Skeleton){
                    monster.hitHero(this);
                    int[] dir = Randomizer.chooseDirection();
                    if(!Monster.hasMoved.contains(monster)) {
                        monster.move(dir[0], dir[1]);
                        Monster.hasMoved.add(monster);
                    }
                }else if(monster instanceof Wizard){
                    monster.hitHero(this);
                    int[] place;
                    if(!Monster.hasMoved.contains(monster)){
                        if(Randomizer.random.nextInt(5) < 5){
                            place = Randomizer.chooseDirection();
                            monster.move(place[0], place[1]);
                        }
                        else{
                            ((Wizard)monster).teleport(Randomizer.getRandomCell(cells));
                        }
                        Monster.hasMoved.add(monster);
                    }
                }
            }
        }
        Monster.hasMoved.clear();
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getCells() {
        return cells;
    }
}
