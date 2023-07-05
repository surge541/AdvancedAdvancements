package me.surge.toasts;

import me.surge.config.Config;
import me.surge.registry.ARegistries;
import net.minecraft.advancement.Advancement;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

/**
 * @author surge
 * @since 03/07/2023
 */
public class AdvancedAdvancementToast extends AdvancedToast {

    private final Advancement advancement;

    private final String title;
    private final Color titleColour;

    private final SoundEvent sound;

    public AdvancedAdvancementToast(Advancement advancement) {
        this.advancement = advancement;

        title = switch (advancement.getDisplay().getFrame()) {
            case TASK -> "Task completed!";
            case CHALLENGE -> "Challenge completed!";
            case GOAL -> "Goal completed!";
        };

        titleColour = Color.decode(switch (advancement.getDisplay().getFrame()) {
            case TASK -> Config.TASK.get();
            case CHALLENGE -> Config.CHALLENGE.get();
            case GOAL -> Config.GOAL.get();
        });

        sound = switch (advancement.getDisplay().getFrame()) {
            case TASK -> ARegistries.TASK.value();
            case CHALLENGE -> SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
            case GOAL -> ARegistries.GOAL.value();
        };
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Color getTitleColour() {
        return titleColour;
    }

    @Override
    public String getMessage() {
        return this.advancement.getDisplay().getTitle().getString();
    }

    @Override
    public Background getBackground() {
        return Background.valueOf(this.advancement.getDisplay().getFrame().name());
    }

    @Override
    public ItemStack getIcon() {
        return this.advancement.getDisplay().getIcon();
    }

    @Override
    public SoundEvent getSound() {
        return sound;
    }

}
