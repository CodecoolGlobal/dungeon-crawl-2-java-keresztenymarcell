package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {

            String sql = "INSERT INTO game_state (id, current_map, saved_at, player_id) VALUES (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, state.getId());
            st.setString(2, state.getCurrentMap());
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
            String sql = "UPDATE game_state SET id = ?, current_map = ?, saved_at = ? WHERE player_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, state.getId());
            st.setString(2, state.getCurrentMap());
            st.setDate(3, state.getSavedAt());
            st.setInt(4, state.getPlayer().getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        return null;
    }

    @Override
    public List<GameState> getAll() {
        return null;
    }
}
