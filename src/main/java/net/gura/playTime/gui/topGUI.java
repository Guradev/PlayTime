package net.gura.playTime.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.gura.playTime.configs.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class topGUI {
    private final MessageHandler messageHandler;

    public topGUI(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void openGUI(Player user) {
        Inventory inv = Bukkit.createInventory(null, 54, messageHandler.get("gui's.top.title"));



        user.openInventory(inv);

    }

}
