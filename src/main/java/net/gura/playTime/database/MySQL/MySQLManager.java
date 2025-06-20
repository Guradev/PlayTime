package net.gura.playTime.database.MySQL;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLManager {

    private final HikariDataSource dataSource;

    public MySQLManager(ConfigurationSection config) {
        HikariConfig hikariConfig = new HikariConfig();

        String host = config.getString("host", "localhost");
        String port = config.getString("port", "3306");
        String database = config.getString("database", "playtime");
        String username = config.getString("username", "root");
        String password = config.getString("password", "");

        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database+ "?useSSL=false&autoReconnect=true");

        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setLeakDetectionThreshold(5000);

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public boolean isHealthy() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}