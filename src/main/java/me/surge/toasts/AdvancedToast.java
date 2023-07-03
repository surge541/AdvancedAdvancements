package me.surge.toasts;

import me.surge.animation.Animation;
import me.surge.config.Config;
import me.surge.config.EntryAnimation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import java.awt.*;

import static me.surge.animation.Easing.LINEAR;
import static me.surge.nanovg.Renderer.*;
import static me.surge.nanovg.Renderer.text;

/**
 * @author surge
 * @since 03/07/2023
 */
public abstract class AdvancedToast {

    private Animation fadeIn = null;
    private Animation hold = null;
    private Animation scissor = null;
    private Animation fadeOut = null;

    private final Color messageColour = Color.decode(Config.MESSAGE.getValue());

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

        float y = Config.ENTRY_ANIMATION.getValue().equals(EntryAnimation.SLIDE) ? -toastHeight + ((Config.Y_OFFSET.getValue() + toastHeight) * factor) : Config.Y_OFFSET.getValue();

        float finalFactor = factor;

        frame(() -> {
            // scale intro
            if (Config.ENTRY_ANIMATION.getValue().equals(EntryAnimation.SCALE)) {
                scale(finalFactor, x + (toastWidth / 2), y + (toastHeight / 2));
            } else if (Config.ENTRY_ANIMATION.getValue().equals(EntryAnimation.FLASH)) {
                if (!(fadeIn.getAnimationFactor() > 0.5 || hold.getState()) || fadeOut.getAnimationFactor() < 0.5 && !fadeOut.getState()) {
                    return;
                }
            }

            // background texture
            texture(this.getBackground().name().toLowerCase(), x, y, toastWidth, toastHeight);

            // title and name
            text(this.getTitle(), x + (72 * scaleFactor), y + (20 * scaleFactor), this.getTitleColour(), 16 * scaleFactor);
            text(this.getMessage(), x + (76 * scaleFactor), y + (40 * scaleFactor), messageColour, 12 * scaleFactor);
        });

        return new Data(factor, x, y, toastWidth, toastHeight);
    }

    public boolean finished() {
        return fadeIn.getAnimationFactor() == 1.0 && fadeIn.getState() && fadeOut.getAnimationFactor() == 0.0 && !fadeOut.getState();
    }

    public abstract String getTitle();
    public abstract Color getTitleColour();
    public abstract String getMessage();

    public abstract Background getBackground();
    public abstract ItemStack getIcon();

    public Animation getFadeIn() {
        return fadeIn;
    }

    public Animation getHold() {
        return hold;
    }

    public Animation getScissor() {
        return scissor;
    }

    public Animation getFadeOut() {
        return fadeOut;
    }

    public Color getMessageColour() {
        return messageColour;
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

    public enum Background {
        TASK,
        GOAL,
        CHALLENGE
    }

}
