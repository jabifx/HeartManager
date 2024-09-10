package mc.jabifx.heart_Manager;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.List;


public final class Heart_Manager extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new MyListener(this), this);
        this.getCommand("hearts").setExecutor(new Commands(this));
        this.getCommand("hearts").setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public ItemStack createAdvancedItem() {
        ItemStack customItem = new ItemStack(Material.SADDLE, 1);
        ItemMeta meta = customItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + getConfig().getString("re-role.name"));
            meta.setLore(List.of(ChatColor.GRAY + getConfig().getString("re-role.description")));
            meta.addEnchant(Enchantment.MENDING, 1000, true);
            customItem.setItemMeta(meta);
        }
        return customItem;
    }

    public void giveItem(Player player){
        ItemStack item = this.createAdvancedItem();
        if (player.getInventory().firstEmpty() != -1) player.getInventory().addItem(item);
        else player.getWorld().dropItemNaturally(player.getLocation(), item);
    }

    public void adjustPlayerHealth(Player player, double delta) {
        double newHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + delta;
        if (newHealth <= getConfig().getInt("max_hearts") * 2 && newHealth > 0) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newHealth);
            if(delta == 2) player.setHealth(newHealth);
        }
        else if (newHealth <= 0) {
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            banList.addBan(player.getName(), getConfig().getString("ban"), (Date) null, "Servidor");
            player.kickPlayer(getConfig().getString("ban"));
        }
    }

    public boolean gamble(Player player){
        double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if(playerMaxHealth + 2 > getConfig().getInt("max_hearts") * 2){
            player.sendMessage(ChatColor.RED + "[HEARTS] "+ ChatColor.WHITE + getConfig().getString("re-role.full-hearts"));
            return false;
        }
        else {
            if (Math.random() < 0.5){
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                this.adjustPlayerHealth(player, 2);
                player.sendMessage(ChatColor.RED + "[HEARTS] "+ ChatColor.WHITE + getConfig().getString("re-role.win"));
            }
            else{
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 1.0f);
                this.adjustPlayerHealth(player, -2);
                player.sendMessage(ChatColor.RED + "[HEARTS] " + getConfig().getString("re-role.lose"));
            }
            return true;
        }
    }
}
