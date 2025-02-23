package plus.crates.commands;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import plus.crates.CratesPlus;
import plus.crates.frameworks.DataManager;
import plus.crates.frameworks.StringConverter;
import plus.crates.menus.Crates;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Crate implements CommandExecutor, TabCompleter {
    private final DataManager data;
    private final DataManager lang;
    private final CratesPlus plugin;

    public Crate(CratesPlus plugin) {
        plugin.getCommand("crate").setExecutor(this);
        plugin.getCommand("crate").setTabCompleter(this);

        this.plugin = plugin;
        data = new DataManager(plugin, "crates");
        lang = new DataManager(plugin, "lang");

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender.hasPermission("cratesplus.admin")) {
                 if (args.length > 0) {
                     if (args[0].equalsIgnoreCase("create")) {
                         if (args.length > 1) {
                             String crateName = args[1];

                             data.reloadConfig();
                             if (data.getConfig().get(crateName + ".color") == null) {
                                 List<ItemStack> items = new ArrayList<>();
                                 items.add(new ItemStack(Material.IRON_SWORD));

                                 List<Double> chances = new ArrayList<>();
                                 chances.add(100.0);

                                 data.getConfig().set(crateName + ".items", items);
                                 data.getConfig().set(crateName + ".color", "§c");
                                 data.getConfig().set(crateName + ".chances", chances);
                                 data.saveConfig();
                                 data.reloadConfig();

                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("SuccessAdded").replace("%cratename%", crateName)));
                             } else {
                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("AlreadyExists")));
                             }
                         } else {
                             sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("ArgumentRequired") + ChatColor.RESET + " /crate create <name>"));
                         }
                     } else if (args[0].equalsIgnoreCase("settings")) {
                         new Crates(CratesPlus.getPlayerMenuUtility((Player) sender), plugin).open();
                     }  else if (args[0].equalsIgnoreCase("rename")) {
                         if (args.length > 2) {
                             String oldName = args[1];
                             String newName = args[2];

                             data.reloadConfig();
                             if (data.getConfig().get(oldName + ".color") == null) {
                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoName")));
                             } else {
                                 // Get all the data
                                 data.reloadConfig();
                                 List<ItemStack> items = (List<ItemStack>) data.getConfig().getList(oldName + ".items");
                                 List<Double> chances = (List<Double>) data.getConfig().getList(oldName + ".chances");
                                 String color = data.getConfig().getString(oldName + ".color");

                                 // Delete old crate
                                 data.getConfig().set(oldName, null);

                                 // Make new crate
                                 data.getConfig().set(newName + ".items", items);
                                 data.getConfig().set(newName + ".color", color);
                                 data.getConfig().set(newName + ".chances", chances);
                                 data.saveConfig();

                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("RenameCrateSuccess").replace("%oldname%", oldName).replace("%newname%", newName)));
                             }
                         } else {
                             sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("ArgumentRequired") + ChatColor.RESET + " /crate rename <old> <new>"));
                         }
                     } else if (args[0].equalsIgnoreCase("delete")) {
                         if (args.length > 1) {
                             String crateName = args[1];

                             data.reloadConfig();
                             if (data.getConfig().get(crateName + ".color") != null) {
                                 String color = data.getConfig().getString(crateName + ".color");

                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("SuccessDeleteCrate").replace("%cratename%", crateName).replace("%cratecolor%", color)));
                                 data.getConfig().set(crateName, null);
                                 data.saveConfig();
                                 data.reloadConfig();
                             } else {
                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoCrate")));
                             }
                         } else {
                             sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("ArgumentRequired") + ChatColor.RESET + " /crate Delete <name>"));
                         }
                     } else if (args[0].equalsIgnoreCase("crate")) {
                         if (args.length > 2) {
                             String crateName = args[1];
                             Player target = Bukkit.getPlayer(args[2]);

                             data.reloadConfig();
                             if (data.getConfig().get(crateName + ".color") != null) {
                                 if (target != null) {
                                     ItemStack crate = CreateCrate(crateName);
                                     String color = data.getConfig().getString(crateName + ".color");

                                     target.getInventory().addItem(crate);

                                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("CrateGiven").replace("%cratename%", crateName)));
                                     target.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerCrateGiven").replace("%cratecolor%", color).replace("%cratename%", crateName)));
                                 } else {
                                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("InvalidUser")));
                                 }
                             } else {
                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoCrate")));
                             }
                         } else if (args.length > 1) {
                             String crateName = args[1];

                             data.reloadConfig();
                             if (data.getConfig().get(crateName + ".color") != null) {
                                 ItemStack crate = CreateCrate(crateName);
                                 String color = data.getConfig().getString(crateName + ".color");
                                 if (sender instanceof Player player) {
                                 player.getInventory().addItem(crate);
                                 player.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerCrateGiven").replace("%cratecolor%", color).replace("%cratename%", crateName)));
                             }
                             } else {
                                 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoCrate")));
                             }
                         } else {
                             sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("ArgumentRequired") + ChatColor.RESET + " /crate crate <type> [player]"));
                         }
                     } else if (args[0].equalsIgnoreCase("keyall")) {


                                 if (args.length == 3) {
                                     String crateName = args[1];

                                     if (data.getConfig().get(crateName + ".color") != null) {


                                         ItemStack key = CreateKey(crateName);

                                         int amount = Integer.parseInt(args[2]);
                                         for (Player keyplayer : Bukkit.getOnlinePlayers()) {
                                             for (int i = 0; i < amount; i++) {
                                                 keyplayer.getInventory().addItem(key);
                                             }
                                             keyplayer.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerKeyGiven").replace("%key%", crateName)));


                                             sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("KeyGiven").replace("%target%", keyplayer.getDisplayName()).replace("%key%", crateName)));

                                         }
                                     }

                                 } else if (args.length == 2) {                                     String crateName = args[1];

                                     if (data.getConfig().get(crateName + ".color") != null) {


                                         ItemStack key = CreateKey(crateName);

                                     int amount = 1;
                                     for (Player keyplayer : Bukkit.getOnlinePlayers()) {
                                         for (int i = 0; i < amount; i++) {
                                             keyplayer.getInventory().addItem(key);
                                         }
                                         keyplayer.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerKeyGiven").replace("%key%", crateName)));


                                         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("KeyGiven").replace("%target%", keyplayer.getDisplayName()).replace("%key%", crateName)));

                                     }
                                 } else {
                                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoKey")));
                                 }
                                 } else {
                                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("ArgumentRequired") + ChatColor.RESET + " /crate keyall <type> [amount]"));

                                 }


                         } else if (args[0].equalsIgnoreCase("key")) {
                        if (args.length > 3) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                if(StringConverter.isStringInt(args[2])) {
                                    int amount = Integer.parseInt(args[2]);

                                    if (sender instanceof Player player) {
                                        ItemStack key = CreateKey(crateName);
                                        String color = data.getConfig().getString(crateName + ".color");


                                        for (int i = 0; i < amount; i++) {
                                            player.getInventory().addItem(key);
                                        }
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("KeyGiven").replace("%target%", player.getDisplayName()).replace("%key%", crateName)));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerKeyGiven").replace("%key%", crateName)));

                                    } else {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("InvalidUser")));
                                    }
                                } else {
                                    Player target = Bukkit.getPlayer(args[2]);
                                    int amount = Integer.parseInt(args[3]);

                                    if (target != null) {
                                        ItemStack key = CreateKey(crateName);
                                        String color = data.getConfig().getString(crateName + ".color");


                                        for (int i = 0; i < amount; i++) {
                                            target.getInventory().addItem(key);
                                        }
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("KeyGiven").replace("%target%", target.getDisplayName()).replace("%key%", crateName)));
                                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerKeyGiven").replace("%key%", crateName)));

                                    } else {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("InvalidUser")));
                                    }
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoCrate")));
                            }
                        } else if (args.length > 2) {
                            String crateName = args[1];
                            if(StringConverter.isStringInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);

                                if (sender instanceof Player player) {
                                    ItemStack key = CreateKey(crateName);
                                    String color = data.getConfig().getString(crateName + ".color");


                                    for (int i = 0; i < amount; i++) {
                                        player.getInventory().addItem(key);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("KeyGiven").replace("%target%", player.getDisplayName()).replace("%key%", crateName)));
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerKeyGiven").replace("%key%", crateName)));

                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("InvalidUser")));
                                }
                            } else {

                                Player target = Bukkit.getPlayer(args[2]);

                            int amount = 1;
                            String color = data.getConfig().getString(crateName + ".color");
                            ItemStack key = CreateKey(crateName);


                            data.reloadConfig();
                                if (data.getConfig().get(crateName + ".color") != null) {


                                    for (int i = 0; i < amount; i++) {
                                        target.getInventory().addItem(key);
                                    }
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("KeyGiven").replace("%key%", crateName)));

                                    } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoKey")));
}
                            }
                        } else if (args.length > 1) {
                            String crateName = args[1];

                            data.reloadConfig();
                            if (data.getConfig().get(crateName + ".color") != null) {
                                ItemStack key = CreateKey(crateName);
                                String color = data.getConfig().getString(crateName + ".color");
                                if(sender instanceof Player player ) {
                                player.getInventory().addItem(key);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', CratesPlus.chatPrefix + lang.getConfig().getString("PlayerKeyGiven").replace("%key%", crateName)));
                                }
                                } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoCrate")));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("ArgumentRequired") + ChatColor.RESET + " /crate key <type> [player] [amount]"));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("InvalidArgument")));
                    }
                } else {
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " ----- CratePlus " + plugin.getDescription().getVersion() + " Help -----");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate settings " + ChatColor.YELLOW + "Edit settings of CratesPlus and crate winnings");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate create <name> " + ChatColor.YELLOW + "Create a new crate");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate rename <old name> <new name> " + ChatColor.YELLOW + "Rename a crate");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate delete <name> " + ChatColor.YELLOW + "Delete a crate");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate give <player/all> [crate] [amount] " + ChatColor.YELLOW + "Give player a crate/key, if no crate given it will be random");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate crate <type> [player] " + ChatColor.YELLOW + "Give player a crate to be placed, for use by admins");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate key <type> [player] [amount] " + ChatColor.YELLOW + "Give player a key");
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + " /crate keyall <type> [amount] " + ChatColor.YELLOW + "Give everyone a key");

                     //			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getConfig().getString("Prefix")) + ChatColor.AQUA + "/crate claim " + ChatColor.YELLOW + "Claim any keys that are waiting for you");
                 }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',CratesPlus.chatPrefix + ChatColor.RED + lang.getConfig().getString("NoArgument")));
            }
    return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> options = new ArrayList<>();
        // Admin version
        Player player = (Player) sender;
        if (args.length == 1 && player.hasPermission("cratesplus.admin")) {
            options.add("create");
            options.add("crate");
            options.add("key");
            options.add("keyall");
            options.add("settings");
            options.add("rename");
            options.add("delete");
        }
        return options;
    }

    private ItemStack CreateKey(String crateName) {
        ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        itemMeta.setDisplayName(color + crateName + " " + lang.getConfig().getString("CrateKey"));

        // Inject name
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, "name"), PersistentDataType.STRING, crateName);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


    private ItemStack CreateCrate(String crateName) {
        ItemStack itemStack = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        data.reloadConfig();
        String color = data.getConfig().getString(crateName + ".color");
        itemMeta.setDisplayName(color + crateName);

        // Inject name
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, "name"), PersistentDataType.STRING, crateName);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
