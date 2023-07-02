package me.surge.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author surge
 * @since 02/07/2023
 */
@Mixin(DrawContext.class)
public interface IDrawContext {

    @Mutable
    @Accessor("matrices")
    void setMatrices(MatrixStack stack);

}
