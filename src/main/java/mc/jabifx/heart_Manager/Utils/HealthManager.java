package mc.jabifx.heart_Manager.Utils;

import mc.jabifx.heart_Manager.Heart_Manager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HealthManager {

    public static boolean adjustPlayerHealth(Player player, double delta) {
        Heart_Manager plugin = Heart_Manager.getInstance();
        double maxHearts = plugin.getConfig().getInt("max hearts") * 2;
        double currentHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double newHealth = currentHealth + delta;

        if (newHealth <= maxHearts && newHealth > 0) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHealth);
            if (delta == 2) player.setHealth(newHealth);
            return true;
        }
        else if (newHealth > maxHearts) {
            ItemStack heartItem = ItemUtils.createAdvancedItem(Material.HEART_OF_THE_SEA, "heart", 1);
            ItemUtils.giveItem(player, heartItem);
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("heart.full"));
            return true;
        }
        else if (newHealth <= 0) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            banList.addBan(player.getName(), plugin.getConfig().getString("ban"), (Date) null, "Servidor");
            double reset = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 4;
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(reset);
            player.setHealth(reset);
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.kickPlayer(plugin.getConfig().getString("ban")), 20L);

            List<String> dead = plugin.getConfig().getStringList("dead");
            String uuidStr = player.getUniqueId().toString();
            if (!dead.contains(uuidStr)) {
                dead.add(uuidStr);
                plugin.getConfig().set("dead", dead);
                plugin.saveConfig();
            }
            return true;
        }
        return false;
    }
}
