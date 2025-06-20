package net.gura.playTime.util;

import java.util.UUID;

public interface PlaytimeStorage {
    long getPlaytime(UUID uuid);
    void savePlaytime(UUID uuid, long seconds);
    void deletePlaytime(UUID uuid);
}