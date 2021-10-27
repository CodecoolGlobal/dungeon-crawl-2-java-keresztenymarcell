package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface InventoryDao {

    void add(PlayerModel player);
    void update(PlayerModel player);
    List<Item> getInventory(int id);

}