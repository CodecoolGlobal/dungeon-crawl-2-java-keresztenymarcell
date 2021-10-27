package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    GameMap map = new GameMap(5, 5, CellType.FLOOR);
    Player player;

    @BeforeEach
    void setupPlayer(){
         player = new Player(map.getCell(0, 0));
    }

    @Test
    void hasWon_PlayerStandsOnPrize_ReturnTrue(){
        Cell prize = map.getCell(0,0);
        prize.setType(CellType.PRIZE);

        assertTrue(player.hasWon(prize));
    }

    @Test
    void hasWon_PlayerNotStandsOnPrize_ReturnFalse(){
        Cell floor = map.getCell(0,0);
        floor.setType(CellType.PRIZE);

        assertTrue(player.hasWon(floor));
    }

    @Test
    void hasKey_PlayerHasKey_ReturnTrue(){
        Item key = new Key();
        player.addItemToInventory(key);

        assertTrue(player.hasKey());
    }

    @Test
    void hasKey_PlayerDontHaveKey_ReturnTrue() {

        assertFalse(player.hasKey());
    }

    @Test
    void removeKey_removeKeyFromInventory(){
        Item key = new Key();
        player.addItemToInventory(key);

        player.removeKey();

        assertFalse(player.hasKey());
    }

    @Test
    void addItemToInventory_PlayerHasItemAfterAdding(){
        Key key = new Key();

        player.addItemToInventory(key);

        assertEquals(key, player.getInventory().get(0));
    }

    @Test
    void isAlive_playerHealthMoreThanZero_ReturnTrue(){
        player.setHealth(30);

        assertTrue(player.isAlive());
    }

    @Test
    void isAlive_PlayerHealthZero(){
        player.setHealth(0);

        assertFalse(player.isAlive());
    }

    @Test
    void pickUpItem_ItemIsApple_HealthPointAdded(){
        Item apple = new Apple(map.getCell(0,1));
        player.getCell().setItem(apple);
        player.pickUpItem();

        assertEquals(130, player.getHealth());

    }

    @Test
    void pickUpItem_ItemIsNotNullSoItIsAnItem_ItemAddedToInventory(){
        Item sword = new Sword(map.getCell(0, 1));
        player.getCell().setItem(sword);

        player.pickUpItem();

        assertEquals(sword, player.getInventory().get(0));
        assertNull(player.getCell().getItem());
    }

    @Test
    void attack_PlayerAttacksEnemy_HealthGetsLowerByAttackPower(){
        Skeleton enemy = new Skeleton(map.getCell(0,1));
        Cell nextCell = map.getCell(0,1);
        nextCell.setActor(enemy);

        player.attack(nextCell);

        assertEquals(3, enemy.getHealth());
    }

    @Test
    void attack_PlayerHaveWeapon_HealthGetsLowerByWeaponDamage(){
        Skeleton enemy = new Skeleton(map.getCell(0,1));
        Cell nextCell = map.getCell(0,1);
        Weapon sword = new Sword(map.getCell(0, 2));
        player.setWeapon(sword);
        nextCell.setActor(enemy);


        player.attack(nextCell);

        assertEquals(2, enemy.getHealth());
    }

    @Test
    void attack_PlayerKillsEnemy_EnemyEqualsNull(){
        Skeleton enemy = new Skeleton(map.getCell(0,1));
        Cell nextCell = map.getCell(0,1);
        nextCell.setActor(enemy);

        player.attack(nextCell);
        player.attack(nextCell);
        assertNull(nextCell.getActor());
    }



}
