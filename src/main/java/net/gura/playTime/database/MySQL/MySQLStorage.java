package net.gura.playTime.database.MySQL;

import net.gura.playTime.util.PlaytimeStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLStorage implements PlaytimeStorage {

    private final MySQLManager manager;

    public MySQLStorage(MySQLManager manager) {
        this.manager = manager;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS playtime (uuid VARCHAR(36) PRIMARY KEY, seconds BIGINT)")) {
            stmt.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[Playtime] The following error ocurred while creating the table: " + e.getMessage());
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
        try (Connection conn = manager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO playtime (uuid, seconds) VALUES (?, ?) ON DUPLICATE KEY UPDATE seconds = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.setLong(2, seconds);
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
}
