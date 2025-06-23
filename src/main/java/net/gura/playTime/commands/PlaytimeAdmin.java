package net.gura.playTime.commands;

import net.gura.playTime.util.PlaytimeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaytimeAdmin implements CommandExecutor {

    private final PlaytimeManager playtimeManager;

    public PlaytimeAdmin(PlaytimeManager playtimeManager) {
        this.playtimeManager = playtimeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String @NotNull [] args) {
        if (!sender.hasPermission("playtime.admin")) {
            sender.sendMessage((Component.text("You do not have permission to do this!")).color(NamedTextColor.RED));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /playtime <set|delete> <player> [seconds]").color(NamedTextColor.RED));
            return true;
        }
        String subCommand = args[0].toLowerCase();
        OfflinePlayer player = Bukkit.getPlayerExact(args[1]);
        UUID uuid = player.getUniqueId();

        switch (subCommand) {
            case "set" -> {
                if (args.length < 3) {
                    sender.sendMessage((Component.text("Usage: /playtime set <player> <seconds>").color(NamedTextColor.RED)));
                    return true;
                }

                try {
                    long seconds = Long.parseLong(args[2]);
                    playtimeManager.setPlaytime(uuid, seconds);
                    sender.sendMessage(Component.text("Playtime for " + player.getName() + " set to " + seconds + " seconds.").color(NamedTextColor.GREEN));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid number: "+ args[2]).color(NamedTextColor.RED));
                }
            }

            case "delete" -> {
                playtimeManager.deletePlaytime(uuid);
                sender.sendMessage(Component.text("Playtime for " + player.getName() + " deleted.").color(NamedTextColor.GREEN));
            }

            default -> sender.sendMessage(Component.text("Use set or delete, invalid argument."));
        }
        return true;
    }
}
