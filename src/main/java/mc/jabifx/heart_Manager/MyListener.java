package mc.jabifx.heart_Manager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


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
            plugin.adjustPlayerHealth(victim, -2);
            plugin.adjustPlayerHealth(killer, 2);
            plugin.giveItem(killer);
        }
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
    }
}
