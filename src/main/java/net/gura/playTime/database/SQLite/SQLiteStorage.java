package net.gura.playTime.database.SQLite;

import net.gura.playTime.util.PlaytimeStorage;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class SQLiteStorage implements PlaytimeStorage {

    private final SQLiteManager manager;

    public SQLiteStorage(SQLiteManager manager) {
        this.manager = manager;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS playtime (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "name VARCHAR(16) NOT NULL, " +
                "seconds BIGINT NOT NULL DEFAULT 0" +
                ");";

        String createIndexSQL = """
                CREATE INDEX IF NOT EXISTS idx_playtime_name ON playtime(name);
                """;

        try (Connection conn = manager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            stmt.execute(createIndexSQL);

        } catch (SQLException e) {
            Bukkit.getLogger().severe("[PlayTime] Failed to create playtime table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public long getPlaytime(UUID uuid) {
        String sql = "SELECT seconds FROM playtime WHERE uuid = ?;";
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("seconds");
                }
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe("[PlayTime] Failed to load playtime for " + uuid + ": " + e.getMessage());
        }
        return 0L;
    }

    @Override
    public void savePlaytime(UUID uuid, long seconds) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();

        String sql = "INSERT INTO playtime (uuid, name, seconds) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET seconds = excluded.seconds, name = excluded.name;";

        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            stmt.setString(2, name != null ? name : "Unknown");
            stmt.setLong(2, seconds);
            stmt.executeUpdate();

        } catch (SQLException e) {
            Bukkit.getLogger().severe("[PlayTime] Failed to save playtime for " + uuid + ": " + e.getMessage());
        }
    }

    @Override
    public void deletePlaytime(UUID uuid) {
        String sql = "DELETE FROM playtime WHERE uuid = ?;";
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            Bukkit.getLogger().severe("[PlayTime] Failed to delete playtime for " + uuid + ": " + e.getMessage());
        }
    }

    public SQLiteManager getManager() {
        return manager;
    }
}