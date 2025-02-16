package plus.crates.frameworks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import plus.crates.CratesPlus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private final String name;
    private final CratesPlus plugin;

    public DataManager(CratesPlus plugin, String name) {
        this.plugin = plugin;
        this.name = name + ".yml";
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), name);
        }

        dataConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = plugin.getResource(name);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) {
            reloadConfig();
        }
        return  dataConfig;
    }

    public void saveConfig() {
        if (dataConfig == null || configFile == null) {
            return;
        }

        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save data to " + configFile, e);
        }
    }
    
    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), name);
        }
        if (!configFile.exists()) {
            plugin.saveResource(name, false);
        }
    }
}