package net.gura.playTime.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.gura.playTime.util.PlaytimeManager;
import net.gura.playTime.util.TimeFormat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaytimeExpansion extends PlaceholderExpansion {

    private final PlaytimeManager playtimeManager;

    public PlaytimeExpansion(PlaytimeManager playtimeManager) {
        this.playtimeManager = playtimeManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "playtime";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Gura";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1";
    }

    @Override
    public String onPlaceholderRequest(Player player, String args) {
        if (player == null) return "";
        long playtime = playtimeManager.getPlaytime(player.getUniqueId());

        switch(args.toLowerCase()) {
            case "seconds":
            case "minutes":
            case "hours":
            case "days":
                return TimeFormat.formatUnit(playtime, args);
            case "dynamic":
            default:
                return TimeFormat.formatPlaytime(playtime);
        }
    }
}
