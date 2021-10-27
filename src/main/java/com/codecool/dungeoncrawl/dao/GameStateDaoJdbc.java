package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;
    private PlayerDao playerDao;

    GameStateDaoJdbc(DataSource dataSource, PlayerDao playerDao) {
        this.dataSource = dataSource;
        this.playerDao = playerDao;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {

            String sql = "INSERT INTO game_state (current_map, discovered_maps, saved_at, player_id) VALUES (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, state.getCurrentMap());
            st.setString(2, String.join(", ", state.getDiscoveredMaps()));
            st.setDate(3, state.getSavedAt());
            st.setInt(4, state.getPlayer().getId());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            state.setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET id = ?, current_map = ?, discovered_maps = ?, saved_at = ? WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, state.getId());
            st.setString(2, state.getCurrentMap());
            st.setString(3, String.join(", ", state.getDiscoveredMaps()));
            st.setDate(4, state.getSavedAt());
            st.setInt(5, state.getPlayer().getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {      // id = player_id
        try (Connection conn = dataSource.getConnection()) {
            // FIRST STEP - read current_map and saved at according to player_id
            String sql = "SELECT id, current_map, discovered_maps, saved_at FROM game_state WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }

            int gameStateId = rs.getInt(1);
            String currentMap = rs.getString(2);
            List<String> discoveredMaps = Arrays.asList(rs.getString(3).split(", ", -1));
            Date savedAt = rs.getDate(4);

            // SECOND STEP - find Player with id we got as a result of the first query
            PlayerModel player = playerDao.get(id);

            // FINISH - create and return new GameState class instance
            GameState gameState = new GameState(currentMap, savedAt, player);
            discoveredMaps.forEach(gameState::addDiscoveredMap);
            gameState.setId(gameStateId);
            return gameState;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            // FIRST STEP - read book_id, author_id and title
            String sql = "SELECT id, current_map, discovered_maps, saved_at, player_id FROM game_state";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            List<GameState> result = new ArrayList<>();
            while (rs.next()) {
                // SECOND STEP - read all data from result set
                int gameStateId = rs.getInt(1);
                String currentMap = rs.getString(2);
                List<String> discoveredMaps = Arrays.asList(rs.getString(3).split(", ", -1));
                Date savedAt = rs.getDate(4);
                int playerId = rs.getInt(5);

                // THIRD STEP - find player with id == playerId
                PlayerModel player = playerDao.get(playerId);

                // FOURTH STEP - create a new GameState class instance and add it to result list.
                GameState gameState = new GameState(currentMap, savedAt, player);
                discoveredMaps.forEach(gameState::addDiscoveredMap);
                gameState.setId(gameStateId);
                result.add(gameState);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getGameStateIdByPlayerName(String name) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT game_state.id FROM game_state JOIN player ON player_id = player.id WHERE player_name = ?";
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
