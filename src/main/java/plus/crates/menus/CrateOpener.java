package plus.crates.menus;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import plus.crates.CratesPlus;
import plus.crates.Handlers.Holograms.DecentHologramMaker;
import plus.crates.frameworks.CrateLocations;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.Menu;
import plus.crates.frameworks.PlayerMenuUtility;

import java.util.Collections;
import java.util.List;

import static org.bukkit.Material.*;

public class CrateOpener extends Menu {
    private final DataManager data;
    private final DataManager lang;
    private final CratesPlus plugin;
    private final String crateName;
    private final CrateLocations crateLocations;
    private final DecentHologramMaker decentHologramMaker;


    public CrateOpener(PlayerMenuUtility playerMenuUtility, CratesPlus plugin, String crateName) {
        super(playerMenuUtility);
        this.plugin = plugin;
        this.crateName = crateName;
        lang = new DataManager(plugin, "lang");
        data = new DataManager(plugin, "crates");
        crateLocations = new CrateLocations(plugin);
        decentHologramMaker = new DecentHologramMaker(plugin);

    }

    @Override
    public String getMenuName() {
        return lang.getConfig().getString("EditCrateColor");
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        switch (event.getCurrentItem().getType()) {

            case DIAMOND_SWORD:
                if(clickedItem.getType() == DIAMOND_SWORD) {
                    data.getConfig().set(crateName + ".opener", "CSGO");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("OpenerChange") + "CSGO");
                    data.saveConfig();
                    data.reloadConfig();
                }
            case COMPASS:
                if(clickedItem.getType() == COMPASS) {
                    data.getConfig().set(crateName + ".opener", "default");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("OpenerChange") + "default");
                    data.saveConfig();
                    data.reloadConfig();
                }
            case BARRIER:
                if(clickedItem.getType() == BARRIER) {
                    data.getConfig().set(crateName + ".opener", "NoGUI");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("OpenerChange") + "NoGUI");
                    data.saveConfig();
                    data.reloadConfig();
                }
        }

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        inventory.addItem(makeItem(DIAMOND_SWORD, "§fCSGO"));
        inventory.addItem(makeItem(COMPASS, "§fDefault"));
        inventory.addItem(makeItem(BARRIER, "§fNoGUI"));
    }
}
