package net.gura.playTime;

import net.gura.playTime.commands.PlaytimeCommand;
import net.gura.playTime.database.DatabaseManager;
import net.gura.playTime.listener.PlayerListener;
import net.gura.playTime.papi.PlaytimeExpansion;
import net.gura.playTime.util.PlaytimeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayTime extends JavaPlugin {

    private PlaytimeManager playtimeManager;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();

        DatabaseManager databaseManager = new DatabaseManager(this);
        playtimeManager = new PlaytimeManager(this, databaseManager.getStorage());

        // PlaceholderAPI expansion
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaytimeExpansion(playtimeManager).register();
            Bukkit.getLogger().info("[PlayTime] PlaceholderAPI hooked successfully.");
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(playtimeManager), this);

        getCommand("playtime").setExecutor(new PlaytimeCommand(playtimeManager));
        getServer().getConsoleSender().sendMessage("[PlayTime] Plugin enabled successfully");
    }

    @Override
    public void onDisable() {
        playtimeManager.saveAllSync();
        getServer().getConsoleSender().sendMessage("[PlayTime] Plugin disabled successfully");
    }

    public PlaytimeManager getPlaytimeManager() {
        return playtimeManager;
    }
}
