package mc.jabifx.heart_Manager.Commands;

import mc.jabifx.heart_Manager.Utils.HealthManager;
import mc.jabifx.heart_Manager.Heart_Manager;
import mc.jabifx.heart_Manager.Utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WithdrawCommand implements CommandExecutor {

    private final Heart_Manager plugin = Heart_Manager.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        String prefix = ChatColor.RED + plugin.getConfig().getString("prefix") + ChatColor.WHITE + " ";

        if (args.length != 1) {
            player.sendMessage(prefix + plugin.getConfig().getString("withdraw.usage"));
            return false;
        }

        int maxCorazones = (int) (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2);

        try {
            int cantidad = Integer.parseInt(args[0]);

            if (cantidad <= 0) {
                player.sendMessage(prefix + plugin.getConfig().getString("withdraw.positive-number"));
                return false;
            }

            if (cantidad >= maxCorazones) {
                player.sendMessage(prefix + plugin.getConfig().getString("withdraw.exceed-max")
                        .replace("{max}", String.valueOf(maxCorazones)));
                return false;
            }

            if (!HealthManager.adjustPlayerHealth(player, -cantidad * 2)) {
                player.sendMessage(prefix + plugin.getConfig().getString("withdraw.withdraw-fail"));
                return false;
            }

            ItemStack item = ItemUtils.createAdvancedItem(Material.HEART_OF_THE_SEA, "heart", cantidad);
            ItemUtils.giveItem(player, item);

            player.sendMessage(prefix + plugin.getConfig().getString("withdraw.withdraw-success")
                    .replace("{amount}", String.valueOf(cantidad)));

        } catch (NumberFormatException ignored) {}

        return true;
    }
}
