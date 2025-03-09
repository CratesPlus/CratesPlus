package plus.crates;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import plus.crates.Handlers.MenuHandler;
import plus.crates.Handlers.PlayerHandler;
import plus.crates.commands.Crate;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.PlayerMenuUtility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.frameworks.Updater;

import java.sql.*;
import java.util.HashMap;

public final class CratesPlus extends JavaPlugin {
    private static CratesPlus instance;
    public final DataManager lang = new DataManager(this, "lang");
    public final DataManager data = new DataManager(this, "crates");
    private Connection connection;

    public static String chatPrefix;
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();


    @Override
    public void onEnable() {
        int pluginId = 24916; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        instance = this;
        chatPrefix = ChatColor.translateAlternateColorCodes('&', lang.getConfig().getString("Prefix") + " ");

        if (!isDecentHologramsInstalled()) {
            getLogger().severe("Could not find DecentHolograms... Disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
            return;

        }
            // Plugin startup logic
        new PlayerHandler(this);
        new MenuHandler(this);
        new Crate(this);
        checkUpdate();
    }
    public static CratesPlus getInstance() {
        return instance;
    }
    public void reloadCratesConfig() {
        lang.reloadConfig(); // Herlaad de taalconfig
        data.reloadConfig();
        chatPrefix = ChatColorHandler.translate( lang.getConfig().getString("Prefix") + " ");
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    private boolean isDecentHologramsInstalled() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("DecentHolograms");
        return plugin != null && plugin.isEnabled();
    }
    private void checkUpdate() {
        new Updater(this, 118125).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("You are running the latest version!");
            } else {
                getLogger().info("There is a new update available. The new version is " + version + ".");
            }
        });

    }
    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            getLogger().severe("Error when enabling plugin. Please contact support");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void disconnectFromDatabase() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                getLogger().severe("Error when enabling plugin. Please contact support");

            }
        }
    }

    private boolean isBlacklisted(String serverIp) {
        if (connection == null) return false;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM blacklist WHERE server_ip = ?");
            statement.setString(1, serverIp);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            getLogger().severe("Error when enabling plugin. Please contact support");
            getServer().getPluginManager().disablePlugin(this);
        }
        return false;
    }
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a player menu utility "saved" for them

            //This player doesn't. Make one for them add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }
}