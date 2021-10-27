package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    GameMap map = new GameMap(5, 5, CellType.FLOOR);

    @Test
    void hasWon_PlayerStandsOnPrize_ReturnTrue(){
        Cell prize = map.getCell(0,0);
        prize.setType(CellType.PRIZE);
        Player player = new Player(map.getCell(0,1));

        assertTrue(player.hasWon(prize));
    }

    @Test
    void hasWon_PlayerNotStandsOnPrize_ReturnFalse(){
        Cell floor = map.getCell(0,0);
        floor.setType(CellType.PRIZE);
        Player player = new Player(map.getCell(0,1));

        assertTrue(player.hasWon(floor));
    }

    @Test
    void hasKey_PlayerHasKey_ReturnTrue(){
        Player player = new Player(map.getCell(0,1));
        Item key = new Key();
        player.addItemToInventory(key);

        assertTrue(player.hasKey());
    }

    @Test
    void hasKey_PlayerDontHaveKey_ReturnTrue() {
        Player player = new Player(map.getCell(0, 1));

        assertFalse(player.hasKey());
    }

    @Test
    void removeKey_removeKeyFromInventory(){
        Player player = new Player(map.getCell(0, 1));
        Item key = new Key();
        player.addItemToInventory(key);

        player.removeKey();

        assertFalse(player.hasKey());
    }

    @Test
    void addItemToInventory_PlayerHasItemAfterAdding(){
        Player player = new Player(map.getCell(0, 1));
        Key key = new Key();

        player.addItemToInventory(key);

        assertEquals(key, player.getInventory().get(0));
    }

    @Test
    void isAlive_playerHealthMoreThanZero_ReturnTrue(){
        Player player = new Player(map.getCell(0, 1));
        player.setHealth(30);

        assertTrue(player.isAlive());
    }

    @Test
    void isAlive_PlayerHealthZero(){
        Player player = new Player(map.getCell(0, 1));
        player.setHealth(0);

        assertFalse(player.isAlive());

    }



}
