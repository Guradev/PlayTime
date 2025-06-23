package net.gura.playTime.commands;

import net.kyori.adventure.text.Component;
import net.gura.playTime.util.PlaytimeManager;
import net.gura.playTime.util.TimeFormat;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaytimeUser implements CommandExecutor {

    private final PlaytimeManager playtimeManager;

    public PlaytimeUser(PlaytimeManager manager) {
        this.playtimeManager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
        }
        if (!sender.hasPermission("playtime.user")) {
            sender.sendMessage((Component.text("You do not have permission to do this!")).color(NamedTextColor.RED));
            return true;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            long seconds = playtimeManager.getPlaytime(player.getUniqueId());
            sender.sendMessage((Component.text("Your playtime is " + TimeFormat.formatPlaytime(seconds)).color(NamedTextColor.GREEN)));
        }
        return true;
    }
}
