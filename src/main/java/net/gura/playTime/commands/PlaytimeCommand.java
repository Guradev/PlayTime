package net.gura.playTime.commands;

import net.gura.playTime.configs.MessageHandler;
import net.gura.playTime.util.PlaytimeManager;
import net.gura.playTime.util.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor {

    private final PlaytimeManager playtimeManager;
    private final MessageHandler messageHandler;
    Map<String, String> placeholders = new HashMap<>();

    public PlaytimeCommand(PlaytimeManager playtimeManager, MessageHandler messageHandler) {
        this.playtimeManager = playtimeManager;
        this.messageHandler = messageHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(messageHandler.get("only-players"));
                return true;
            }

            if (!sender.hasPermission("playtime.view")) {
                sender.sendMessage((messageHandler.get("no-permission")));
                return true;
            }

            Player player = (Player) sender;
            long seconds = playtimeManager.getPlaytime(player.getUniqueId());

            placeholders.put("playtime", TimeFormat.formatPlaytime(seconds));

            sender.sendMessage(messageHandler.get("success.view-own", placeholders));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(messageHandler.get("usage.base"));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        UUID uuid = target.getUniqueId();

        placeholders.put("player", target.getName());
        placeholders.put("playtime", TimeFormat.formatPlaytime(playtimeManager.getPlaytime(uuid)));

        if (subCommand.equals("get")) {
            if (!sender.hasPermission("playtime.view.others")) {
                sender.sendMessage(messageHandler.get("no-permission.others", placeholders));
                return true;
            }

            if (!target.hasPlayedBefore()) {
                sender.sendMessage(messageHandler.get("not-joined", placeholders));
                return true;
            }

            sender.sendMessage(messageHandler.get("success.view-other", placeholders));
            return true;
        }

        if (!sender.hasPermission("playtime.admin")) {
            sender.sendMessage(messageHandler.get("no-permission"));
            return true;
        }

        switch (subCommand) {
            case "set" -> {
                if (args.length < 3) {
                    sender.sendMessage(messageHandler.get("usage.set"));
                    return true;
                }

                Player onlinePlayer = Bukkit.getPlayer(uuid);
                if (onlinePlayer == null || !onlinePlayer.isOnline()) {
                    sender.sendMessage(messageHandler.get("not-online", placeholders));
                    return true;
                }

                try {
                    long seconds = Long.parseLong(args[2]);
                    playtimeManager.setPlaytime(uuid, seconds);

                    placeholders.put("playtime", TimeFormat.formatPlaytime(seconds));
                    placeholders.put("player", onlinePlayer.getName());

                    sender.sendMessage(messageHandler.get("success.set", placeholders));
                } catch (NumberFormatException e) {
                    sender.sendMessage(messageHandler.get("invalid-number"));
                }
            }

            case "delete" -> {
                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(messageHandler.get("not-joined", placeholders));
                    return true;
                }
                playtimeManager.deletePlaytime(uuid);
                sender.sendMessage(messageHandler.get("success.delete", placeholders));
            }
            default -> sender.sendMessage(messageHandler.get("error.invalid-subcommand"));
        }
        return true;
    }
}