package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Monster;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.utilities.Randomizer;


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
                    int[] dir = Randomizer.chooseDirection();
                    if(!Monster.haveMoved.contains(monster)) {
                        monster.move(dir[0], dir[1]);
                        Monster.haveMoved.add(monster);
                    }
                }
            }
        }
        Monster.haveMoved.clear();
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

}
