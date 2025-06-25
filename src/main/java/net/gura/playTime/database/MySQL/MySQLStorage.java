package net.gura.playTime.database.MySQL;

import net.gura.playTime.util.PlaytimeStorage;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class MySQLStorage implements PlaytimeStorage {

    private final MySQLManager manager;

    public MySQLStorage(MySQLManager manager) {
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
            Bukkit.getLogger().severe("[Playtime] The following error ocurred while creating the table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public long getPlaytime(UUID uuid) {
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT seconds FROM playtime WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("seconds");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[Playtime] Error retrieving playtime for " + uuid + ": " + e.getMessage());
        }
        return 0L;
    }

    @Override
    public void savePlaytime(UUID uuid, long seconds) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();

        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO playtime (uuid, name, seconds) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE seconds = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, name != null ? name : "Unknown");
            stmt.setLong(3, seconds);
            stmt.executeUpdate();

        } catch (SQLException e) {
            Bukkit.getLogger().severe("[Playtime] There was an error when saving the playtime for " + uuid + ": " + e.getMessage());
        }
    }

    @Override
    public void deletePlaytime(UUID uuid) {
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM playtime WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[Playtime] There was an error when deleting the playtime for " + uuid + ": " + e.getMessage());
        }
    }

    public MySQLManager getManager() {
        return manager;
    }
}
