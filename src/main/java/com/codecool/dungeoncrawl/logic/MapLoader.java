package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Thug;
import com.codecool.dungeoncrawl.logic.actors.Ufo;
import com.codecool.dungeoncrawl.logic.items.Apple;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String mapSource) {
        InputStream is = MapLoader.class.getResourceAsStream(mapSource);
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 'd':
                        case 'D':
                            cell.setType(CellType.CLOSED_DOOR);
                            break;
                        case 'T':
                            cell.setType(CellType.TREE);
                            break;
                        case ',':
                            cell.setType(CellType.GRASS);
                            break;
                        case 'a':
                            cell.setType(CellType.GRASS);
                            new Apple(cell);
                            break;
                        case 'b':
                            cell.setType(CellType.BUSH);
                            break;
                        case 'L':
                            cell.setType(CellType.LATTER);
                            break;
                        case 'H':
                            cell.setType(CellType.HOUSE);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case 'u':
                            cell.setType(CellType.FLOOR);
                            new Ufo(cell);
                            break;
                        case 'S':
                            cell.setType(CellType.FLOOR);
                            new Thug(cell);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case 'w':
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
