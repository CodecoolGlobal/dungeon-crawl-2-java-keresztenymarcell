package com.codecool.dungeoncrawl.logic.utilities;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randomizer {

    public static final Random random = new Random();

    public static int[] chooseDirection(){
        int dir = random.nextInt(4) + 1;
        if(dir == 1) return new int[]{0, -1};
        else if(dir == 2) return new int[]{-1, 0};
        else if(dir == 3) return new int[]{0, 1};
        else return new int[]{1, 0};
    }

    public static Cell getRandomCell(Cell[][] cells){
        List<Cell> empties = new ArrayList<>();
        for(Cell[] row: cells){
            for(Cell col: row){
                if(col.getActor() == null && col.getType() == CellType.FLOOR){
                    empties.add(col);
                }
            }
        }
        return empties.get(random.nextInt(empties.size()));
    }
}
