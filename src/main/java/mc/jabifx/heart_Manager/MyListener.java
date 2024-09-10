package mc.jabifx.heart_Manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.attribute.Attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyListener implements Listener {

    private Heart_Manager plugin;

    public MyListener (Heart_Manager plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FileConfiguration config = plugin.getConfig();
        List<Map<?, ?>> players = this.plugin.getPlayers();
        String playerName = event.getPlayer().getName();
        int vida = 20;

        if (players.isEmpty()) {
            Map<String, Integer> newPlayer = new HashMap<>();
            newPlayer.put(playerName, vida);
            players.add(newPlayer);
            config.set("players", players);
            this.plugin.saveConfig();
        }
        else{
            boolean playerFound = false;

            for (Map<?, ?> p : players) {
                if (p.containsKey(playerName)) {
                    vida = (int) p.get(playerName);
                    playerFound = true;
                    break;
                }
            }

            if (!playerFound) {
                Map<String, Integer> newPlayer = new HashMap<>();
                newPlayer.put(playerName, vida);
                players.add(newPlayer);
                config.set("players", players);
                this.plugin.saveConfig();
            }
        }
        //event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(vida);
        //event.getPlayer().setHealth(vida);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity(); // Jugador que murió
        Player killer = victim.getKiller(); // Asesino (puede ser null si no es un jugador)

        if (killer != null) {
            double victimMaxHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double killerMaxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (victimMaxHealth - 2 >= this.plugin.getConfig().getInt("config.minHearts") * 2) {
                victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(victimMaxHealth - 2);
                victim.setHealth(victimMaxHealth - 2);
            }
            if (killerMaxHealth + 2 <= this.plugin.getConfig().getInt("config.maxHearts") * 2) {
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killerMaxHealth + 2);
                killer.setHealth(killerMaxHealth);
            }
        }
    }
}
