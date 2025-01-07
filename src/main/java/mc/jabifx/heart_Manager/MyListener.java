package mc.jabifx.heart_Manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyListener implements Listener {

    private Heart_Manager plugin;

    public MyListener (Heart_Manager plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            ItemStack item = plugin.createAdvancedItem(Material.SADDLE, "re-role");
            plugin.adjustPlayerHealth(victim, -2);
            plugin.adjustPlayerHealth(killer, 2);
            plugin.giveItem(killer, item);
        }
        else plugin.adjustPlayerHealth(victim, -2);
    }

    @EventHandler
    public void onPlayerUseMagicStick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.SADDLE && item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.MENDING)) {
            switch (event.getAction()) {
                case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK:
                    if(this.plugin.gamble(player)) player.getInventory().removeItem(item);
                    break;
                default:
                    break;
            }
        }
        else if (item.getType() == Material.HEART_OF_THE_SEA && item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.MENDING)) {
            switch (event.getAction()) {
                case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK:
                    if(plugin.adjustPlayerHealth(player, 2)){
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        item.setAmount(item.getAmount() - 1);
                    }
                    else{
                        player.playSound(player.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 1.0f);
                        player.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix")+ ChatColor.WHITE + plugin.getConfig().getString("heart.full-hearts"));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        String playerName = player.getName();

        ItemStack result = event.getRecipe().getResult();
        ItemStack customItem = plugin.createAdvancedItem(Material.HEART_OF_THE_SEA, "heart");

        if (!plugin.isSameItem(result, customItem)) return;

        List<Map<?, ?>> players = plugin.getConfig().getMapList("crafting");
        int craftCount = 0;
        boolean playerFound = false;

        for (Map<?, ?> playerData : players) {
            if (playerData.containsKey(playerName)) {
                craftCount = (int) playerData.get(playerName);
                ((Map<String, Integer>) playerData).put(playerName, craftCount + 1);
                playerFound = true;
                break;
            }
        }

        if (!playerFound) {
            craftCount = 1;
            Map<String, Integer> newPlayerData = new HashMap<>();
            newPlayerData.put(playerName, craftCount);
            players.add(newPlayerData);
        }

        if (craftCount >= plugin.getConfig().getInt("max_crafts")) {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix")+ ChatColor.WHITE + plugin.getConfig().getString("craft"));
            event.setCancelled(true);
        }
        else{
            plugin.getConfig().set("crafting", players);
            plugin.saveConfig();
        }
    }

}
