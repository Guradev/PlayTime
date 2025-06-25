package net.gura.playTime.configs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MessageHandler {

    private final JavaPlugin plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<String, String> messages = new HashMap<>();

    public MessageHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        loadMessages();

    }
    private void loadMessages() {
        File file = new File(plugin.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        try (InputStreamReader reader = new InputStreamReader(plugin.getResource("messages.yml"), StandardCharsets.UTF_8)) {
            FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
            config.setDefaults(defaultConfig);
        } catch (Exception ignored) {}

        for (String key : config.getKeys(true)) {
            if (!config.isConfigurationSection(key)) {
                messages.put(key, config.getString(key));
            }
        }
    }

    public Component get(String path, Map<String, String> placeholders) {
        String raw = messages.getOrDefault(path, "<red>Missing message: " + path);

        if (!raw.contains("<prefix>") && messages.containsKey("prefix")) {
            raw = "<prefix>" + raw;
        }

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            raw = raw.replace("<" + entry.getKey() + ">", entry.getValue());
        }

        // Recursive placeholder for prefix
        if (raw.contains("<prefix>") && messages.containsKey("prefix")) {
            raw = raw.replace("<prefix>", messages.get("prefix"));
        }

        return miniMessage.deserialize(raw);
    }

    public Component get(String path) {
        return get(path, Map.of());
    }
}
