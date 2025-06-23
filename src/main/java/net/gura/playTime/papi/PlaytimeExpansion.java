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
    public @NotNull String getAuthor() {
        return "Gura";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "playtime";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return playtimeManager != null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String args) {
        if (player == null) return "";
        long playtime = playtimeManager.getPlaytime(player.getUniqueId());

        return switch (args.toLowerCase()) {
            case "seconds_full", "minutes_full", "hours_full", "days_full" -> TimeFormat.formatUnit(playtime, args.replace("_full", ""));
            case "seconds", "minutes", "hours", "days" -> TimeFormat.formatUnitSuffix(playtime, args);
            default -> TimeFormat.formatPlaytime(playtime);
        };
    }
}
