package mc.jabifx.heart_Manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class MyListener implements Listener {

    private final Heart_Manager plugin = Heart_Manager.getInstance();

    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if (result == null || result.getType() != Material.NETHER_STAR) return;

        ItemMeta meta = result.getItemMeta();
        if (meta == null || !meta.hasEnchant(Enchantment.MENDING)) return;

        if (!plugin.getConfig().getBoolean("craft revive", true)) {
            event.getInventory().setResult(null);

            HumanEntity crafter = event.getView().getPlayer();
            if (crafter instanceof Player) {
                crafter.sendMessage(ChatColor.WHITE + plugin.getConfig().getString("revive.disabled"));
                crafter.closeInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            ItemStack item = ItemUtils.createAdvancedItem(Material.SADDLE, "re-role", 1);
            HealthManager.adjustPlayerHealth(victim, -2);
            if (victim.getUniqueId().equals(killer.getUniqueId())) return;
            HealthManager.adjustPlayerHealth(killer, 2);
            ItemUtils.giveItem(killer, item);
        }
        else HealthManager.adjustPlayerHealth(victim, -2);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ReviveManager.removeFromReviveList(player.getUniqueId(), event.getPlayer());
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) return;
        if (!item.hasItemMeta() || !item.getItemMeta().hasEnchant(Enchantment.MENDING)) return;
        if (event.getHand() != EquipmentSlot.HAND) return; // MAIN_HAND

        // Solo procesar clicks derechos
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK:
                break;
            default:
                return;
        }

        Material type = item.getType();
        String prefix = ChatColor.RED + plugin.getConfig().getString("prefix") + ChatColor.WHITE;

        if (type == Material.SADDLE) {
            if (GambleManager.gamble(player)) {
                item.setAmount(item.getAmount() - 1);
            }
        }
        else if (type == Material.NETHER_STAR) {
            ReviveManager.openReviveGUI(player);
        }
        else if (type == Material.HEART_OF_THE_SEA) {
            double maxHearts = plugin.getConfig().getInt("uses");
            if ((player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 2) / 2 > maxHearts) {
                player.sendMessage(prefix + plugin.getConfig().getString("uses msg"));
                return;
            }

            if (HealthManager.adjustPlayerHealth(player, 2)) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                item.setAmount(item.getAmount() - 1);
                player.sendMessage(prefix + plugin.getConfig().getString("heart.added"));
            }
            else {
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 1.0f);
                player.sendMessage(prefix + plugin.getConfig().getString("heart.full-hearts"));
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.RED + "Dead players")) {
            e.setCancelled(true);

            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getType() != Material.PLAYER_HEAD) return;

            String clickedName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
            Player clicker = (Player) e.getWhoClicked();

            ReviveManager.revivePlayer(clickedName);

            e.getInventory().setItem(e.getSlot(), null);
            clicker.closeInventory();

            ItemStack itemInHand = clicker.getInventory().getItemInMainHand();
            if (itemInHand.getType() != Material.AIR) {
                int amount = itemInHand.getAmount();
                if (amount > 1) itemInHand.setAmount(amount - 1);
                else clicker.getInventory().setItemInMainHand(null);
            }
        }
    }
}
