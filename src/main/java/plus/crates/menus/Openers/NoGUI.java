package plus.crates.menus.Openers;

import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import plus.crates.CratesPlus;
import plus.crates.frameworks.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NoGUI {
    private final CratesPlus plugin;
    private final DataManager data;
    private final DataManager lang;
    public NoGUI(CratesPlus plugin) {
        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");

    }
    public static DataManager NoGUIData(NoGUI instance) {
        if (instance.data != null) {
            // Gebruik instance.data
            return instance.data;
        }
        return null;
    }
    public static DataManager NoGUILang(NoGUI instance) {
        if (instance.lang != null) {
            // Gebruik instance.data
            return instance.lang;
        }
        return null;
    }



        public static void NoGUIOpener(CratesPlus plugin, String crateName, Player player) {
        NoGUI nogui = new NoGUI(plugin);
        nogui.NoGUIData(nogui).reloadConfig();
        List<ItemStack> items = (List<ItemStack>) nogui.NoGUIData(nogui).getConfig().getList(crateName + ".items");
        List<Double> chances = (List<Double>) nogui.NoGUIData(nogui).getConfig().getList(crateName + ".chances");
        assert chances != null;

        List<ItemStack> rowOfItems = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            rowOfItems.add(randomItemStack(items, chances, chances.size()));
        }

        // Set row of items




        player.getInventory().addItem(rowOfItems.get(52));
                ItemStack wonItem = rowOfItems.get(52);
                int itemIndex = rowOfItems.indexOf(wonItem);
            String itemNaam = Itemname.formatMaterialName(wonItem.getType());


                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player2.sendMessage(ChatColor.translateAlternateColorCodes('&', NoGUI.NoGUILang(nogui).getConfig().getString("Prefix") + " " + NoGUI.NoGUILang(nogui).getConfig().getString("Won").replace("%winner%", player.getName()).replace("%item%", itemNaam).replace("%cratename%",crateName)));

            }

        }

    private static int findCeil(double[] arr, double r, int l, int h) {
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


    private static ItemStack randomItemStack(List<ItemStack> items, List<Double> chance, int n)
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
