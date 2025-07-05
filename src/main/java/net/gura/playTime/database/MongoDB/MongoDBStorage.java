package net.gura.playTime.database.MongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import net.gura.playTime.util.PlaytimeStorage;

import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.UUID;

public class MongoDBStorage implements PlaytimeStorage {

    private final MongoDBManager manager;
    private final MongoCollection<Document> collection;

    public MongoDBStorage(MongoDBManager manager) {
        this.manager = manager;
        this.collection = manager.getDatabase().getCollection("playtime");
        createIndexesIfNotExists();
    }

    private void createIndexesIfNotExists() {
        // MongoDB crea collections en insert
        collection.createIndex(Indexes.ascending("name"));
    }

    @Override
    public long getPlaytime(UUID uuid) {
        try {
            Document doc = collection.find(Filters.eq("uuid", uuid.toString())).first();
            if (doc != null) {
                return doc.getLong("seconds");
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("[PlayTime] Failed to load playtime for " + uuid + ": " + e.getMessage());
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public void savePlaytime(UUID uuid, long seconds) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();

        Document doc = new Document("uuid", uuid.toString())
                .append("name", name != null ? name : "Unknown")
                .append("seconds", seconds);
        try {
            collection.replaceOne(
                    Filters.eq("uuid", uuid.toString()),
                    doc,
                    new ReplaceOptions().upsert(true)
            );
        } catch (Exception e) {
            Bukkit.getLogger().severe("[PlayTime] Failed to save playtime for " + uuid + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deletePlaytime(UUID uuid) {
        try {
            collection.deleteOne(Filters.eq("uuid", uuid.toString()));
        } catch (Exception e) {
            Bukkit.getLogger().severe("[PlayTime] Failed to delete playtime for " + uuid + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public MongoDBManager getManager() {
        return manager;
    }
}
