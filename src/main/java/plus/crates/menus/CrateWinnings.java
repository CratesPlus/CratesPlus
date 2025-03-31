package plus.crates.menus;

import org.bukkit.entity.Player;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import plus.crates.CratesPlus;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.Menu;
import plus.crates.frameworks.PlayerMenuUtility;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CrateWinnings extends Menu {
    private final DataManager data;
    private final DataManager lang;
    private final String crateName;
    private final CratesPlus plugin;

    public CrateWinnings(PlayerMenuUtility playerMenuUtility, CratesPlus plugin, String crateName) {
        super(playerMenuUtility);
        this.plugin = plugin;
        this.crateName = crateName;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");

    }

    @Override
    public String getMenuName() {
        return lang.getConfig().getString("EditCrate").replace("%cratename%",crateName);
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < getSlots(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                // Ensure the chatName is stored before saving
                items.add(item);
            }
        }

        data.getConfig().set(crateName + ".items", items);

        // Automatically distribute chances
        List<Double> chances = new ArrayList<>();
        double chance = 100.0 / items.size();
        for (int i = 0; i < items.size(); i++) {
            chances.add(chance);
        }
        data.getConfig().set(crateName + ".chances", chances);
        data.saveConfig();
        ChatColorHandler.sendMessage(player,lang.getConfig().getString("Prefix") + " " + lang.getConfig().getString("SuccessCrateWinnings"));
    }






    @Override
    public void setMenuItems() {
        // Load the crate items
        data.reloadConfig();
        List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(crateName + ".items");

        assert items != null;
        for (ItemStack itemStack : items) {
            inventory.addItem(itemStack);
        }
    }
}
