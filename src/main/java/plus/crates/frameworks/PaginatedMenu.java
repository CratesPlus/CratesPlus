package plus.crates.frameworks;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

/*

A class extending the functionality of the regular Menu, but making it Paginated

 */

public abstract class PaginatedMenu extends Menu {

    //Keep track of what page the menu is on
    protected int page = 0;
    //28 is max items because with the border set below,
    //28 empty slots are remaining.
    protected int maxItemsPerPage = 28;
    //the index represents the index of the slot
    //that the loop is on
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    //Set the border and menu buttons for the menu
    public void addMenuBorder(){
        inventory.setItem(48, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"));

        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close"));

        inventory.setItem(50, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"));

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}
