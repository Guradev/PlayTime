package net.gura.playTime.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void saveDefaultConfig() {
        plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    public void saveConfig() {
        plugin.saveConfig();
    }
    public void reloadConfig() {
        plugin.reloadConfig();
    }
}
