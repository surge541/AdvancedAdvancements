package me.surge.toasts;

import me.surge.config.Config;
import net.minecraft.advancement.Advancement;
import net.minecraft.item.ItemStack;

import java.awt.*;

/**
 * @author surge
 * @since 03/07/2023
 */
public class AdvancedAdvancementToast extends AdvancedToast {

    private final Advancement advancement;

    private final String title;
    private final Color titleColour;

    public AdvancedAdvancementToast(Advancement advancement) {
        this.advancement = advancement;

        title = switch (advancement.getDisplay().getFrame()) {
            case TASK -> "Task completed!";
            case CHALLENGE -> "Challenge completed!";
            case GOAL -> "Goal completed!";
        };

        titleColour = Color.decode(switch (advancement.getDisplay().getFrame()) {
            case TASK -> Config.TASK.getValue();
            case CHALLENGE -> Config.CHALLENGE.getValue();
            case GOAL -> Config.GOAL.getValue();
        });
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

}
