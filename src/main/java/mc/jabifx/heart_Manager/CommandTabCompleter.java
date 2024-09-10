package mc.jabifx.heart_Manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].startsWith("set")) completions.add("set");
            else if (args[0].startsWith("getItem")) completions.add("getItem");
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            for (Player player : sender.getServer().getOnlinePlayers()) completions.add(player.getName());
        }
        return completions;
    }
}
