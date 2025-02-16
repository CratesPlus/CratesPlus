package plus.crates.menus;

import org.bukkit.entity.Player;
import plus.crates.CratesPlus;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.Menu;
import plus.crates.frameworks.PlayerMenuUtility;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrateChances extends Menu {
    private final CratesPlus plugin;
    private final DataManager data;
    private final DataManager lang;

    private final String crateName;
    public CrateChances(PlayerMenuUtility playerMenuUtility, CratesPlus plugin, String crateName) {
        super(playerMenuUtility);

        this.plugin = plugin;
        this.crateName = crateName;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");

    }

    @Override
    public String getMenuName() {
        return "Edit " + crateName + " Crate Chances";
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
    public void handleMenu(InventoryClickEvent event) {
        data.reloadConfig();
        List<Double> chances = (List<Double>) data.getConfig().getList(crateName + ".chances");
        List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(crateName + ".items");

        if (chances == null || items == null) return; // Prevent NullPointerException

        int slot = event.getSlot();

        // Ensure slot corresponds to an actual crate item
        if (slot < 0 || slot >= items.size()) {
            return;
        }

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = clickedItem.getItemMeta().getDisplayName();
        Double chance = chances.get(slot);

        new AnvilGUI.Builder()
                .onClick((clickedSlot, stateSnapshot) -> {
                    if (clickedSlot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    try {
                        double percentage = Double.parseDouble(text);

                        // Update the correct chance
                        chances.set(slot, percentage);
                        data.getConfig().set(crateName + ".chances", chances);
                        data.saveConfig();

                        // Reopen menu to refresh chances
                        new CrateChances(CratesPlus.getPlayerMenuUtility(stateSnapshot.getPlayer()), plugin, crateName).open();
                        return AnvilGUI.Response.close();
                    } catch (NumberFormatException e) {
                        return AnvilGUI.Response.text(lang.getConfig().getString("InvalidNumber"));
                    }
                })
                .title(itemName + " Chance")
                .text(chance.toString())
                .plugin(plugin)
                .itemLeft(new ItemStack(Material.PAPER))
                .open((Player) event.getWhoClicked());
    }


    @Override
    public void closeMenu(InventoryCloseEvent event) {
    }

    @Override
    public void setMenuItems() {
        data.reloadConfig();
        List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(crateName + ".items");
        List<Double> chances = (List<Double>) data.getConfig().getList(crateName + ".chances");

        if (items == null || chances == null) return;

        // Ensure the chances list matches the items list size
        while (chances.size() < items.size()) {
            chances.add(0.0); // Default 0% chance for missing items
        }

        data.getConfig().set(crateName + ".chances", chances);
        data.saveConfig();

        for (int i = 0; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) continue;

            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add(lang.getConfig().getString("Chances") + chances.get(i) + "%");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            inventory.addItem(itemStack);
        }
    }

}
