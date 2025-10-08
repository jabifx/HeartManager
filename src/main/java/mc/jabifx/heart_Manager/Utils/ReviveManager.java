package mc.jabifx.heart_Manager.Utils;

import mc.jabifx.heart_Manager.Heart_Manager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ReviveManager {

    public static void openReviveGUI(Player player) {
        Heart_Manager plugin = Heart_Manager.getInstance();
        List<String> deadUUIDs = plugin.getConfig().getStringList("dead");

        if (deadUUIDs.isEmpty()) {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("revive.no banned"));
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.RED + "Dead players");
        int slot = 0;

        for (String uuidString : deadUUIDs) {
            if (slot >= gui.getSize()) break;
            OfflinePlayer offline = Bukkit.getOfflinePlayer(UUID.fromString(uuidString));
            if (offline.getName() == null) continue;

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(offline);
                meta.setDisplayName(ChatColor.RED + offline.getName());
                skull.setItemMeta(meta);
            }

            gui.setItem(slot++, skull);
        }

        player.openInventory(gui);
    }

    public static void revivePlayer(String name) {
        Heart_Manager plugin = Heart_Manager.getInstance();
        OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
        UUID uuid = offline.getUniqueId();

        Bukkit.broadcastMessage(ChatColor.RED + name + " " + plugin.getConfig().getString("revive.msg"));
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1));

        addToReviveList(uuid);

        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        if (offline.getName() != null && banList.isBanned(offline.getName())) {
            banList.pardon(offline.getName());
        }

        List<String> deadList = plugin.getConfig().getStringList("dead");
        if (deadList.remove(uuid.toString())) {
            plugin.getConfig().set("dead", deadList);
            plugin.saveConfig();
        }
    }

    public static void addToReviveList(UUID uuid) {
        Heart_Manager plugin = Heart_Manager.getInstance();
        List<String> list = plugin.getConfig().getStringList("to-revive");
        String id = uuid.toString();
        if (!list.contains(id)) {
            list.add(id);
            plugin.getConfig().set("to-revive", list);
            plugin.saveConfig();
        }
    }

    public static void removeFromReviveList(UUID uuid, Player player) {
        Heart_Manager plugin = Heart_Manager.getInstance();
        List<String> list = plugin.getConfig().getStringList("to-revive");
        String id = uuid.toString();

        if (list.remove(id)) {
            plugin.getConfig().set("to-revive", list.isEmpty() ? null : list);
            plugin.saveConfig();

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
        }
    }
}
