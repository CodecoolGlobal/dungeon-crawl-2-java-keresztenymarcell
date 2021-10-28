package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {
    GameMap gameMap = new GameMap(3, 3, CellType.FLOOR);

    @Test
    void moveUpdatesCells() {
        Player player = new Player(gameMap.getCell(1, 1));
        player.move(1, 0);

        assertEquals(2, player.getX());
        assertEquals(1, player.getY());
        assertNull(gameMap.getCell(1, 1).getActor());
        assertEquals(player, gameMap.getCell(2, 1).getActor());
    }

    @Test
    void cannotMoveIntoWall() {
        gameMap.getCell(2, 1).setType(CellType.WALL);
        Player player = new Player(gameMap.getCell(1, 1));
        player.move(1, 0);

        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    void cannotMoveOutOfMap() {
        Player player = new Player(gameMap.getCell(2, 1));
        player.move(1, 0);

        assertEquals(2, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    void cannotMoveIntoAnotherActor() {
        Player player = new Player(gameMap.getCell(1, 1));
        Skeleton skeleton = new Skeleton(gameMap.getCell(2, 1));
        player.move(1, 0);

        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
        assertEquals(2, skeleton.getX());
        assertEquals(1, skeleton.getY());
        assertEquals(skeleton, gameMap.getCell(2, 1).getActor());
    }

    @Test
    void checkIfCanMove_NextCellInputIsEmptyCell_returnFalse(){
        Cell empty = gameMap.getCell(2,1);
        empty.setType(CellType.EMPTY);
        Actor player = new Player(gameMap.getCell(2,2));

        assertFalse(player.checkIfCanMove(empty));
    }

    @Test
    void checkIfCanMove_NextCellInputIsClosedDoor_returnFalse(){
        Cell closedDoor = gameMap.getCell(0,0);
        closedDoor.setType(CellType.CLOSED_DOOR);
        Actor player = new Player(gameMap.getCell(2,2));


        assertFalse(player.checkIfCanMove(closedDoor));
    }

    @Test
    void checkIfCanMove_nextCellInputIsWall_returnFalse(){
        Cell wall = gameMap.getCell(1,0);
        wall.setType(CellType.WALL);
        Actor player = new Player(gameMap.getCell(2,2));

        assertFalse(player.checkIfCanMove(wall));
    }

    @Test
    void checkIfCanMove_nextCellInputIsTree_returnFalse(){
        Cell tree = gameMap.getCell(1,1);
        tree.setType(CellType.TREE);
        Actor player = new Player(gameMap.getCell(2,2));

        assertFalse(player.checkIfCanMove(tree));
    }

    @Test
    void checkIfCanMove_nextCellInputIsFloor_returnTrue(){
        Cell floor = gameMap.getCell(1,1);
        floor.setType(CellType.FLOOR);
        Actor player = new Player(gameMap.getCell(2,2));

        assertTrue(player.checkIfCanMove(floor));


    }
}