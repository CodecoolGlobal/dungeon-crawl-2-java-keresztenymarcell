package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, x, y) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "UPDATE player SET hp = ?, x = ?, y = ? WHERE player_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, player.getHp());
            statement.setInt(2, player.getX());
            statement.setInt(3, player.getY());
            statement.setString(4, player.getPlayerName());
            statement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try (Connection conn = dataSource.getConnection()){
            String sql = "SELECT player_name, hp, x, y FROM player WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                String playerName = rs.getString(1);
                int hp = rs.getInt(2);
                int x = rs.getInt(3);
                int y = rs.getInt(4);
                return new PlayerModel(playerName, hp, x ,y);
            }
            else{
                return null;
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try(Connection conn = dataSource.getConnection()){
            String sql = "SELECT player_name, hp, x, y FROM player";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<PlayerModel> result = new ArrayList<>();
            while (rs.next()) {
                PlayerModel player = new PlayerModel(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
                result.add(player);
            }
            return result;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getIdByName(String name) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id FROM player WHERE player_name = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
             if(!rs.next()){
                 return -1;
             }
             return rs.getInt(1);

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


}
