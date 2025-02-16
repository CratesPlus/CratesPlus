package plus.crates.Handlers;

import plus.crates.CratesPlus;
import plus.crates.frameworks.Menu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuHandler implements Listener {
    public MenuHandler(CratesPlus plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onMenuClick(InventoryClickEvent event){

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            if (!menu.isChangeable()) {
                event.setCancelled(true);
            }
            if (event.getCurrentItem() == null) { //deal with null exceptions
                return;
            }
            //Since we know our inventoryholder is a menu, get the Menu Object representing
            // the menu we clicked on
            //Call the handleMenu object which takes the event and processes it
            menu.handleMenu(event);
        }

    }

    @EventHandler
    private void onMenuClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            menu.closeMenu(event);
        }
    }
}
