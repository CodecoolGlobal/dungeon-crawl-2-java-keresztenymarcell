package com.codecool.dungeoncrawl.logic.utilities;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class ImportExport {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Actor.class, new PropertyBasedInterfaceMarshal())
            .registerTypeAdapter(Weapon.class, new PropertyBasedInterfaceMarshal())
            .registerTypeAdapter(Item.class, new PropertyBasedInterfaceMarshal()).create();

    public void export(GameMap gameMap) throws IOException {
        gson.toJson(gameMap, new FileWriter("export1.txt"));

    }


}
