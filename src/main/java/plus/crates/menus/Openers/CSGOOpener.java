package plus.crates.menus.Openers;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import plus.crates.CratesPlus;
import plus.crates.frameworks.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CSGOOpener extends Menu {
    private final CratesPlus plugin;
    private final Player player;
    private final DataManager data;
    private final DataManager lang;
    private final String crateName;

    public CSGOOpener(PlayerMenuUtility playerMenuUtility, CratesPlus plugin, String crateName, Player player) {
        super(playerMenuUtility);
        this.crateName = crateName;
        this.plugin = plugin;
        this.player = player;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");
    }

    @Override
    public String getMenuName() {
        data.reloadConfig();
        ChatColor color = HexColor.ItemHEX(data.getConfig().getString(crateName + ".color"));
        return color + crateName + lang.getConfig().getString("Win");
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        // set lines


        data.reloadConfig();
        List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(crateName + ".items");
        List<Double> chances = (List<Double>) data.getConfig().getList(crateName + ".chances");
        assert chances != null;

        List<ItemStack> rowOfItems = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            rowOfItems.add(randomItemStack(items, chances, chances.size()));
        }

        // Set row of items


        for (int i = 0; i < 50; i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.setItem(13, rowOfItems.get(finalI));

                    Material[] glassColors = {
                            Material.RED_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE,
                            Material.GREEN_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
                            Material.PURPLE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE,
                            Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE
                    };

                    int colorIndex = 0;

                    for (int i = 0; i < 28; i++) {
                        if (i == 13) continue; // Skip slot 13

                        Material glassType = glassColors[colorIndex % finalI % glassColors.length];
                        ItemStack glassPane = new ItemStack(glassType);
                        inventory.setItem(i, glassPane);

                        colorIndex++; // Volgende kleur gebruiken
                    }


                }
            }.runTaskLater(plugin, 2 * i);
        }

        new BukkitRunnable() {
            @Override
            public void run() {


                player.getInventory().addItem(rowOfItems.get(52));
                ItemStack wonItem = rowOfItems.get(52);
                int itemIndex = rowOfItems.indexOf(wonItem);
                String itemNaam = Itemname.formatMaterialName(wonItem.getType());


                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getConfig().getString("Prefix") + " " + lang.getConfig().getString("Won").replace("%winner%", player.getName()).replace("%item%", itemNaam).replace("%cratename%",crateName)));
                }
            }
        }.runTaskLater(plugin, 100);
    }

    private int findCeil(double[] arr, double r, int l, int h) {
        if (arr == null || arr.length == 0 || r > arr[h]) {
            return h;
        }

        int mid;
        while (l < h) {
            mid = l + ((h - l) >> 1);
            if (r > arr[mid]) {
                l = mid + 1;
            } else {
                h = mid;
            }
        }

        return (l < arr.length && arr[l] >= r) ? l : -1;
    }


    private ItemStack randomItemStack(List<ItemStack> items, List<Double> chance, int n)
    {
        // Create and fill prefix array
        double[] prefix = new double[n];
        int i;
        prefix[0] = chance.get(0);
        for (i = 1; i < n; ++i)
            prefix[i] = prefix[i - 1] + chance.get(i);

        // prefix[n-1] is sum of all frequencies.
        // Generate a random number with
        // value from 1 to this sum
        double r = Math.random() * prefix[n - 1];
        // Find index of ceiling of r in prefix array
        int indexc = findCeil(prefix, r, 0, n - 1);
        if (indexc == -1) indexc = n - 1;
        return items.get(indexc);
    }
}

