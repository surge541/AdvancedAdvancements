package me.surge.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import me.surge.animation.Animation;
import me.surge.config.Config;
import net.minecraft.advancement.Advancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static me.surge.animation.Easing.LINEAR;
import static me.surge.nanovg.Renderer.*;

/**
 * @author surge
 * @since 02/07/2023
 */
public class AdvancedToast {

    private final Advancement advancement;

    private Animation fadeIn = null;
    private Animation hold = null;
    private Animation scissor = null;
    private Animation fadeOut = null;

    private final String title;
    private final Color colour;

    public AdvancedToast(Advancement advancement) {
        this.advancement = advancement;

        title = switch (advancement.getDisplay().getFrame()) {
            case TASK -> "Task completed!";
            case CHALLENGE -> "Challenge completed!";
            case GOAL -> "Goal completed!";
        };

        colour = Color.decode(switch (advancement.getDisplay().getFrame()) {
            case TASK -> Config.TASK.getValue();
            case CHALLENGE -> Config.CHALLENGE.getValue();
            case GOAL -> Config.GOAL.getValue();
        });
    }

    public Data draw(DrawContext context, int width, int height) {
        // these are only initialised here so that config reloading actually has an effect if there are many
        // advancements in the queue
        if (fadeIn == null) {
            fadeIn = new Animation(Config.FADE_IN.getValue(), false, Config.FADE_IN_EASING.getValue());
            hold = new Animation(Config.HOLD.getValue(), false, LINEAR);
            scissor = new Animation(Config.HOLD.getValue() / 2f, false, LINEAR);
            fadeOut = new Animation(Config.FADE_OUT.getValue(), true, Config.FADE_OUT_EASING.getValue());
        }

        float scaleFactor = Config.SCALE.getValue();

        float toastWidth = (255 * 2) * scaleFactor;
        float toastHeight = (32 * 2) * scaleFactor;

        float x = (width / 2f) - (toastWidth / 2f);
        float y = 50;

        float factor = (float) fadeIn.getAnimationFactor();

        // animation queue
        fadeIn.setState(true);

        if (fadeIn.getLinearFactor() == 1.0) {
            hold.setState(true);
            scissor.setState(true);
            factor = 1f;
        }

        if (hold.getLinearFactor() == 1.0 && hold.getState()) {
            fadeOut.setState(false);
            factor = (float) fadeOut.getAnimationFactor();
        }

        float finalFactor = factor;

        frame(() -> {
            // scale intro
            scale(finalFactor, x + (toastWidth / 2), y + (toastHeight / 2));

            // background texture
            texture(this.advancement.getDisplay().getFrame().name().toLowerCase(), x, y, toastWidth, toastHeight);

            // title and name
            text(this.title, x + (72 * scaleFactor), y + (20 * scaleFactor), colour, 16 * scaleFactor);
            text(this.advancement.getDisplay().getTitle().getString(), x + (76 * scaleFactor), y + (40 * scaleFactor), Color.GRAY, 12 * scaleFactor);
        });

        return new Data(factor, x, y, toastWidth, toastHeight);
    }

    public Advancement getAdvancement() {
        return advancement;
    }

    public boolean finished() {
        return fadeIn.getAnimationFactor() == 1.0 && fadeIn.getState() && fadeOut.getAnimationFactor() == 0.0 && !fadeOut.getState();
    }

    public static class Data {

        private float factor;

        private final float toastX;
        private final float toastY;
        private final float toastWidth;
        private final float toastHeight;

        public Data(float factor, float x, float y, float w, float h) {
            this.factor = factor;
            this.toastX = x;
            this.toastY = y;
            this.toastWidth = w;
            this.toastHeight = h;
        }

        public float getFactor() {
            return factor;
        }

        public void setFactor(float factor) {
            this.factor = factor;
        }

        public float getToastX() {
            return toastX;
        }

        public float getToastY() {
            return toastY;
        }

        public float getToastWidth() {
            return toastWidth;
        }

        public float getToastHeight() {
            return toastHeight;
        }

    }

}
