package mc.jabifx.heart_Manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;

public class Commands implements CommandExecutor {

    private Heart_Manager plugin;

    public Commands(Heart_Manager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if(!sender.isOp()) return true;
            Player player = (Player) sender;
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("getRerole")){
                    ItemStack item = plugin.createAdvancedItem(Material.SADDLE, "re-role");
                    sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix")+ ChatColor.WHITE + "Re-role item received.");
                    plugin.giveItem(player, item);
                }
                else if(args[0].equalsIgnoreCase("getHeart")){
                    ItemStack item = plugin.createAdvancedItem(Material.HEART_OF_THE_SEA, "heart");
                    sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix")+ ChatColor.WHITE + "Heart received.");
                    plugin.giveItem(player, item);
                }
            }
            else if(args.length == 3){
                if(args[0].equalsIgnoreCase("set")){
                    String target = args[1];
                    Player targetPlayer = getServer().getPlayer(target);
                    if(targetPlayer != null){
                        targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Integer.parseInt(args[2]) * 2);
                        sender.sendMessage(ChatColor.RED + plugin.getConfig().getString("prefix")+ ChatColor.AQUA + target + ChatColor.WHITE + " has now " + args[2]+" hearts.");
                    }
                    else sender.sendMessage(ChatColor.RED + target + "is not online.");
                }
            }
        }
        return true;
    }
}
