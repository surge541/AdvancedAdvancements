package me.surge.mixins;

import me.surge.duck.DDrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author surge
 * @since 03/07/2023
 */
@Mixin(DrawContext.class)
public abstract class MixinDrawContext implements DDrawContext {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private MatrixStack matrices;

    @Shadow
    public abstract void draw();

    @Shadow
    public abstract VertexConsumerProvider.Immediate getVertexConsumers();

    @Override
    public void drawItemWithoutEntityF(ItemStack stack, float x, float y) {
        if (!stack.isEmpty()) {
            BakedModel bakedModel = this.client.getItemRenderer().getModel(stack, this.client.world, null, 0);
            this.matrices.push();
            this.matrices.translate(x + 8f, y + 8f, 150f);

            try {
                this.matrices.multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
                this.matrices.scale(16.0F, 16.0F, 16.0F);

                boolean bl = !bakedModel.isSideLit();

                if (bl) {
                    DiffuseLighting.disableGuiDepthLighting();
                }

                this.client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, this.matrices, this.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, bakedModel);

                this.draw();

                if (bl) {
                    DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable var12) {
                CrashReport crashReport = CrashReport.create(var12, "Rendering item");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");

                crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.add("Item Damage", () -> String.valueOf(stack.getDamage()));
                crashReportSection.add("Item NBT", () -> String.valueOf(stack.getNbt()));
                crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));

                throw new CrashException(crashReport);
            }

            this.matrices.pop();
        }
    }

}
