package plus.crates.frameworks;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Stack;

/*
Companion class to all menus. This is needed to pass information across the entire
 menu system no matter how many inventories are opened or closed.

 Each player has one of these objects, and only one.
 */

public class PlayerMenuUtility {

    private final Player owner;

    //store the player that will be killed, so we can access him in the next menu


    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }
}