package net.gura.playTime;

import net.gura.playTime.commands.PlaytimeCommand;
import net.gura.playTime.configs.MessageHandler;
import net.gura.playTime.database.DatabaseManager;
import net.gura.playTime.database.MySQL.MySQLManager;
import net.gura.playTime.database.SQLite.SQLiteManager;
import net.gura.playTime.listener.PlayerListener;
import net.gura.playTime.papi.PlaytimeExpansion;
import net.gura.playTime.util.PlaytimeManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayTime extends JavaPlugin {

    private PlaytimeManager playtimeManager;
    private MessageHandler messageHandler;
    private SQLiteManager sqliteManager;
    private MySQLManager mySQLManager;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();

        messageHandler = new MessageHandler(this);
        DatabaseManager databaseManager = new DatabaseManager(this);
        playtimeManager = new PlaytimeManager(this, databaseManager.getStorage());

        getServer().getPluginManager().registerEvents(new PlayerListener(playtimeManager), this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getScheduler().runTaskLater(this, () -> {
                new PlaytimeExpansion(playtimeManager).register();
                Bukkit.getConsoleSender().sendMessage("[PlayTime] PlaceholderAPI hooked successfully.");
            }, 1L);
        } else {
            Bukkit.getConsoleSender().sendMessage("[PlayTime] PlaceholderAPI not found. PlaceholderAPI expansion disabled.");
        }

        getCommand("playtime").setExecutor(new PlaytimeCommand(playtimeManager, messageHandler));
        getServer().getConsoleSender().sendMessage("[PlayTime] Plugin enabled successfully");
    }

    @Override
    public void onDisable() {
        playtimeManager.saveAllSync();

        if (sqliteManager != null) {
            sqliteManager.close();
            getServer().getConsoleSender().sendMessage("[PlayTime] SQLite database connection closed successfully.");
        } else if (mySQLManager != null) {
            mySQLManager.close();
            getServer().getConsoleSender().sendMessage("[PlayTime] MySQL database connection closed successfully.");
        } else {
            getServer().getConsoleSender().sendMessage("[PlayTime] No database connection was found. This isn't intended behavior, please check your config.yml!");
        }
        getServer().getConsoleSender().sendMessage("[PlayTime] Plugin disabled successfully");
    }
}
