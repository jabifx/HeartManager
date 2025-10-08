package mc.jabifx.heart_Manager;

import mc.jabifx.heart_Manager.Commands.Commands;
import mc.jabifx.heart_Manager.Commands.WithdrawCommand;
import mc.jabifx.heart_Manager.Listeners.MyListener;
import mc.jabifx.heart_Manager.Utils.RecipeManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Heart_Manager extends JavaPlugin {

    private static Heart_Manager instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        new RecipeManager(this).registerRecipes();
        getServer().getPluginManager().registerEvents(new MyListener(), this);
        this.getCommand("hearts").setExecutor(new Commands());
        this.getCommand(getConfig().getString("withdraw.command")).setExecutor(new WithdrawCommand());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static Heart_Manager getInstance() {
        return instance;
    }
}
