package plus.crates.Handlers;

import org.lushplugins.chatcolorhandler.ChatColorHandler;
import plus.crates.CratesPlus;
import plus.crates.Handlers.Holograms.DecentHologramMaker;
import plus.crates.frameworks.DataManager;
import plus.crates.menus.Openers.CSGOOpener;
import plus.crates.menus.Openers.DefaultOpener;
import plus.crates.menus.CratePreview;
import org.bukkit.*;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import plus.crates.menus.Openers.NoGUI;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandler implements Listener {
    private final CratesPlus plugin;
    private final DataManager data;
    private final DataManager lang;
    public PlayerHandler(CratesPlus plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");

    }


    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if (!isCrate(itemStack)) return;

        // Get crate name
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        String crateName = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
        assert crateName != null;


        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        container.set(new NamespacedKey(plugin, "name"), PersistentDataType.STRING, crateName);
        state.update();
        Location crateLoc = event.getBlock().getLocation();
        String path = crateName + ".locations";

        List<String> locations = data.getConfig().getStringList(path);
        String locString = crateLoc.getWorld().getName() + "," + crateLoc.getBlockX() + "," + crateLoc.getBlockY() + "," + crateLoc.getBlockZ();

        if (!locations.contains(locString)) {
            locations.add(locString);
        }

        data.getConfig().set(path, locations);
        data.saveConfig();


        // Place holograms
        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        String line1 = color + crateName;
        String line2 = ChatColorHandler.translate(lang.getConfig().getString("RightClickCrate"));
        String line3 = ChatColorHandler.translate(lang.getConfig().getString("LeftClickCrate"));
        ArrayList<String> lines = new ArrayList<>();
        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        DecentHologramMaker.create(crateLoc, lines);

    }
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (event.getClickedBlock().getType() != Material.CHEST) return;
        if (event.getClickedBlock().getState() instanceof TileState state) {
            PersistentDataContainer container = state.getPersistentDataContainer();

            // If chest is a crate
            if (container.has(new NamespacedKey(plugin, "name"), PersistentDataType.STRING)) {
                String crateName = container.get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
                Player player = event.getPlayer();

                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (player.hasPermission("cratesplus.admin") && player.isSneaking()) {
                        Location crateLoc = event.getClickedBlock().getLocation();
                        if(player.getGameMode() == GameMode.SURVIVAL) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("NoSurvival")));
                            event.setCancelled(true);
                        } else {
                            DecentHologramMaker.remove(crateLoc);
                        }
                    } else {
                        event.setCancelled(true);
                        new CratePreview(CratesPlus.getPlayerMenuUtility(event.getPlayer()), plugin, crateName).open();
                    }
                } else {
                    event.setCancelled(true);
                    ItemStack itemStack = event.getItem();
                    data.reloadConfig();
                    String color = data.getConfig().getString(crateName + ".color");

                    if (itemStack != null) {
                        if (isCorrectKey(itemStack, crateName) && itemStack.getType() == Material.TRIPWIRE_HOOK) {
                            // Remove key from inv
                            itemStack.setAmount(itemStack.getAmount() - 1);
                            if ("CSGO".equals(data.getConfig().getString(crateName + ".opener"))) {
                                new CSGOOpener(CratesPlus.getPlayerMenuUtility(player), plugin, crateName, player).open();
                            } else if("NoGUI".equals(data.getConfig().getString(crateName + ".opener"))) {
                            NoGUI.NoGUIOpener(plugin,crateName,player);

                        }
                            else {
                                new DefaultOpener(CratesPlus.getPlayerMenuUtility(player), plugin, crateName, player).open();
                            }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("MustHolding").replace("%key%", crateName).replace("%keycolor%", color)));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("MustHolding").replace("%key%", crateName).replace("%keycolor%", color)));
                    }
                }
            }
        }
    }

    private boolean isCrate(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        return container.has(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
    }

    private boolean isCorrectKey(ItemStack itemStack, String crateName) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        // If not a key return
        if (!container.has(new NamespacedKey(plugin, "name"), PersistentDataType.STRING)) return false;

        String keyName = container.get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);

        return crateName.equals(keyName);
    }

}