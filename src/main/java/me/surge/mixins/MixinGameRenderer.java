package me.surge.mixins;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.surge.toasts.AdvancedToastManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author surge
 * @since 02/07/2023
 */
@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow @Final private BufferBuilderStorage buffers;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V", shift = At.Shift.AFTER))
    public void hookRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world != null) {
            Window window = MinecraftClient.getInstance().getWindow();
            AdvancedToastManager.draw(new DrawContext(MinecraftClient.getInstance(), this.buffers.getEntityVertexConsumers()), window.getWidth(), window.getHeight());

            GlStateManager._disableCull();
            GlStateManager._disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        }
    }

}
