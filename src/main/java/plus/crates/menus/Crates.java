package plus.crates.menus;

import plus.crates.CratesPlus;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.Menu;
import plus.crates.frameworks.PlayerMenuUtility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class Crates extends Menu {
    private final CratesPlus plugin;
    private final DataManager data;
    private final DataManager lang;
    public Crates(PlayerMenuUtility playerMenuUtility, CratesPlus plugin) {
        super(playerMenuUtility);

        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");

    }

    @Override
    public String getMenuName() {
        return "CratesPlus " + lang.getConfig().getString("Settings");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String crateName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        new CrateSettings(CratesPlus.getPlayerMenuUtility(player), plugin, crateName).open();
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        data.reloadConfig();
        data.getConfig().getKeys(false).forEach(crate -> {
            // Create crate item
            String color = data.getConfig().getString(crate + ".color");
            inventory.addItem(makeItem(Material.CHEST, color + crate));
        });
    }
}
