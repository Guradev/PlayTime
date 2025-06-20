package net.gura.playTime.database.MySQL;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

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



        this.dataSource = new HikariDataSource(hikariConfig);
    }

}
