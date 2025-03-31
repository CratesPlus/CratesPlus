package plus.crates.frameworks;

import org.bukkit.Material;

public class Itemname {
    public static String formatMaterialName(Material material) {
        String name = material.toString().toLowerCase().replace("_", " "); // "diamond_sword" → "diamond sword"
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1)) // Eerste letter hoofdletter maken
                    .append(" ");
        }

        return formattedName.toString().trim(); // Extra spatie verwijderen
    }
}
