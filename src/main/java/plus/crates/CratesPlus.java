package plus.crates;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import plus.crates.Handlers.MenuHandler;
import plus.crates.Handlers.PlayerHandler;
import plus.crates.commands.Crate;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.frameworks.Updater;

import java.util.HashMap;

public final class CratesPlus extends JavaPlugin {
    private static CratesPlus instance;
    public final DataManager lang = new DataManager(this, "lang");
    public final DataManager data = new DataManager(this, "crates");

    public static String chatPrefix;
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();


    @Override
    public void onEnable() {
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
        chatPrefix = ChatColor.translateAlternateColorCodes('&', lang.getConfig().getString("Prefix") + " ");
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