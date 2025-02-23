package plus.crates.menus;

import org.lushplugins.chatcolorhandler.ChatColorHandler;
import plus.crates.CratesPlus;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.Menu;
import plus.crates.frameworks.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CrateSettings extends Menu {
    private final CratesPlus plugin;
    private final String crateName;
    private final DataManager data;
    private final DataManager lang;

    public CrateSettings(PlayerMenuUtility playerMenuUtility, CratesPlus plugin, String crateName) {
        super(playerMenuUtility);

        this.plugin = plugin;
        this.crateName = crateName;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");
    }

    @Override
    public String getMenuName() {
        return lang.getConfig().getString("EditCrateSettings").replace("%cratename%", crateName);
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
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()) {
            case DIAMOND -> new CrateWinnings(CratesPlus.getPlayerMenuUtility(player), plugin, crateName).open();
            case CYAN_WOOL -> new CrateColor(CratesPlus.getPlayerMenuUtility(player), plugin, crateName).open();
            case GOLD_INGOT -> new CrateChances(CratesPlus.getPlayerMenuUtility(player), plugin, crateName).open();
            case IRON_DOOR -> new CrateOpener(CratesPlus.getPlayerMenuUtility(player), plugin, crateName).open();
            case BARRIER -> new Crates(CratesPlus.getPlayerMenuUtility(player), plugin).open();

        }
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        // make items
        inventory.setItem(0, makeItem(Material.DIAMOND, lang.getConfig().getString("EditCrateWinnings")));
        inventory.setItem(2, makeItem(Material.CYAN_WOOL, lang.getConfig().getString("EditCrateColor")));
        inventory.setItem(4, makeItem(Material.GOLD_INGOT, lang.getConfig().getString("EditItemPercentages")));
        inventory.setItem(6, makeItem(Material.IRON_DOOR, lang.getConfig().getString("EditOpeners")));
        inventory.setItem(8, makeItem(Material.BARRIER, lang.getConfig().getString("GoBack")));

    }
}
