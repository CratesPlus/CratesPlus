package plus.crates.frameworks;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import plus.crates.CratesPlus;


public class ChatName {

    private final CratesPlus plugin;
    private final DataManager cratesData;
    public ChatName(CratesPlus plugin) {

        this.plugin = plugin;
        cratesData = new DataManager(plugin, "crates");

    }

    public static String getChatName(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return "Unknown Item";
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item.getType().name().replace("_", " ");
        }

        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CratesPlus.getInstance(), "chatName");

        if (data.has(key, PersistentDataType.STRING)) {
            return data.get(key, PersistentDataType.STRING);
        }

        return item.getType().name().replace("_", " "); // Default fallback
    }

    public static ItemStack setChatName(ItemStack item, String chatName) {
        if (item == null || chatName == null) return item;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(CratesPlus.getInstance(), "chatName"), PersistentDataType.STRING, chatName);

        item.setItemMeta(meta);
        return item;
    }


}
