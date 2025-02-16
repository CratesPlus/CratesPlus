package plus.crates.Handlers.Holograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.World;
import plus.crates.CratesPlus;
import plus.crates.frameworks.DataManager;


import java.util.ArrayList;
import java.util.Objects;

public class DecentHologramMaker {
    private final DataManager data;
    private final CratesPlus plugin;

    public DecentHologramMaker(CratesPlus plugin) {
        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
    }

    public static void create(Location location, ArrayList<String> lines) {

        double y = location.getY() + 2;
        double x = location.getX() + 0.5;
        double z = location.getZ() + 0.5;
        World world = location.getWorld();
        Location location1 = new Location(world,x,y,z);
        DHAPI.createHologram("x_"+location.getBlockX()+"_y_"+location.getBlockY()+"_z_"+location.getBlockZ(), location1, true, lines);


    }
    public static void remove(Location location) {
        DHAPI.removeHologram("x_"+location.getBlockX()+"_y_"+location.getBlockY()+"_z_"+location.getBlockZ());

    }
    public void reloadColor(Location location, String crateName) {
        String color = Objects.requireNonNull(data.getConfig().get(crateName + ".color")).toString();;
        Hologram hologram = DHAPI.getHologram("x_"+location.getBlockX()+"_y_"+location.getBlockY()+"_z_"+location.getBlockZ());
        if(hologram == null) return;
        else DHAPI.setHologramLine(hologram, 0, color + crateName);
    }
}
