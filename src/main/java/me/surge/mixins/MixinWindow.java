package me.surge.mixins;

import me.surge.config.Config;
import me.surge.nanovg.Renderer;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;

/**
 * @author surge
 * @since 02/07/2023
 */
@Mixin(Window.class)
public class MixinWindow {

    @Shadow @Final private long handle;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void hookInit(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci) {
        Renderer.initialise();

        glfwSetWindowFocusCallback(handle, new GLFWWindowFocusCallback() {

            @Override
            public void invoke(long window, boolean focused) {
                Config.load();
            }

        });
    }

}
