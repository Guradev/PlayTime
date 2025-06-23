package net.gura.playTime.commands;

import net.gura.playTime.util.PlaytimeManager;
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /playtime <set|delete> <player> [seconds]");
            return true;
        }
        String subCommand = args[0].toLowerCase();
        OfflinePlayer player = Bukkit.getPlayerExact(args[1]);
        UUID uuid = player.getUniqueId();

        switch (subCommand) {
            case "set" -> {
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /playtime set <player> <seconds>");
                    return true;
                }

                try {
                    long seconds = Long.parseLong(args[2]);
                    playtimeManager.setPlaytime(uuid, seconds);
                    sender.sendMessage("§aPlaytime for " + player.getName() + " set to " + seconds + " seconds.");
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid number: " + args[2]);
                }
            }

            case "delete" -> {
                playtimeManager.deletePlaytime(uuid);
                sender.sendMessage("§aPlaytime for " + player.getName() + " has been deleted.");
            }

            default -> sender.sendMessage("§cUse set or delete, invalid argument.");
        }
        return true;
    }
}
