package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private InventoryDao inventoryDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao);
        inventoryDao = new InventoryDaoJdbc(dataSource);
    }

    public void savePlayer(Player player, GameMap map) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
        inventoryDao.add(model);
        saveGameState(map, model);
    }

    public void updatePlayer(Player player, GameMap map) {
        PlayerModel model = new PlayerModel(player);
        int playerId = playerDao.getIdByName(player.getName());
        model.setId(playerId);
        playerDao.update(model);
        inventoryDao.update(model);
        updateGameState(map, model);
    }

    public void saveGameState(GameMap map, PlayerModel playerModel) {
        String currentMap = new Gson().toJson(map);
        java.util.Date utilDate = new java.util.Date();
        Date savedAt = new java.sql.Date(utilDate.getTime());
        GameState gameState = new GameState(currentMap, savedAt, playerModel);
        gameStateDao.add(gameState);
    }

    public void updateGameState(GameMap map, PlayerModel playerModel) {
        String currentMap = new Gson().toJson(map);
        java.util.Date utilDate = new java.util.Date();
        Date savedAt = new java.sql.Date(utilDate.getTime());
        GameState gameState = new GameState(currentMap, savedAt, playerModel);
        int gameStateId = gameStateDao.getGameStateIdByPlayerName(map.getPlayer().getName());
        gameState.setId(gameStateId);
        gameStateDao.update(gameState);
    }

    public Player loadPlayer() {
        throw new IllegalArgumentException();
    }

    public GameMap loadMap() {
        throw new IllegalArgumentException();
    }

    public boolean checkName(String name){
        int id = playerDao.getIdByName(name);
        return playerDao.get(id) != null;
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("PSQL_DB_NAME");
        String user = System.getenv("PSQL_USER_NAME");
        String password = System.getenv("PSQL_PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }

    public PlayerDao getPlayerDao(){
        return playerDao;
    }
}
