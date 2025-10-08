package mc.jabifx.heart_Manager;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtils {

    public static ItemStack createAdvancedItem(Material material, String item, int amount) {
        Heart_Manager plugin = Heart_Manager.getInstance();
        ItemStack customItem = new ItemStack(material, amount);
        ItemMeta meta = customItem.getItemMeta();

        if (meta == null) return customItem;

        String displayName = ChatColor.RED + plugin.getConfig().getString(item + ".name");
        String loreText = plugin.getConfig().getString(item + ".description");

        meta.setDisplayName(displayName);
        meta.setLore(List.of(ChatColor.GRAY + loreText));
        meta.addEnchant(Enchantment.MENDING, 1000, true);
        customItem.setItemMeta(meta);

        return customItem;
    }

    public static void giveItem(Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() != -1) player.getInventory().addItem(item);
        else player.getWorld().dropItemNaturally(player.getLocation(), item);
    }
}
