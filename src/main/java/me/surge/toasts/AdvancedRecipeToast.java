package me.surge.toasts;

import com.google.common.collect.Lists;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import me.surge.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;

import java.awt.*;
import java.util.List;

/**
 * @author surge
 * @since 03/07/2023
 */
public class AdvancedRecipeToast extends AdvancedToast {

    private final List<Recipe<?>> recipes = Lists.newArrayList();
    private final Color titleColour = Color.decode(Config.RECIPES_COLOUR.getValue());

    private Animation cycle;
    private int index = 0;

    public void add(Recipe<?> recipe) {
        this.recipes.add(recipe);
    }

    @Override
    public String getTitle() {
        return "Recipes Unlocked!";
    }

    @Override
    public Color getTitleColour() {
        return titleColour;
    }

    @Override
    public String getMessage() {
        return this.recipes.size() + " new recipes unlocked!";
    }

    @Override
    public Background getBackground() {
        return Background.GOAL;
    }

    @Override
    public ItemStack getIcon() {
        if (MinecraftClient.getInstance().world == null) {
            return new ItemStack(Items.CRAFTING_TABLE);
        }

        if (cycle == null) {
            cycle = new Animation((float) Config.HOLD.getValue() / this.recipes.size(), false, Easing.LINEAR);
        }

        cycle.setState(true);

        if (cycle.getAnimationFactor() == 1.0) {
            // *weeps*
            cycle = new Animation((float) Config.HOLD.getValue() / this.recipes.size(), false, Easing.LINEAR);

            index++;
        }

        if (index >= this.recipes.size()) {
            index = 0;
        }

        return recipes.get(this.index).getOutput(MinecraftClient.getInstance().world.getRegistryManager());
    }

}
