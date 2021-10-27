package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

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

    public void savePlayer(Player player) {
        PlayerModel playerModel = new PlayerModel(player);
        playerDao.add(playerModel);
    }

    public void updatePlayer(Player player){
        PlayerModel playerModel = new PlayerModel(player);
        playerDao.update(playerModel);
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
