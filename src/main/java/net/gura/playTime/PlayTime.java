package net.gura.playTime;

import net.gura.playTime.papi.PlaytimeExpansion;
import net.gura.playTime.util.PlaytimeManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayTime extends JavaPlugin {
    FileConfiguration config = getConfig();


    PlaytimeManager playtimeManager = new PlaytimeManager(this, // database manager required);

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaytimeExpansion(playtimeManager).register();

        }


        getServer().getConsoleSender().sendMessage("PlayTime plugin habilitado");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("PlayTime plugin desabilitado");

    }
}
