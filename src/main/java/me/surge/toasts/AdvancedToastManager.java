package me.surge.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import me.surge.config.Config;
import me.surge.mixins.IDrawContext;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author surge
 * @since 02/07/2023
 */
public class AdvancedToastManager {

    private static final Queue<AdvancedToast> toastQueue = new ConcurrentLinkedQueue<>();

    public static void draw(DrawContext context, int width, int height) {
        AdvancedToast toast = toastQueue.peek();

        if (toast == null) {
            return;
        }

        AdvancedToast.Data data = toast.draw(context, width, height);

        MatrixStack stack = context.getMatrices();

        ((IDrawContext) context).setMatrices(new MatrixStack());

        data.setFactor(MathHelper.clamp(data.getFactor(), 0.0001f, 1f));

        float mcScale = MinecraftClient.getInstance().options.getGuiScale().getValue();

        // scale
        context.getMatrices().translate((data.getToastX() + (data.getToastWidth() / 2)) / mcScale, (data.getToastY() + (data.getToastHeight() / 2)) / mcScale, 200.0);
        context.getMatrices().scale(data.getFactor(), data.getFactor(), data.getFactor());
        context.getMatrices().translate(-((data.getToastX() + (data.getToastWidth() / 2)) / mcScale), -((data.getToastY() + (data.getToastHeight() / 2)) / mcScale), -200.0);

        float scale = 1f / mcScale;
        context.getMatrices().scale(scale, scale, scale);

        float configScale = Config.SCALE.getValue();

        float offset = 16;

        if (toast.getAdvancement().getDisplay().getFrame().equals(AdvancementFrame.GOAL)) {
            offset = 14;
        }

        offset *= configScale;

        context.getMatrices().translate(((int) (data.getToastX() + offset)), ((data.getToastY() + (16 * configScale))), 200.0);
        context.getMatrices().scale(2, 2, 2);
        context.getMatrices().translate(-(((data.getToastX() + offset))), -(((data.getToastY() + (16 * configScale)))), -200.0);

        context.getMatrices().translate(((int) (data.getToastX() + offset)), ((data.getToastY() + (16 * configScale))), 200.0);
        context.getMatrices().scale(configScale, configScale, configScale);
        context.getMatrices().translate(-(((data.getToastX() + offset))), -(((data.getToastY() + (16 * configScale)))), -200.0);

        RenderSystem.setShaderColor(1f, 1f, 1f, data.getFactor());

        context.drawItemWithoutEntity(toast.getAdvancement().getDisplay().getIcon(), (int) (data.getToastX() + offset), (int) (data.getToastY() + (16 * configScale)));

        ((IDrawContext) context).setMatrices(stack);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();

        if (toast.finished()) {
            toastQueue.poll();
        }
    }

    public static void add(AdvancedToast toast) {
        toastQueue.add(toast);
    }

}
