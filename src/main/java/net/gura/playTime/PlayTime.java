package net.gura.playTime;

import net.gura.playTime.commands.Playtime;
import net.gura.playTime.configs.ConfigManager;
import net.gura.playTime.database.MySQL.MySQLStorage;
import net.gura.playTime.papi.PlaytimeExpansion;
import net.gura.playTime.util.PlaytimeManager;
import net.gura.playTime.util.PlaytimeStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayTime extends JavaPlugin {

    ConfigManager config = new ConfigManager(this);

    PlaytimeStorage storage = new MySQLStorage(getConfig().getConfigurationSection("mysql"));
    PlaytimeManager playtimeManager = new PlaytimeManager(this, storage);

    @Override
    public void onEnable() {
        config.saveDefaultConfig();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaytimeExpansion(playtimeManager).register();
            Bukkit.getLogger().info("[PlayTime] PlaceholderAPI hooked successfully.");
        }

        getCommand("playtime").setExecutor(new Playtime());
        getServer().getConsoleSender().sendMessage("[PlayTime] plugin enabled");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[PlayTime] plugin disabled");

    }
}
