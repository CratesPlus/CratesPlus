package plus.crates.frameworks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import plus.crates.CratesPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CrateLocations {
    private final DataManager data;
    private final CratesPlus plugin;

    public CrateLocations(CratesPlus plugin) {
        this.plugin = plugin;
        this.data = new DataManager(plugin, "crates");
    }

    public List<Location> getCrateLocations(String crateName) {
        String path = crateName + ".locations";
        List<String> locationStrings = data.getConfig().getStringList(path);
        List<Location> locations = new ArrayList<>();

        for (String locString : locationStrings) {
            String[] parts = locString.split(",");
            if (parts.length != 4) {
                plugin.getLogger().warning("Unvalid Location: " + locString);
                continue;
            }

            World world = Bukkit.getWorld(parts[0]);
            if (world == null) {
                plugin.getLogger().warning("World Not Found " + parts[0]);
                continue;
            }

            try {
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                locations.add(new Location(world, x, y, z));
            } catch (NumberFormatException e) {
                plugin.getLogger().log(Level.WARNING, "Can't convert coords to numbers: " + locString, e);
            }
        }
        return locations;
    }
}
