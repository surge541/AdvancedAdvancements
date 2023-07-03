package me.surge.mixins;

import net.minecraft.client.toast.RecipeToast;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author surge
 * @since 03/07/2023
 */
@Mixin(RecipeToast.class)
public interface IRecipeToast {

    @Accessor("recipes")
    List<Recipe<?>> getRecipes();

}
