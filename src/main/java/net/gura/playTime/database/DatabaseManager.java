package net.gura.playTime.database;

import net.gura.playTime.database.MySQL.MySQLManager;
import net.gura.playTime.database.MySQL.MySQLStorage;
import net.gura.playTime.database.SQLite.SQLiteManager;
import net.gura.playTime.database.SQLite.SQLiteStorage;
import net.gura.playTime.util.PlaytimeStorage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseManager {

    private final PlaytimeStorage playtimeStorage;

    public DatabaseManager(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();

        String typeName = config.getString("database.type", "SQLITE").toUpperCase();
        DatabaseType dbType;

        try {
            dbType = DatabaseType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid database type in config: " + typeName);
            plugin.getLogger().warning("Falling back to SQLITE.");
            dbType = DatabaseType.SQLITE;
        }

        switch (dbType) {
            case MYSQL -> {
                ConfigurationSection dbConfig = config.getConfigurationSection("database.mysql");
                MySQLManager mySQLManager = new MySQLManager(dbConfig);
                this.playtimeStorage = new MySQLStorage(mySQLManager);
                Bukkit.getLogger().info("[PlayTime] Using MySQL database.");
            }
            case SQLITE -> {
                SQLiteManager sqliteManager = new SQLiteManager(plugin);
                this.playtimeStorage = new SQLiteStorage(sqliteManager);
                Bukkit.getLogger().info("[PlayTime] Using SQLite database.");
            }
            default -> throw new IllegalStateException("Unhandled database type: " + dbType);
        }
    }

    public PlaytimeStorage getStorage() {
        return playtimeStorage;
    }
}