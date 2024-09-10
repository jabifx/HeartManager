package mc.jabifx.heart_Manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Heart_Manager extends JavaPlugin {

    private List<Map<?, ?>> players = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        getServer().getPluginManager().registerEvents(new MyListener(this), this);
        this.players = config.getMapList("players");
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public List<Map<?, ?>> getPlayers() {
        return players;
    }
}
