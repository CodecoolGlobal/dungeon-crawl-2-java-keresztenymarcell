package com.codecool.dungeoncrawl.logic.utilities;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Weapon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImportExport {

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Actor.class, new PropertyBasedInterfaceMarshal())
            .registerTypeAdapter(Weapon.class, new PropertyBasedInterfaceMarshal())
            .registerTypeAdapter(Item.class, new PropertyBasedInterfaceMarshal()).create();

    public void exportToFile(GameMap gameMap, String name) throws IOException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String path = s + "/export/" + name + ".json";
        File file = new File(path);
        FileWriter f1 = new FileWriter(file);

        gson.toJson(gameMap, f1);
        f1.flush();
    }



    public void importFromFile(String name) throws IOException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String path = s + "/export/" + name + ".json";

        Reader reader = Files.newBufferedReader(Paths.get(name));
        GameMap gameMap = gson.fromJson(reader, GameMap.class);
        System.out.println(gameMap);





    }

}
