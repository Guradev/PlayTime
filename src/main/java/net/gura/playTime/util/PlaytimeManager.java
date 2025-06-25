package net.gura.playTime.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlaytimeManager {
    private final JavaPlugin plugin;
    // Caches en memoria
    private final Map<UUID, Long> playtimeCache = new ConcurrentHashMap<>();
    private final Map<UUID, Long> loginTimes = new ConcurrentHashMap<>();
    Map<String, String> placeholders = new HashMap<>();

    // Referencias al database handler
    private final PlaytimeStorage playtimeStorage;

    public PlaytimeManager(JavaPlugin plugin, PlaytimeStorage playtimeStorage) {
        this.plugin = plugin;
        this.playtimeStorage = playtimeStorage;
    }

    public void handleJoin(Player player) {
        UUID uuid = player.getUniqueId();
        loginTimes.put(uuid, System.currentTimeMillis());

        // Cargando playtime en async
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            long stored = playtimeStorage.getPlaytime(uuid);
            playtimeCache.put(uuid, stored);
        });
    }

    public void handleQuit(Player player) {
        UUID uuid = player.getUniqueId();
        long seconds = getSessionTime(uuid);
        playtimeCache.merge(uuid, seconds, Long::sum);
        long totalPlaytime = playtimeCache.get(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            playtimeStorage.savePlaytime(uuid, totalPlaytime);
        });
        loginTimes.remove(uuid);
    }

    public void saveAllSync() {
            for (Map.Entry<UUID, Long> entry : playtimeCache.entrySet()) {
                UUID uuid = entry.getKey();
                long time = entry.getValue() + getSessionTime(uuid);
                playtimeStorage.savePlaytime(uuid, time);
            }
    }

    // Get current playtime (includes session)
    public long getPlaytime(UUID uuid) {
        return playtimeCache.getOrDefault(uuid, 0L) + getSessionTime(uuid);
    }

    public void setPlaytime(UUID uuid, long seconds) {
        Player player = Bukkit.getPlayer(uuid);
        playtimeCache.put(uuid, seconds);

        if (player != null && player.isOnline()) {
            loginTimes.put(uuid, System.currentTimeMillis());
        }

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                playtimeStorage.savePlaytime(uuid, seconds);
        });
    }


    public void deletePlaytime(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        placeholders.put("player", Bukkit.getOfflinePlayer(uuid).getName());

        playtimeCache.remove(uuid);
        loginTimes.remove(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            playtimeStorage.deletePlaytime(uuid);
        });
        if (offlinePlayer.hasPlayedBefore()) {
            handleJoin(offlinePlayer.getPlayer());
        }
    }

    private long getSessionTime(UUID uuid) {
        if (!loginTimes.containsKey(uuid)) return 0L;
        long login = loginTimes.get(uuid);
        return (System.currentTimeMillis() - login) / 1000L;
    }
}
