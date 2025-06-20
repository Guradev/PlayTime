package net.gura.playTime.database;

import net.gura.playTime.util.PlaytimeStorage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseManager {

    private final PlaytimeStorage playtimeStorage;

    public DatabaseManager(JavaPlugin plugin, PlaytimeStorage playtimeStorage) {
        this.playtimeStorage = playtimeStorage;
        FileConfiguration config = plugin.getConfig();

        String typeName = config.getString("database.type", "MYSQL").toUpperCase();
        DatabaseType dbType;

        try {
            dbType = DatabaseType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid database type in config: " + typeName);
            plugin.getLogger().warning("Falling back to MYSQL.");
            dbType = DatabaseType.MYSQL;
        }

        ConfigurationSection dbConfig = config.getConfigurationSection("database." + dbType.name().toLowerCase());

        switch (dbType) {
            case MYSQL:
                break;
            case MONGODB:
                break;
            default:
                throw new IllegalStateException("Unhandled database type: " + dbType);
        }
    }

    public PlaytimeStorage getStorage() {
        return playtimeStorage;
    }
}
