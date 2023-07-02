package me.surge.mixins;

import net.minecraft.advancement.Advancement;
import net.minecraft.client.toast.AdvancementToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author surge
 * @since 02/07/2023
 */
@Mixin(AdvancementToast.class)
public interface IAdvancementToast {

    @Accessor("advancement")
    Advancement getAdvancement();

}
