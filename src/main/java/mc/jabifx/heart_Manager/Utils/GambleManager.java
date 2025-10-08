package mc.jabifx.heart_Manager.Utils;

import mc.jabifx.heart_Manager.Heart_Manager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class GambleManager {

    public static boolean gamble(Player player) {
        Heart_Manager plugin = Heart_Manager.getInstance();
        double playerMax = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double limit = plugin.getConfig().getInt("max hearts") * 2;

        if (playerMax + 2 > limit) {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix") +
                    ChatColor.WHITE + plugin.getConfig().getString("re-role.full-hearts"));
            return false;
        }

        if (Math.random() < 0.5) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            HealthManager.adjustPlayerHealth(player, 2);
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix") +
                    ChatColor.WHITE + plugin.getConfig().getString("re-role.win"));
        }
        else {
            player.playSound(player.getLocation(), Sound.ENTITY_GHAST_HURT, 1, 1);
            HealthManager.adjustPlayerHealth(player, -2);
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix") +
                    plugin.getConfig().getString("re-role.lose"));
        }
        return true;
    }
}
