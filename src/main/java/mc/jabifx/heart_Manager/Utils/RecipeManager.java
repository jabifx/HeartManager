package mc.jabifx.heart_Manager.Utils;

import mc.jabifx.heart_Manager.Heart_Manager;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;

import java.util.List;

public class RecipeManager {

    private final Heart_Manager plugin;

    public RecipeManager(Heart_Manager plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        // Heart Recipe
        ItemStack heartItem = ItemUtils.createAdvancedItem(Material.HEART_OF_THE_SEA, "heart", 1);
        NamespacedKey heartKey = new NamespacedKey(plugin, "heart");
        ShapedRecipe heartRecipe = new ShapedRecipe(heartKey, heartItem);

        List<String> shape = plugin.getConfig().getStringList("heart recipe.shape");
        heartRecipe.shape(shape.get(0), shape.get(1), shape.get(2));

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("heart recipe.ingredients");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                char symbol = key.charAt(0);
                Material mat = Material.matchMaterial(section.getString(key));
                if (mat != null) heartRecipe.setIngredient(symbol, mat);
            }
        }

        Bukkit.addRecipe(heartRecipe);

        // Revive Recipe
        ItemStack reviveItem = ItemUtils.createAdvancedItem(Material.NETHER_STAR, "revive", 1);
        NamespacedKey reviveKey = new NamespacedKey(plugin, "revive");
        ShapedRecipe reviveRecipe = new ShapedRecipe(reviveKey, reviveItem);
        reviveRecipe.shape("RHR", "HSH", "RHR");
        reviveRecipe.setIngredient('H', new RecipeChoice.ExactChoice(heartItem));
        reviveRecipe.setIngredient('S', Material.NETHER_STAR);
        reviveRecipe.setIngredient('R', Material.END_CRYSTAL);
        Bukkit.addRecipe(reviveRecipe);
    }
}
