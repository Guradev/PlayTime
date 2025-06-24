package net.gura.playTime.commands;

import net.gura.playTime.util.PlaytimeManager;
import net.gura.playTime.util.TimeFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor {

    private final PlaytimeManager playtimeManager;

    public PlaytimeCommand(PlaytimeManager playtimeManager) {
        this.playtimeManager = playtimeManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            if (!sender.hasPermission("playtime.view")) {
                sender.sendMessage(Component.text("You do not have permission to do this!").color(NamedTextColor.RED));
                return true;
            }

            Player player = (Player) sender;
            long seconds = playtimeManager.getPlaytime(player.getUniqueId());
            sender.sendMessage(Component.text("Your playtime is " + TimeFormat.formatPlaytime(seconds)).color(NamedTextColor.GREEN));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /playtime <get|set|delete> <player> [seconds]").color(NamedTextColor.RED));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        UUID uuid = target.getUniqueId();

        if (subCommand.equals("get")) {
            if (!sender.hasPermission("playtime.view.others")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.").color(NamedTextColor.RED));
                return true;
            }

            if (!target.hasPlayedBefore()) {
                sender.sendMessage(Component.text(target.getName() + " has not joined the server.").color(NamedTextColor.RED));
                return true;
            }

            long seconds = playtimeManager.getPlaytime(uuid);
            sender.sendMessage(Component.text(target.getName() + "'s playtime is " + TimeFormat.formatPlaytime(seconds)).color(NamedTextColor.GREEN));
            return true;
        }

        if (!sender.hasPermission("playtime.admin")) {
            sender.sendMessage(Component.text("You do not have permission to do this!").color(NamedTextColor.RED));
            return true;
        }

        switch (subCommand) {
            case "set" -> {
                if (args.length < 3) {
                    sender.sendMessage(Component.text("Usage: /playtime set <player> <seconds>").color(NamedTextColor.RED));
                    return true;
                }

                Player onlinePlayer = Bukkit.getPlayer(uuid);
                if (onlinePlayer == null || !onlinePlayer.isOnline()) {
                    sender.sendMessage(Component.text(target.getName() + " must be online to set playtime.").color(NamedTextColor.RED));
                    return true;
                }

                try {
                    long seconds = Long.parseLong(args[2]);
                    playtimeManager.setPlaytime(uuid, seconds);
                    sender.sendMessage(Component.text("Playtime for " + target.getName() + " set to " + seconds + " seconds.").color(NamedTextColor.GREEN));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("You typed an invalid number. Try again").color(NamedTextColor.RED));
                }
            }

            case "delete" -> {
                playtimeManager.deletePlaytime(uuid);
                sender.sendMessage(Component.text("Playtime for " + target.getName() + " deleted.").color(NamedTextColor.GREEN));
            }

            default -> sender.sendMessage(Component.text("Invalid subcommand. Use: set, delete or get").color(NamedTextColor.RED));
        }
        return true;
    }
}