package net.gura.playTime.database.MongoDB;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bukkit.configuration.ConfigurationSection;

public class MongoDBManager {

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoDBManager(ConfigurationSection config) {
        String host = config.getString("host", "localhost");
        int port = Integer.parseInt(config.getString("port", "27017"));
        String dbName = config.getString("database", "playtime");
        String username = config.getString("username", "root");
        String password = config.getString("password", "");

        String uri;
        if (!username.isEmpty() && !password.isEmpty()) {
            uri = String.format("mongodb://%s:%s@%s:%d/%s", username, password, host, port, dbName);
        } else {
            uri = String.format("mongodb://%s:%d/%s", host, port, dbName);
        }

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(dbName);
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public boolean isHealthy() {
        try {
            database.listCollectionNames().first(); // Simple ping
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
