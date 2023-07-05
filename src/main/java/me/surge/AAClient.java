package me.surge;

import me.surge.config.Config;
import me.surge.nanovg.Renderer;
import me.surge.registry.ARegistries;
import net.fabricmc.api.ClientModInitializer;

/**
 * @author surge
 * @since 02/07/2023
 */
public class AAClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Renderer.terminate();
            Config.save();
        }));

        Config.load();

        // create file if it doesn't
        Config.save();

        ARegistries.register();
    }

}
