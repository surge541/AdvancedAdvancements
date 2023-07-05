package me.surge.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * @author surge
 * @since 04/07/2023
 */
public class ARegistries {

    public static RegistryEntry.Reference<SoundEvent> TASK = register("task_complete");
    public static RegistryEntry.Reference<SoundEvent> GOAL = register("goal_complete");

    public static void register() {}

    private static RegistryEntry.Reference<SoundEvent> register(String id) {
        Identifier identifier = new Identifier("advancedadvancements", id);

        return Registry.registerReference(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

}
