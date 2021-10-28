package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.utilities.PropertyBasedInterfaceMarshal;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDaoJdbc implements InventoryDao {

    private DataSource dataSource;
    private Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new PropertyBasedInterfaceMarshal()).create();

    public InventoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        String inventory = gson.toJson(player.getInventory());

        try(Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO inventory (player_id, inventory) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, player.getId());
            statement.setString(2, inventory);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        String inventory = new Gson().toJson(player.getInventory());
        try (Connection conn = dataSource.getConnection()){
            String sql = "UPDATE inventory SET inventory = ? WHERE player_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, inventory);
            statement.setInt(2, player.getId());
            statement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> getInventory(int id) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "SELECT inventory FROM inventory WHERE player_id = ?";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            String queryResult = rs.getString(1);

            Type listOfItemObject = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();

            return gson.fromJson(queryResult, listOfItemObject);
        }catch (SQLException e){
            throw new RuntimeException(e);

        }


    }


}
