package plus.crates.menus;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eclipse.aether.util.listener.ChainedTransferListener;
import plus.crates.CratesPlus;
import plus.crates.Handlers.Holograms.DecentHologramMaker;
import plus.crates.frameworks.CrateLocations;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.Menu;
import plus.crates.frameworks.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Material.*;

public class CrateColor extends Menu {
    private final DataManager data;
    private final DataManager lang;
    private final CratesPlus plugin;
    private final String crateName;
    private final CrateLocations crateLocations;
    private final DecentHologramMaker decentHologramMaker;


    public CrateColor(PlayerMenuUtility playerMenuUtility, CratesPlus plugin, String crateName) {
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
        return 18;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        switch (event.getCurrentItem().getType()) {

            case BLACK_WOOL:
                if(clickedItem.getType() == BLACK_WOOL) {
                    data.getConfig().set(crateName + ".color", "§0");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.BLACK + lang.getConfig().getString("Black"));
            }
            case BLUE_WOOL:
                if(clickedItem.getType() == BLUE_WOOL) {
                    data.getConfig().set(crateName + ".color", "§1");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.DARK_BLUE + lang.getConfig().getString("DarkBlue"));
                }
            case GREEN_CONCRETE:
                if(clickedItem.getType() == GREEN_CONCRETE) {
                    data.getConfig().set(crateName + ".color", "§2");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.GREEN + lang.getConfig().getString("DarkGreen"));
                }
                case CYAN_CONCRETE:

                    if(clickedItem.getType() == CYAN_CONCRETE) {

                        data.getConfig().set(crateName + ".color", "§3");
                        event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.DARK_AQUA + lang.getConfig().getString("DarkAqua"));
                    }
                case RED_CONCRETE:
                    if(clickedItem.getType() == RED_CONCRETE) {

                        data.getConfig().set(crateName + ".color", "§4");
                        event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.DARK_RED + lang.getConfig().getString("DarkRed"));
                    }
            case PURPLE_WOOL:
                if(clickedItem.getType() == PURPLE_WOOL) {
                    data.getConfig().set(crateName + ".color", "§5");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.DARK_PURPLE + lang.getConfig().getString("DarkPurple"));
                }
                case ORANGE_WOOL:
                    if(clickedItem.getType() == ORANGE_WOOL) {
                        data.getConfig().set(crateName + ".color", "§6");
                        event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.GOLD + lang.getConfig().getString("Gold"));
                    }
                case LIGHT_GRAY_WOOL:
                    if(clickedItem.getType() == LIGHT_GRAY_WOOL) {

                        data.getConfig().set(crateName + ".color", "§7");
                        event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.GRAY + lang.getConfig().getString("Gray"));
                    }
                case GRAY_WOOL:
                    if(clickedItem.getType() == GRAY_WOOL) {

                        data.getConfig().set(crateName + ".color", "§8");
                        event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.DARK_GRAY + lang.getConfig().getString("DarkGray"));
                    }
                case LIGHT_BLUE_WOOL:
                    if(clickedItem.getType() == LIGHT_BLUE_WOOL) {

                        data.getConfig().set(crateName + ".color", "§9");
                        event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.BLUE + lang.getConfig().getString("Blue"));
                    }
            case GREEN_WOOL:
                if(clickedItem.getType() == GREEN_WOOL) {

                    data.getConfig().set(crateName + ".color", "§a");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.GREEN + lang.getConfig().getString("Green"));
                }
            case CYAN_WOOL:
                if(clickedItem.getType() == CYAN_WOOL) {

                    data.getConfig().set(crateName + ".color", "§b");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.AQUA + lang.getConfig().getString("Aqua"));
                }
            case RED_WOOL:
                if(clickedItem.getType() == RED_WOOL) {

                    data.getConfig().set(crateName + ".color", "§c");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.RED + lang.getConfig().getString("Red"));
                }
            case PINK_WOOL:
                if(clickedItem.getType() == PINK_WOOL) {

                    data.getConfig().set(crateName + ".color", "§d");
                    event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.LIGHT_PURPLE + lang.getConfig().getString("LightPurple"));
                }
                    case YELLOW_WOOL:
                        if(clickedItem.getType() == YELLOW_WOOL) {

                            data.getConfig().set(crateName + ".color", "§e");
                            event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.YELLOW + lang.getConfig().getString("Yellow"));
                        }
                case WHITE_WOOL:
                    if(clickedItem.getType() == WHITE_WOOL) {

                        data.getConfig().set(crateName + ".color", "§f");
                        event.getWhoClicked().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.WHITE + lang.getConfig().getString("White"));
                    }
            case BARRIER:
                if(clickedItem.getType() == BARRIER) {
                    new AnvilGUI.Builder()
                            .onClick((clickedSlot, stateSnapshot) -> {
                                if (clickedSlot != AnvilGUI.Slot.OUTPUT) {
                                    return Collections.emptyList();
                                }

                                String text = stateSnapshot.getText();
                                    // Update the correct chance
                                    data.getConfig().set(crateName + ".color", text);
                                data.saveConfig();
                                data.reloadConfig();
                                // Reopen menu to refresh chances
                                    stateSnapshot.getPlayer().sendMessage(lang.getConfig().getString("ColorChange") + ChatColor.WHITE + " " + text);
                                    return AnvilGUI.Response.close();

                            })
                            .title("Type HEX code")
                            .text("HEX (without &)")
                            .plugin(plugin)
                            .itemLeft(new ItemStack(Material.PAPER))
                            .open((Player) event.getWhoClicked());

                }
        }
        data.saveConfig();
        data.reloadConfig();
        List<Location> crateLocation = crateLocations.getCrateLocations(crateName);
        if (crateLocation.isEmpty()) {
            return;
        }
        for (Location loc : crateLocation) {
            decentHologramMaker.reloadColor(loc, crateName);
        }
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        inventory.addItem(makeItem(BLACK_WOOL, "§0" + lang.getConfig().getString("Black")));
        inventory.addItem(makeItem(Material.BLUE_WOOL, "§1" + lang.getConfig().getString("DarkBlue")));
        inventory.addItem(makeItem(Material.GREEN_CONCRETE, "§2" + lang.getConfig().getString("DarkGreen")));
        inventory.addItem(makeItem(Material.CYAN_CONCRETE, "§3" + lang.getConfig().getString("DarkAqua")));
        inventory.addItem(makeItem(Material.RED_CONCRETE, "§4" + lang.getConfig().getString("DarkRed")));
        inventory.addItem(makeItem(Material.PURPLE_WOOL, "§5" + lang.getConfig().getString("DarkPurple")));
        inventory.addItem(makeItem(Material.ORANGE_WOOL, "§6" + lang.getConfig().getString("Gold")));
        inventory.addItem(makeItem(Material.LIGHT_GRAY_WOOL, "§7" + lang.getConfig().getString("Gray")));
        inventory.addItem(makeItem(Material.GRAY_WOOL, "§8" + lang.getConfig().getString("DarkGray")));
        inventory.setItem(9, makeItem(Material.LIGHT_BLUE_WOOL, "§9" + lang.getConfig().getString("Blue")));
        inventory.setItem(10, makeItem(Material.GREEN_WOOL, "§a" + lang.getConfig().getString("Green")));
        inventory.setItem(11, makeItem(Material.CYAN_WOOL, "§b" + lang.getConfig().getString("Aqua")));
        inventory.setItem(12, makeItem(Material.RED_WOOL, "§c" + lang.getConfig().getString("Red")));
        inventory.setItem(13, makeItem(Material.PINK_WOOL, "§d" + lang.getConfig().getString("LightPurple")));
        inventory.setItem(14, makeItem(Material.YELLOW_WOOL, "§e" + lang.getConfig().getString("Yellow")));
        inventory.setItem(15, makeItem(Material.WHITE_WOOL, "§f" + lang.getConfig().getString("White")));
        inventory.setItem(17, makeItem(BARRIER, "§f" + lang.getConfig().getString("Other")));

    }
}
