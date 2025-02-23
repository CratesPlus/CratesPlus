package plus.crates.frameworks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class HexColor {
    public static ChatColor ItemHEX(String hexCode) {
        if(hexCode.startsWith("#")) {
            return ChatColor.of(hexCode);
        } else {
            String translated = ChatColor.translateAlternateColorCodes('&',hexCode);
            return ChatColor.getByChar(translated.charAt(1));
        }
    }

}
