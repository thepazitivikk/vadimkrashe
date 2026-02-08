package org.thepazitivik.krasheegorvadim.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DatabaseManager {

    private final Plugin plugin;
    private DatabaseProvider provider;

    public DatabaseManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        FileConfiguration config = plugin.getConfig();
        String type = config.getString("database.type", "sqlite").toLowerCase();

        try {
            if (type.equals("mysql")) {
                String host = config.getString("database.mysql.host", "localhost");
                int port = config.getInt("database.mysql.port", 3306);
                String database = config.getString("database.mysql.database", "minecraft");
                String username = config.getString("database.mysql.username", "root");
                String password = config.getString("database.mysql.password", "");

                provider = new MySQLProvider(host, port, database, username, password);
            } else {
                provider = new SQLiteProvider(plugin.getDataFolder());
            }

            provider.connect();
            createTables();
            plugin.getLogger().info("Database connected successfully using " + type.toUpperCase());
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to database", e);
        }
    }

    private void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS custom_items (" +
                "id VARCHAR(64) PRIMARY KEY," +
                "item_data TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    public void saveItem(String id, String serializedData) {
        String sql = "INSERT OR REPLACE INTO custom_items (id, item_data) VALUES (?, ?)";
        if (provider instanceof MySQLProvider) {
            sql = "REPLACE INTO custom_items (id, item_data) VALUES (?, ?)";
        }

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, serializedData);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save item: " + id, e);
        }
    }

    public String loadItem(String id) {
        String sql = "SELECT item_data FROM custom_items WHERE id = ?";

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("item_data");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load item: " + id, e);
        }
        return null;
    }

    public List<String> loadAllItemIds() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT id FROM custom_items";

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load item ids", e);
        }
        return ids;
    }

    public void deleteItem(String id) {
        String sql = "DELETE FROM custom_items WHERE id = ?";

        try (Connection conn = provider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to delete item: " + id, e);
        }
    }

    public void shutdown() {
        if (provider != null) {
            provider.disconnect();
        }
    }
}

